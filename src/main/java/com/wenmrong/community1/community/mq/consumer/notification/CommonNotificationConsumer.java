package com.wenmrong.community1.community.mq.consumer.notification;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wenmrong.community1.community.constants.MQGroup;
import com.wenmrong.community1.community.constants.MQTag;
import com.wenmrong.community1.community.constants.MQTopic;
import com.wenmrong.community1.community.mapper.CommentMapper;
import com.wenmrong.community1.community.mapper.QuestionMapper;
import com.wenmrong.community1.community.mapper.UserMapper;
import com.wenmrong.community1.community.model.Comment;
import com.wenmrong.community1.community.model.Notification;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.service.NotificationService;
import com.wenmrong.community1.community.sysenum.SysEnum;
import com.wenmrong.community1.community.utils.CharacterUtil;
import com.wenmrong.community1.community.utils.UserInfoProfile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
@Component
@RocketMQMessageListener(topic = MQTopic.NOTIFICATION_TOPIC, consumerGroup = MQGroup.COMMON_NOTIFICATION,selectorExpression = MQTag.COMMON)
@Slf4j
public class CommonNotificationConsumer implements RocketMQReplyListener<Notification, String> {
    @Resource
    NotificationService notificationService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CommentMapper commentMapper;
    @Override
    public String onMessage(Notification notification) {
        log.error("线程名称：{}" + "message {}", Thread.currentThread().getName(), JSONObject.toJSONString(notification));
        Long articleId = notification.getOuterid();
        this.buildNotification(notification, notification.getType());
        notificationService.save(notification);
        return "success";
    }

    @NotNull
    private void buildNotification(Notification notification, Integer type) {
        if (type == null) {
            type = 1;
            notification.setType(1);
        }
        Long notifierId = notification.getNotifier();
        User notifierUser = userMapper.selectById(notifierId);
        if (type.equals(SysEnum.Notification_Type.UN_LIKE.getType()) || type.equals(SysEnum.Notification_Type.LIKE.getType())) {
            if (notification.getOuterid() == null) {
                return;
            }
            Question question = questionMapper.selectOne(new QueryWrapper<Question>().eq("id", notification.getOuterid()));
            User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", question.getCreator()));
            notification.setNotifier(user.getId());
            notification.setOuterTitle(CharacterUtil.buildNotificationContent(user,question, notification.getType()));
            Long creator = question.getCreator();
            notification.setReceiver(creator);
        }

        if (type.equals(SysEnum.Notification_Type.FOLLOW.getType()) || type.equals(SysEnum.Notification_Type.UN_FOLLOW.getType())) {
            String desc = CharacterUtil.buildDescription(type);
            User user = userMapper.selectOne(new QueryWrapper<User>().eq("id", notification.getReceiver()));
            notification.setOuterTitle(notifierUser.getName() + desc + user.getName());
        }

        if (type.equals(SysEnum.Notification_Type.COMMENT.getType())) {
            Long commentId = notification.getOuterid();
            Comment comment = commentMapper.selectOne(new QueryWrapper<Comment>().eq("id", commentId));
            Long questionId = comment.getQuestionId();
            Question question = questionMapper.selectOne(new QueryWrapper<Question>().eq("id", questionId));
            notification.setReceiver(question.getId());
        }
    }

}
