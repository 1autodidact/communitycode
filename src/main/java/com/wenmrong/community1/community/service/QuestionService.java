package com.wenmrong.community1.community.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Sets;
import com.wenmrong.community1.community.cache.TagCache;
import com.wenmrong.community1.community.constants.MQTopic;
import com.wenmrong.community1.community.dto.*;
import com.wenmrong.community1.community.enums.SortEnum;
import com.wenmrong.community1.community.exception.CustomizeErrorCode;
import com.wenmrong.community1.community.exception.CustomizeException;
import com.wenmrong.community1.community.mapper.*;
import com.wenmrong.community1.community.model.*;
import com.wenmrong.community1.community.sysenum.SysEnum;
import com.wenmrong.community1.community.utils.UserInfoProfile;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQLocalRequestCallback;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.jetbrains.annotations.NotNull;
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

    public PaginationDTO<QuestionDTO> list(String search, String tag, Integer page, Integer size, String sort) {
        if (StringUtils.isNotBlank(search)) {
            String[] tags = search.split(",");
            search = Arrays.stream(tags).collect(Collectors.joining("|"));
        }


        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<QuestionDTO>();
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        questionQueryDTO.setTag(tag);

        for (SortEnum sortEnum : SortEnum.values()) {
            if (sortEnum.name().toLowerCase().equals(sort)) {
                questionQueryDTO.setSort(sort);

                if (sortEnum == SortEnum.HOT7) {
                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 7);
                }
                if (sortEnum == SortEnum.HOT30) {
                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30);
                }
                break;
            }
        }
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);
        Integer totalPage;

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage, page);
        Integer offset = page < 1 ? 0 : size * (page - 1);
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        questionQueryDTO.setPage(offset);
        questionQueryDTO.setSize(size);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            questionDTO.setUser(userDto);
            TagDTO tagDTO = this.randomGetTag();
            questionDTO.setTagDTO(tagDTO);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    private TagDTO randomGetTag() {
        List<TagDTO> tagDTOS = TagCache.get();
        boolean nextBoolean = RandomUtils.nextBoolean();
        if (nextBoolean) {
            if (RandomUtils.nextBoolean()) {
                return tagDTOS.get(3);
            }
            return tagDTOS.get(1);
        } else {
            return tagDTOS.get(2);
        }
    }


    public QuestionDTO getById(Long id) {
        //先进行计数,如果question已经查询完毕,再计数无法及时刷新数据
        questionService.incView(id);
        Question question = questionMapper.selectById(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        return assembleQuestionInfo(question);
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        } else {
            //更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);


    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isNotBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = queryDTO.getTag().split(",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());


        return questionDTOS;
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
        notification.setOuterTitle(this.buildNotificationContent(userProfile, question));
        notification.setNotifier(userProfile.getId());
        rocketMQTemplate.sendAndReceive(MQTopic.NOTIFICATION_TOPIC, notification, new RocketMQLocalRequestCallback<String>() {

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

    @NotNull
    private String buildNotificationContent(User userProfile, Question question) {
        return String.format("%s发布了%s文章",userProfile.getName(),question.getTitle());
    }

    public List<QuestionDTO> selectRelatedQuestion(Integer currentPage, Integer pageSize, String labelIds, String currentArticleId, String createUser) {
        User user = UserInfoProfile.getUserProfile();
        PageHelper.startPage(currentPage, pageSize, true);
        HashSet requestLabId = new HashSet(Arrays.asList(Optional.ofNullable(labelIds).orElse("").split(",")));
        QueryWrapper condition = createUser == null ? null : new QueryWrapper<Question>().eq("creator", createUser);


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
