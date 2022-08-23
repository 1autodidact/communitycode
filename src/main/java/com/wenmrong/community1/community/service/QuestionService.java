package com.wenmrong.community1.community.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Sets;
import com.wenmrong.community1.community.cache.TagCache;
import com.wenmrong.community1.community.dto.PaginationDTO;
import com.wenmrong.community1.community.dto.QuestionDTO;
import com.wenmrong.community1.community.dto.QuestionQueryDTO;
import com.wenmrong.community1.community.dto.TagDTO;
import com.wenmrong.community1.community.enums.SortEnum;
import com.wenmrong.community1.community.exception.CustomizeErrorCode;
import com.wenmrong.community1.community.exception.CustomizeException;
import com.wenmrong.community1.community.mapper.*;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.model.QuestionExample;
import com.wenmrong.community1.community.model.User;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionService extends ServiceImpl<QuestionMapper, Question> {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Resource
    private LabelMapper labelMapper;
    @Autowired
    private QuestionService questionService;

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
            questionDTO.setUser(user);
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

    public PaginationDTO<QuestionDTO> list(Long userId, Integer page, Integer size) {
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);

        Integer totalCount = (int) questionMapper.countByExample(questionExample);


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
        Integer offset = size * (page - 1);
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
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

   public void create(QuestionDTO questionDTO) {
       Question question = new Question();
       BeanUtils.copyProperties(questionDTO,question);
       question.setCreator(Long.valueOf(questionDTO.getUserId()));
       question.setViewCount(0);
       question.setCommentCount(0);
       question.setLikeCount(0);
       question.setTag("language");
       question.setLabelIds(questionDTO.getLabelIds());
       questionMapper.insert(question);

   }

   public List<QuestionDTO> selectRelatedQuestion(Integer currentPage, Integer pageSize, String labelIds, String currentArticleId) {
       PageHelper.startPage(currentPage,pageSize,true);
       HashSet requestLabId = new HashSet(Arrays.asList(Optional.ofNullable(labelIds).orElse("").split(",")));

       List<Question> questions = questionMapper.selectList(null);

       if (StringUtils.isBlank(labelIds)) {
           return questions.stream().map(this::assembleQuestionInfo).collect(Collectors.toList());
       }

       List<QuestionDTO> relatedQuestions = questions.stream().filter(item -> {
           List<String> dbLabelIds = item.getLabelIds().stream().map(id -> String.valueOf(id)).collect(Collectors.toList());
           Sets.SetView intersection = Sets.intersection(new HashSet<>(dbLabelIds), requestLabId);
           return intersection.size() != 0;
       }).map(this::assembleQuestionInfo).collect(Collectors.toList());
       return relatedQuestions.stream().filter(item -> !item.getId().equals(Long.valueOf(currentArticleId))).collect(Collectors.toList());

   }


    private QuestionDTO assembleQuestionInfo(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }


}
