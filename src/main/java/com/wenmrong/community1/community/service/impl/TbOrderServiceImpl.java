package com.wenmrong.community1.community.service.impl;

import com.wenmrong.community1.community.model.TbOrder;
import com.wenmrong.community1.community.mapper.TbOrderMapper;
import com.wenmrong.community1.community.service.TbOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wenmrong.community1.community.utils.SnowFlake;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author autodidact
 * @since 2022-08-17
 */
@Service
public class TbOrderServiceImpl extends ServiceImpl<TbOrderMapper, TbOrder> implements TbOrderService {
    private static final Logger log = LoggerFactory.getLogger(TbOrderServiceImpl.class);

    static SnowFlake snowFlake = new SnowFlake(2, 3);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;



    public void placeOrder() {
        final long id = snowFlake.nextId();
        TbOrder order = new TbOrder();
        order.setOrderId(String.valueOf(id));
        Message<TbOrder> message = MessageBuilder.withPayload(order)
                .setHeader(RocketMQHeaders.TRANSACTION_ID, String.valueOf(id))
                .build();
        rocketMQTemplate.sendMessageInTransaction("ts_order_topic",message,null);
    }

}
