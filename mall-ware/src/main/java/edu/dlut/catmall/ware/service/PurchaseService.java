package edu.dlut.catmall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlut.catmall.ware.vo.MergeVo;
import edu.dlut.catmall.ware.vo.PurchaseDoneVo;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.catmall.ware.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:51:46
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceivePurchase(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);

    void received(List<Long> ids);

    void done(PurchaseDoneVo doneVo);
}

