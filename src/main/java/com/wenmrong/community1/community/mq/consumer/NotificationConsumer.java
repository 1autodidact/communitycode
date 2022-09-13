package com.wenmrong.community1.community.mq.consumer;

import com.alibaba.fastjson.JSONObject;
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
@RocketMQMessageListener(topic = MQTopic.NOTIFICATION_TOPIC, consumerGroup = "notification_consumer")
@Slf4j
public class NotificationConsumer implements RocketMQReplyListener<Notification, String> {
    @Resource
    NotificationService notificationService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public String onMessage(Notification notification) {
        log.error("线程名称：{}" + "message {}", Thread.currentThread().getName(), JSONObject.toJSONString(notification));
        Optional<SysEnum.Notification_Type> matchedNotification = Arrays.stream(SysEnum.Notification_Type.values()).filter(item -> item.getType().equals(notification.getType())).findFirst();
        SysEnum.Notification_Type notification_type = matchedNotification.get();
        switch (notification_type) {
            case COMMENT:
                break;
            case FOLLOW:
                notificationService.follow(notification);
                break;
            case PUBLISH:
                notificationService.publish(notification);
                break;

            default:
                //语句
        }
        return "success";
    }



//    @Override
//    public String onMessage(Long message) {
//        return "success";
//    }
}
