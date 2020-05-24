package edu.dlut.catmall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlut.catmall.ware.entity.WareInfoEntity;
import edu.dlut.catmall.ware.vo.FareVO;
import edu.dlut.common.utils.PageUtils;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:51:46
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    FareVO getFare(Long attrId);
}

