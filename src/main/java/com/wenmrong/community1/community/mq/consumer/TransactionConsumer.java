package com.wenmrong.community1.community.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.wenmrong.community1.community.model.NftOrder;
import com.wenmrong.community1.community.service.QuestionService;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "ts_order_topic",consumerGroup = "ts_order_consumer")
public class TransactionConsumer implements RocketMQListener<NftOrder> {
    private static final Logger log = LoggerFactory.getLogger(TransactionConsumer.class);

    @Autowired
    QuestionService questionService;

    @Override
    public void onMessage(NftOrder message) {
        log.error("事务消息：{}", JSONObject.toJSONString(message));
    }
}
