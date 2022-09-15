package com.wenmrong.community1.community.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.regexp.internal.RE;
import com.wenmrong.community1.community.constants.MQTopic;
import com.wenmrong.community1.community.mapper.NotificationMapper;
import com.wenmrong.community1.community.model.Notification;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <h3>community</h3>
 * <p></p>
 *
 * @author : Autodidact
 * @date : 2022-09-10 09:33
 **/
@Component
@RocketMQMessageListener(topic = MQTopic.SAVED_NOTIFICATION_TOPIC,consumerGroup = "save_notification")
@Slf4j
public class SaveNotificationConsumer implements RocketMQReplyListener<String, Boolean> {
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private NotificationService notificationService;
    @Override
    public Boolean onMessage(String hashKey) {
        Map<String,String> entries = redisTemplate.opsForHash().entries(hashKey);
        log.error("entries ",JSONObject.toJSONString(entries));
        List<Notification> publishNotification = entries.entrySet().stream()
                .map(item -> JSONObject.parseObject(item.getValue(),Notification.class))
                .collect(Collectors.toList());
        return notificationService.saveBatch(publishNotification);
    }
}