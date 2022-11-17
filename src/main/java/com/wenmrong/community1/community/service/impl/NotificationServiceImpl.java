package com.wenmrong.community1.community.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wenmrong.community1.community.constants.MQTag;
import com.wenmrong.community1.community.constants.MQTopic;
import com.wenmrong.community1.community.mapper.UserFollowMapper;
import com.wenmrong.community1.community.mapper.UserMapper;
import com.wenmrong.community1.community.model.Notification;
import com.wenmrong.community1.community.mapper.NotificationMapper;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.model.UserFollow;
import com.wenmrong.community1.community.service.NotificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wenmrong.community1.community.sysenum.SysEnum;
import com.wenmrong.community1.community.utils.CharacterUtil;
import com.wenmrong.community1.community.utils.UserInfoProfile;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQLocalRequestCallback;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author autodidact
 * @since 2022-09-09
 */
@Service
@Slf4j
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {
    @Autowired
    RedisTemplate redisTemplate;
    @Resource
    UserFollowMapper userFollowMapper;
    @Resource
    NotificationMapper notificationMapper;
    @Autowired
    RocketMQTemplate rocketMQTemplate;
    @Resource
    UserMapper userMapper;
    private static final String PUBLISH_NOTIFICATION = "autodidact:notification:publish";

    private static final String FOLLOW_NOTIFICATION = "autodidact:notification:follow";

    @Override
    public void publish(Notification notification) {
        Long publisher = notification.getNotifier();
        User UserInfoForPublisher = userMapper.selectOne(new QueryWrapper<User>().eq("id", publisher));
        List<UserFollow> followUsers = userFollowMapper.selectList(new QueryWrapper<UserFollow>().eq("user_id", publisher));
        List<Long> followUserIds = followUsers.stream().map(UserFollow::getFollowId).collect(Collectors.toList());
        followUserIds.forEach(id -> {
            Notification savedNotification = new Notification();
            BeanUtils.copyProperties(notification,savedNotification);
            savedNotification.setReceiver(id);
            savedNotification.setType(SysEnum.Notification_Type.PUBLISH.getType());
            redisTemplate.opsForHash().put(this.buildReceivingQuestionKey(savedNotification.getOuterid()), this.buildStorageKey(publisher,id),JSONObject.toJSONString(savedNotification));
        });

        rocketMQTemplate.sendAndReceive(MQTopic.SAVED_NOTIFICATION_TOPIC, this.buildReceivingQuestionKey(notification.getOuterid()), new RocketMQLocalRequestCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean message) {
                log.error("消息发送成功" + message);
            }
            @Override
            public void onException(Throwable e) {
                log.error("消息发送失败", e);
            }
        });
    }

    @Override
    public void follow(Notification notification) {

    }


    @Override
    public List<Notification> getList(Integer currentPage, Integer pageSize, String type, String isRead) {
        User userProfile = UserInfoProfile.getUserProfile();
        QueryWrapper<Notification> wrapper = new QueryWrapper<Notification>().eq("receiver", userProfile.getId());
        List<Notification> notifications = notificationMapper.selectList(wrapper);
        return notifications;
    }

    @NotNull
    private String buildReceivingQuestionKey(Long id) {
       return String.format("%s_%s", PUBLISH_NOTIFICATION, id);
    }

    @NotNull
    private String buildFollowKey(Long id) {
        return String.format("%s_%s", FOLLOW_NOTIFICATION, id);
    }



    /**
     *
     * @param publisherId
     * @param outerId 文章id/评论id
     * @return
     */
    @NotNull
    private String buildStorageKey(Long publisherId, Long outerId) {
        return String.format("%s:%s", publisherId,outerId);
    }

    public void sendCommonNotification(Long outerId, Notification notification, User user) {
        notification.setNotifier(user.getId());
        notification.setNotifierName(user.getName());
        notification.setOuterid(outerId);
        rocketMQTemplate.sendAndReceive(CharacterUtil.buildNotificationDestination(MQTopic.NOTIFICATION_TOPIC, MQTag.COMMON), notification, new RocketMQLocalRequestCallback<String>() {

            @Override
            public void onSuccess(String message) {
                log.error("消息发送成功{} topic{}", message, MQTopic.NOTIFICATION_TOPIC);
            }

            @Override
            public void onException(Throwable e) {
                log.error("{}消息发送失败", MQTopic.NOTIFICATION_TOPIC, e);
            }
        });
    }

}
