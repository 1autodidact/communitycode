package com.wenmrong.community1.community.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wenmrong.community1.community.dto.CommentDTO;
import com.wenmrong.community1.community.dto.UserDto;
import com.wenmrong.community1.community.enums.CommentTypeEnum;
import com.wenmrong.community1.community.enums.NotificationStatusEnum;
import com.wenmrong.community1.community.enums.NotificationTypeEnum;
import com.wenmrong.community1.community.exception.CustomizeErrorCode;
import com.wenmrong.community1.community.exception.CustomizeException;
import com.wenmrong.community1.community.mapper.*;
import com.wenmrong.community1.community.model.*;
import com.wenmrong.community1.community.sysenum.SysEnum;
import com.wenmrong.community1.community.utils.UserInfoProfile;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService  extends ServiceImpl<CommentMapper, Comment> {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Resource
    private UserLevelMapper userLevelMapper;
    @Autowired
    private NotificationService notificationService;
    @Transactional
    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);

        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbComment = commentMapper.selectById(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            //回复问题
            Question question = questionMapper.selectById(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            //调用了commentExtMapper.xml文件里面调用了commentExtMapper接口,进而可以把参数传到commentExtMapper.xml
            commentExtMapper.incCommentCount(parentComment);
            //创建通知
            createNotification(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());


        } else {
            //回复问题
            Question question = questionMapper.selectById(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //INTEGER类型模型默认初始化的时候为null,尽管数据库default=0
            comment.setCommentCount(0);
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            createNotification(comment, question.getCreator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
    }

    public void createNotification(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        if (receiver == comment.getCommentator() && (notificationType.getType() == 1 || notificationType.getType() == 2)) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        // 1 为评论问题类型
        List<Comment> comments = commentMapper.selectList(new QueryWrapper<Comment>().eq("question_id", id));
        List<Long> userIds = comments.stream().map(Comment::getCommentator).collect(Collectors.toList());
        List<User> users = userMapper.selectList(new QueryWrapper<User>().in("id", userIds));
        List<UserLevel> relatedUserLevel = userLevelMapper.selectList(new QueryWrapper<UserLevel>().in("user_id", userIds));

        List<CommentDTO> commentDTOS = comments.stream().map(item -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(item, commentDTO);
            Optional<User> matchedUser = users.stream().filter(userInfo -> item.getCommentator().equals(userInfo.getId())).findFirst();
            if (matchedUser.isPresent()) {
                Optional<UserLevel> level = relatedUserLevel.stream().filter(info -> info.getUserId().equals(matchedUser.get().getId())).findFirst();
                UserLevel userLevel = level.get();
                UserDto userDto = new UserDto();
                userDto.setUserLevel(userLevel);
                commentDTO.setUser(userDto);
            }
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;


    }


    @Transactional
    public void create(Comment comment) {
        User user = UserInfoProfile.getUserProfile();
        comment.setParentId(comment.getQuestionId());
        comment.setQuestionId(comment.getQuestionId());
        comment.setType(SysEnum.CommentType.COMMENT.getType());
        comment.setCommentator(user.getId());
        commentMapper.insert(comment);
        Notification notification = new Notification();
        notification.setOuterid(comment.getId());
        notification.setNotifier(comment.getCommentator());
        notificationService.sendCommonNotification(comment.getId(), notification, user);
    }
}
