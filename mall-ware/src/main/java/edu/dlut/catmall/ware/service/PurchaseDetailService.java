package edu.dlut.catmall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.catmall.ware.entity.PurchaseDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:51:46
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PurchaseDetailEntity> listDetailByPurchaseId(Long id);
}

