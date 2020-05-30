package edu.dlut.catmall.seckill.service.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import edu.dlut.catmall.seckill.feign.CouponFeign;
import edu.dlut.catmall.seckill.feign.ProductFeign;
import edu.dlut.catmall.seckill.interceptor.LoginUserInterceptor;
import edu.dlut.catmall.seckill.service.SeckillService;
import edu.dlut.catmall.seckill.to.SeckillSkuRedisTO;
import edu.dlut.catmall.seckill.vo.SeckillSessionWithSkus;
import edu.dlut.catmall.seckill.vo.SkuInfoVO;
import edu.dlut.common.to.mq.SeckillOrderTO;
import edu.dlut.common.utils.R;
import edu.dlut.common.vo.MemberResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/29  00:15
 * DESCRIPTION:
 **/
@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {

    @Resource
    private CouponFeign couponFeign;

    @Resource
    private ProductFeign productFeign;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RedissonClient redissonClient;

    private final String SESSION_CACHE_PREFIX = "seckill:session:";

    private final String SKUKILL_CACHE_PREFIX = "seckill:skus";

    private final String SKU_STOCK_SEMAPHORE = "seckill:stock:";

    @Override
    public void uploadSeckillSkuLatest3Days() {
        // 扫描最近三天需要参与秒杀的活动
        R session = couponFeign.getLatest3DaySession();
        if (session.getCode() == 0) {
            // 查询上架商品
            List<SeckillSessionWithSkus> sessionData = session.getData(new TypeReference<List<SeckillSessionWithSkus>>() {});
            // 缓存到 Redis
            if (!CollectionUtils.isEmpty(sessionData)) {
                // 1. 缓存活动信息
                saveSessionInfo(sessionData);
                // 2. 缓存活动的关联商品信息
                saveSessionSkuInfo(sessionData);
            }
        }
    }

    public List<SeckillSkuRedisTO> handleGetCurrentSeckillSkus(BlockException e) {
        log.info("回调方法");
        return new ArrayList<>();
    }

    @SentinelResource(value = "getCurrentSeckillSkus", blockHandler = "handleGetCurrentSeckillSkus")
    @Override
    public List<SeckillSkuRedisTO> getCurrentSeckillSkus() {
        // 1 确定当前时间属于哪个秒杀场次
        long time = new Date().getTime();
        // 获取以 前缀开头的所有key
        Set<String> keys = stringRedisTemplate.keys(SESSION_CACHE_PREFIX + "*");
        try (Entry entry = SphU.entry("seckillSkus")) {
            for (String key : keys) {
                //  key:  seckill:session:16722xxxxx
                String newKey = key.replace(SESSION_CACHE_PREFIX, "");
                String[] timeRange = newKey.split("_");
                long start = Long.parseLong(timeRange[0]);
                long end = Long.parseLong(timeRange[1]);
                if (time >= start && time <= end) {
                    // 获取这个秒杀场次的所有商品信息
                    List<String> range = stringRedisTemplate.opsForList().range(key, -100, 100);
                    BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
                    assert range != null;
                    List<String> strings = hashOps.multiGet(range);
                    if (!CollectionUtils.isEmpty(strings)) {
                        return strings.stream().map(item -> JSON.parseObject(item, SeckillSkuRedisTO.class))
                                .collect(Collectors.toList());
                    }
                    break;
                }
            }
        } catch (BlockException e) {
            log.info("sentinel回调...", e);
        }
        // 2 获取这个秒杀场次需要的所有商品信息
        return null;
    }

    @Override
    public SeckillSkuRedisTO getSkuSeckillInfo(Long skuId) {
        // 找到所有需要参与秒杀的商品的 key
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        Set<String> keys = hashOps.keys();
        if (!CollectionUtils.isEmpty(keys)) {
            String regex = "\\d_" + skuId;
            for (String key : keys) {
                if (Pattern.matches(regex, key)) {
                    String json = hashOps.get(key);
                    SeckillSkuRedisTO skuRedisTO = JSON.parseObject(json, SeckillSkuRedisTO.class);

                    // 随机码
                    long current = new Date().getTime();
                    assert skuRedisTO != null;
                    if (current >= skuRedisTO.getStartTime() && current <= skuRedisTO.getEndTime()) {
                        // 如果在秒杀时间内 随机码是必要的 所以不作任何处理
                    } else {
                        skuRedisTO.setRandomCode(null);
                    }
                    return skuRedisTO;
                }
            }
        }
        return null;
    }

    /**
     * TODO 上架秒杀商品时 每一个数据都应该设置过期时间
     * 秒杀后续的流程 简化了收获地址等信息
     * @param killId
     * @param key
     * @param num
     * @return
     */
    @Override
    public String kill(String killId, String key, Integer num) {
        MemberResponseVO member = LoginUserInterceptor.loginUser.get();
        // 获取当前秒杀商品的详细信息
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        String json = hashOps.get(killId);
        if (StringUtils.isEmpty(json)) {
            return "";
        } else {
            // 校验随机码与商品ID
            SeckillSkuRedisTO redisTO = JSON.parseObject(json, SeckillSkuRedisTO.class);
            Long startTime = redisTO.getStartTime();
            Long endTime = redisTO.getEndTime();
            long ttl = endTime - startTime;
            long time = new Date().getTime();
            if (time >= startTime && time <= endTime) {
                // 如果在秒杀时间内
                String randomCode = redisTO.getRandomCode();
                String skuId = redisTO.getPromotionSessionId() + "_" + redisTO.getSkuId();
                if (randomCode.equals(key) && killId.equals(skuId)) {
                    // 验证秒杀数量是否合理
                    if (num <= redisTO.getSeckillLimit()) {
                        // 验证当前用户是否已参与过秒杀，只要秒杀成功就去 redis 占位 数据格式 userId_sessionId_skuId
                        String rediskey = member.getId() + "_" + skuId;
                        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(rediskey, num.toString(), ttl, TimeUnit.MILLISECONDS);
                        if (aBoolean) {
                            // 占位成功说明之前未参与过秒杀活动
                            // 引入分布式信号量
                            RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
                            boolean b = semaphore.tryAcquire(num);
                            if (b) {
                                // 秒杀成功 快速下单 发送消息到 MQ 整个操作时间在 10ms 左右
                                String timeId = IdWorker.getTimeId();
                                SeckillOrderTO seckillOrderTO = new SeckillOrderTO();
                                seckillOrderTO.setOrderSn(timeId);
                                seckillOrderTO.setMemberId(member.getId());
                                seckillOrderTO.setNum(num);
                                seckillOrderTO.setPromotionSessionId(redisTO.getPromotionSessionId());
                                seckillOrderTO.setSkuId(redisTO.getSkuId());
                                seckillOrderTO.setSeckillPrice(redisTO.getSeckillPrice());
                                rabbitTemplate.convertAndSend("order-event-exchange", "order.seckill.queue", seckillOrderTO);
                                return timeId;
                            }
                            return "";
                        } else {
                            return "";
                        }
                    }
                } else {
                    return "";
                }
            } else {
                // 秒杀时间已过
                return "";
            }
        }
        return "";
    }

    private void saveSessionInfo(List<SeckillSessionWithSkus> sessionData) {
        sessionData.forEach(session -> {
            long startTime = session.getStartTime().getTime();
            long endTime = session.getEndTime().getTime();
            String key = SESSION_CACHE_PREFIX + startTime + "_" + endTime;
            Boolean hasKey = stringRedisTemplate.hasKey(key);
            if (!hasKey) {
                List<String> collect = session.getRelationSkus().stream()
                        .map(item -> item.getPromotionSessionId() + "_" + item.getSkuId().toString()).collect(Collectors.toList());
                stringRedisTemplate.opsForList().leftPushAll(key, collect);
            }
        });
    }

    private void saveSessionSkuInfo(List<SeckillSessionWithSkus> sessionData) {
        sessionData.forEach(session -> {
            // 准备哈希操作
            BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
            session.getRelationSkus().forEach(seckillSkuVO -> {
                String token = UUID.randomUUID().toString().replace("-", "");

                if (!ops.hasKey(seckillSkuVO.getPromotionSessionId().toString() + "_" + seckillSkuVO.getSkuId().toString())) {
                    // 缓存商品
                    SeckillSkuRedisTO redisTO = new SeckillSkuRedisTO();
                    // SKU 的基本数据
                    R skuInfo = productFeign.getSkuInfo(seckillSkuVO.getSkuId());
                    if (skuInfo.getCode() == 0) {
                        SkuInfoVO info = skuInfo.getData("skuInfo", new TypeReference<SkuInfoVO>() {
                        });
                        redisTO.setSkuInfo(info);
                    }

                    // SKU 的秒杀信息
                    BeanUtils.copyProperties(seckillSkuVO, redisTO);

                    // 设置当前商品的秒杀时间信息
                    redisTO.setStartTime(session.getStartTime().getTime());
                    redisTO.setEndTime(session.getEndTime().getTime());

                    // 引入随机码
                    redisTO.setRandomCode(token);

                    // 保存到 redis
                    String redisJSONString = JSON.toJSONString(seckillSkuVO);
                    ops.put(seckillSkuVO.getPromotionSessionId() + "_" + seckillSkuVO.getSkuId(), redisJSONString);

                    // 使用库存作为分布式信号量
                    RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
                    // 商品可以秒杀的数量作为信号量
                    semaphore.trySetPermits(seckillSkuVO.getSeckillCount());
                }
            });
        });
    }
}
