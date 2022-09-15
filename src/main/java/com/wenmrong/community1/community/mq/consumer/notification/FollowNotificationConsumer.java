package com.wenmrong.community1.community.mq.consumer.notification;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wenmrong.community1.community.constants.MQGroup;
import com.wenmrong.community1.community.constants.MQTag;
import com.wenmrong.community1.community.constants.MQTopic;
import com.wenmrong.community1.community.mapper.QuestionMapper;
import com.wenmrong.community1.community.mapper.UserMapper;
import com.wenmrong.community1.community.model.Notification;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.service.NotificationService;
import com.wenmrong.community1.community.sysenum.SysEnum;
import com.wenmrong.community1.community.utils.CharacterUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
@Component
@RocketMQMessageListener(topic = MQTopic.NOTIFICATION_TOPIC, consumerGroup = MQGroup.FOLLOW_NOTIFICATION,selectorExpression = MQTag.FOLLOW)
@Slf4j
public class FollowNotificationConsumer  implements RocketMQReplyListener<Notification, String> {
    @Resource
    NotificationService notificationService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private UserMapper userMapper;
    @Override
    public String onMessage(Notification notification) {
        log.error("线程名称：{}" + "message {}", Thread.currentThread().getName(), JSONObject.toJSONString(notification));
        Long articleId = notification.getOuterid();
        Question question = questionMapper.selectOne(new QueryWrapper<Question>().eq("id", articleId));
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", question.getCreator()));
        notification.setNotifier(user.getId());
        notification.setReceiver(question.getCreator());
        notification.setOuterTitle(CharacterUtil.buildNotificationContent(user,question, SysEnum.Notification_Type.FOLLOW.getType()));
        notificationService.save(notification);
        return "success";
    }

}
