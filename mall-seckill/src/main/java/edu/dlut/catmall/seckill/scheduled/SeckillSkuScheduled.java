package edu.dlut.catmall.seckill.scheduled;

import edu.dlut.catmall.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/29  00:11
 * DESCRIPTION: 秒杀商品的定时上架
 *  每天晚上三点 上架最近三天需要秒杀的商品
 **/
@Slf4j
@Service
public class SeckillSkuScheduled {

    @Resource
    private SeckillService seckillService;

    @Resource
    private RedissonClient redissonClient;

    private final String UPLOAD_STOCK = "seckill:upload:lock";

    @Scheduled(cron = "*/3 * * * * ?")
    public void uploadSeckillSkuLatest3Days() {
        // 重复上架无需处理
        log.info("上架秒杀的商品信息");
        // 分布式锁 锁的业务执行完成 状态更新完成 释放锁以后 其他操作不会被阻塞很长时间
        RLock lock = redissonClient.getLock(UPLOAD_STOCK);
        lock.lock(10, TimeUnit.SECONDS);
        try {
            seckillService.uploadSeckillSkuLatest3Days();
        } finally {
            lock.unlock();;
        }
    }

}
