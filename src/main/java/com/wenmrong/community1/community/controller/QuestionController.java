package com.wenmrong.community1.community.controller;

import com.github.pagehelper.PageInfo;
import com.wenmrong.community1.community.dto.*;
import com.wenmrong.community1.community.enums.CommentTypeEnum;
import com.wenmrong.community1.community.model.Label;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionHistoryService questionHistoryService;
    @Autowired
    private StatisticService statisticService;

    @Autowired
    private LabelService labelService;


    @GetMapping("/getQuestions")
    @ResponseBody
    public ResultDTO getQuestions(
                                  @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                  @RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize,
                                  @RequestParam(required = false) String labelIds,
                                    @RequestParam(required = false) String currentArticleId,
                                  @RequestParam(required = false) String createUser) {

        List<QuestionDTO> questionDTOS = questionService.selectRelatedQuestion(currentPage, pageSize, labelIds, currentArticleId, createUser);
        return ResultDTO.okOf(new PageInfo<>(questionDTOS));

    }


    @GetMapping("/question/getById")
    @ResponseBody
    public ResultDTO question(@RequestParam Long id,boolean isPv){
        QuestionDTO questionDTO = questionService.getById(id);
        return ResultDTO.okOf(questionDTO);
    }


    @GetMapping("/question/getCountById")
    @ResponseBody
    public ResultDTO getArticleCommentVisitTotal() {
        StatisticData statistic = statisticService.statistic();
        return ResultDTO.okOf(statistic);
    }

    @GetMapping("/getArticleCommentVisitTotal")
    @ResponseBody
    public ResultDTO getArticleCommentVisitCount() {
        StatisticData statistic = statisticService.statistic();
        return ResultDTO.okOf(statistic);
    }

    @GetMapping("/label/getList")
    @ResponseBody
    public ResultDTO getLabels() {
        List<Label> labels = labelService.getLabels();
        return ResultDTO.okOf(labels);
    }



    @PostMapping("/question/create")
    @ResponseBody
    public ResultDTO create( QuestionDTO questionDTO) {
        questionService.create(questionDTO);
        return ResultDTO.okOf();
    }
    @GetMapping("/article/getLikesArticle")
    @ResponseBody
    public ResultDTO getLikesArticle(@RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                     @RequestParam(name = "pageSize", defaultValue = "7") Integer pageSize,
                                     @RequestParam String likeUser) {
        List<QuestionDTO> likesArticle = questionService.getLikesArticle(likeUser);
        return ResultDTO.okOf(likesArticle);

    }
}
