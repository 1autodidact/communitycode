package com.wenmrong.community1.community.mq.consumer.notification;

import com.alibaba.fastjson.JSONObject;
import com.wenmrong.community1.community.constants.MQGroup;
import com.wenmrong.community1.community.constants.MQTag;
import com.wenmrong.community1.community.constants.MQTopic;
import com.wenmrong.community1.community.model.Notification;
import com.wenmrong.community1.community.service.NotificationService;
import com.wenmrong.community1.community.sysenum.SysEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Optional;

/**
 * <h3>community</h3>
 * <p></p>
 *
 * @author : Autodidact
 * @date : 2022-09-07 21:18
 **/
@Component
@RocketMQMessageListener(topic = MQTopic.NOTIFICATION_TOPIC, consumerGroup = MQGroup.PUBLISH_NOTIFICATION, selectorExpression = MQTag.PUBLISH)
@Slf4j
public class PublishNotificationConsumer implements RocketMQReplyListener<Notification, String> {
    @Resource
    NotificationService notificationService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public String onMessage(Notification notification) {
        log.error("线程名称：{}" + "message {}", Thread.currentThread().getName(), JSONObject.toJSONString(notification));
        notificationService.publish(notification);
        //语句
        return "success";
    }


}
