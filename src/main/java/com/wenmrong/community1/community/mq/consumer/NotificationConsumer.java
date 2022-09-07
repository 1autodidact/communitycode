package com.wenmrong.community1.community.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.wenmrong.community1.community.constants.MQTopic;
import com.wenmrong.community1.community.mapper.QuestionMapper;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <h3>community</h3>
 * <p></p>
 *
 * @author : Autodidact
 * @date : 2022-09-07 21:18
 **/
@Component
@RocketMQMessageListener(topic = MQTopic.NOTIFICATION_TOPIC,consumerGroup = "notification_consumer")
@Slf4j
public class NotificationConsumer implements RocketMQReplyListener<Question,String> {
    @Resource
    NotificationService notificationService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Override
    public String onMessage(Question message) {
        log.error( "线程名称：{}" +"message {}",Thread.currentThread().getName(), JSONObject.toJSONString(message));
        return null;
    }
}
