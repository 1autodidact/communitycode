package com.wenmrong.community1.community.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Sets;
import com.wenmrong.community1.community.constants.MQTag;
import com.wenmrong.community1.community.constants.MQTopic;
import com.wenmrong.community1.community.dto.*;
import com.wenmrong.community1.community.exception.CustomizeErrorCode;
import com.wenmrong.community1.community.exception.CustomizeException;
import com.wenmrong.community1.community.mapper.*;
import com.wenmrong.community1.community.model.*;
import com.wenmrong.community1.community.sysenum.SysEnum;
import com.wenmrong.community1.community.utils.CharacterUtil;
import com.wenmrong.community1.community.utils.UserInfoProfile;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQLocalRequestCallback;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionService extends ServiceImpl<QuestionMapper, Question> {
    @Resource
    private UserMapper userMapper;
    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private QuestionExtMapper questionExtMapper;
    @Resource
    private LabelMapper labelMapper;
    @Autowired
    private QuestionService questionService;
    @Resource
    private UserLikeMapper userlikeMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private UserLevelMapper userLevelMapper;
    @Autowired
    private UserService userService;
    @Resource
    private UserFollowMapper userFollowMapper;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Autowired
    private NotificationService notificationService;



    public QuestionDTO getById(Long id) {
        //先进行计数,如果question已经查询完毕,再计数无法及时刷新数据
        questionService.incView(id);
        Question question = questionMapper.selectById(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        return assembleQuestionInfo(question);
    }



    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);


    }



    @Transactional
    public void create(QuestionDTO questionDTO) {
        User userProfile = UserInfoProfile.getUserProfile();
        Question question = new Question();
        BeanUtils.copyProperties(questionDTO, question);
        question.setCreator(Long.valueOf(questionDTO.getUserId()));
        question.setViewCount(0);
        question.setCommentCount(0);
        question.setLikeCount(0);
        question.setTag("language");
        question.setLabelIds(questionDTO.getLabelIds());
        questionMapper.insert(question);

        Notification notification = new Notification();
        notification.setType(SysEnum.Notification_Type.PUBLISH.getType());
        notification.setOuterid(question.getId());
        notification.setNotifierName(userProfile.getName());
        notification.setOuterTitle(CharacterUtil.buildNotificationContent(userProfile, question, SysEnum.Notification_Type.PUBLISH.getType()));
        notification.setNotifier(userProfile.getId());
        rocketMQTemplate.sendAndReceive(CharacterUtil.buildNotificationDestination(MQTopic.NOTIFICATION_TOPIC, MQTag.PUBLISH), notification, new RocketMQLocalRequestCallback<String>() {

            @Override
            public void onSuccess(String message) {
                log.error("消息发送成功" + message);
            }

            @Override
            public void onException(Throwable e) {
                log.error("消息发送失败", e);
            }
        });

    }


    public List<QuestionDTO> selectRelatedQuestion(Integer currentPage, Integer pageSize, String labelIds, String currentArticleId, String createUser, String title) {
        User user = UserInfoProfile.getUserProfile();
        PageHelper.startPage(currentPage, pageSize, true);
        HashSet requestLabId = new HashSet(Arrays.asList(Optional.ofNullable(labelIds).orElse("").split(",")));
        QueryWrapper condition = createUser == null ? new QueryWrapper<Question>().orderByDesc("gmt_create") : new QueryWrapper<Question>().eq("creator", createUser).orderByDesc("gmt_create");
        if (StringUtils.isNotBlank(title)) {
            condition = (QueryWrapper) condition.eq("title",title);
        }

        List<Question> questions = questionMapper.selectList(condition);
        List<Long> articleIds = questions.stream().map(Question::getId).collect(Collectors.toList());
        if (articleIds.size() == 0) {
            return new ArrayList<>();
        }
        List<UserLike> userLikeRecord = userlikeMapper.selectList(new QueryWrapper<UserLike>().in("article_id", articleIds));

        List<Comment> relatedComments = commentMapper.selectList(new QueryWrapper<Comment>().in("question_id", articleIds));

        if (StringUtils.isBlank(labelIds)) {
            return questions.stream().map(item -> {
                QuestionDTO questionDTO = this.assembleQuestionInfo(item);
                List<UserLike> relatedArticle = userLikeRecord.stream().filter(record -> record.getArticleId().equals(item.getId())).collect(Collectors.toList());

                List<Comment> relatedCommentRecord = relatedComments.stream().filter(comment -> comment.getQuestionId().equals(item.getId())).collect(Collectors.toList());
                if (user != null) {
                    boolean isLike = relatedArticle.stream().anyMatch(article -> article.getLikeUser().equals(user.getId()));
                    questionDTO.setLike(isLike);
                }

                questionDTO.setLikeCount(relatedArticle.size());
                questionDTO.setCommentCount(relatedCommentRecord.size());
                return questionDTO;

            }).collect(Collectors.toList());
        }

        List<QuestionDTO> relatedQuestions = questions.stream().filter(item -> {
            List<String> dbLabelIds = item.getLabelIds().stream().map(id -> String.valueOf(id)).collect(Collectors.toList());
            Sets.SetView intersection = Sets.intersection(new HashSet<>(dbLabelIds), requestLabId);
            return intersection.size() != 0;
        }).map(this::assembleQuestionInfo).collect(Collectors.toList());
        return relatedQuestions.stream().filter(item -> !item.getId().equals(Long.valueOf(currentArticleId))).collect(Collectors.toList());

    }


    private QuestionDTO assembleQuestionInfo(Question question) {
        User currentUser = UserInfoProfile.getUserProfile();
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        UserDto userDto = userService.buildUserLevelInfo(user);
        questionDTO.setUser(userDto);
        if (currentUser != null) {
            UserFollow userFollow = userFollowMapper.selectOne(new QueryWrapper<UserFollow>().eq("user_id", user.getId()).eq("follow_id", currentUser.getId()));
            if (userFollow != null) {
                questionDTO.setIsFollow(true);
            }
        }
        return questionDTO;
    }


    public List<QuestionDTO> getLikesArticle(String likeUser) {
        List<UserLike> like_user = userlikeMapper.selectList(new QueryWrapper<UserLike>().eq("like_user", likeUser));
        List<Long> likeArticleIds = like_user.stream().map(UserLike::getArticleId).collect(Collectors.toList());
        if (likeArticleIds.size() == 0) {
            return new ArrayList<QuestionDTO>();
        }
        List<Question> questions = questionMapper.selectList(new QueryWrapper<Question>().in("id", likeArticleIds));
        List<QuestionDTO> questDtos = questions.stream().map(this::assembleQuestionInfo).collect(Collectors.toList());
        return questDtos;
    }
}
