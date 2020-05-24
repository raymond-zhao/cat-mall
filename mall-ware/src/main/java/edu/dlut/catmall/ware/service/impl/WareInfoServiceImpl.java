package edu.dlut.catmall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import edu.dlut.catmall.ware.feign.MemberFeign;
import edu.dlut.catmall.ware.vo.FareVO;
import edu.dlut.catmall.ware.vo.MemberAddressVO;
import edu.dlut.common.utils.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.common.utils.Query;

import edu.dlut.catmall.ware.dao.WareInfoDao;
import edu.dlut.catmall.ware.entity.WareInfoEntity;
import edu.dlut.catmall.ware.service.WareInfoService;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Resource
    private MemberFeign memberFeign;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.eq("id", key).or()
                    .like("name", key).or()
                    .like("address", key).or()
                    .like("areacode", key);
        }
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public FareVO getFare(Long attrId) {
        FareVO fareVO = new FareVO();
        R r = memberFeign.addrInfo(attrId);
        MemberAddressVO data = r.getData("memberReceiveAddress", new TypeReference<MemberAddressVO>() {});
        if (!ObjectUtils.isEmpty(data)) {
            String phone = data.getPhone();
            String substring = phone.substring(phone.length() - 1);
            fareVO.setFare(new BigDecimal(substring));
            fareVO.setMemberAddressVO(data);
            return fareVO;
        }
        return null;
    }

}