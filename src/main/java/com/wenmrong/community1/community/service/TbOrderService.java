package com.wenmrong.community1.community.service;

import com.wenmrong.community1.community.model.TbOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author autodidact
 * @since 2022-08-17
 */
public interface TbOrderService extends IService<TbOrder> {
    public void placeOrder();

}
