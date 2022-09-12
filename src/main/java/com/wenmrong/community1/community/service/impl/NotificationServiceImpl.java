package com.wenmrong.community1.community.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wenmrong.community1.community.mapper.UserFollowMapper;
import com.wenmrong.community1.community.mapper.UserMapper;
import com.wenmrong.community1.community.model.Notification;
import com.wenmrong.community1.community.mapper.NotificationMapper;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.model.UserFollow;
import com.wenmrong.community1.community.service.NotificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wenmrong.community1.community.sysenum.SysEnum;
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
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {
    @Autowired
    RedisTemplate redisTemplate;
    @Resource
    UserFollowMapper userFollowMapper;
    @Resource
    UserMapper userMapper;
    private static final String PUBLISH_NOTIFICATION = "autodidact:notification:publish";

    private static final String FOLLOW_NOTIFICATION = "autodidact:notification:publish";

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
            redisTemplate.opsForHash().put(this.buildReceivingQuestionKey(id), this.buildStorageKey(publisher,savedNotification.getOuterid()),JSONObject.toJSONString(savedNotification));
        });
    }

    @Override
    public void follow(Notification notification) {

        redisTemplate.opsForHash().put(this.buildFollowKey(notification.getReceiver()), this.buildStorageKey(notification.getReceiver(),notification.getOuterid()),JSONObject.toJSONString(notification));

    }

    @NotNull
    private String buildReceivingQuestionKey(Long id) {
       return String.format("%s:%s", PUBLISH_NOTIFICATION, id);
    }

    @NotNull
    private String buildFollowKey(Long id) {
        return String.format("%s:%s", FOLLOW_NOTIFICATION, id);
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

}
