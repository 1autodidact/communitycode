package com.wenmrong.community1.community.controller;

import com.github.pagehelper.PageInfo;
import com.wenmrong.community1.community.dto.*;
import com.wenmrong.community1.community.model.Label;
import com.wenmrong.community1.community.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
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
            @RequestParam(required = false) String createUser,
            @RequestParam(required = false) String title) {

        List<QuestionDTO> questionDTOS = questionService.selectRelatedQuestion(currentPage, pageSize, labelIds, currentArticleId, createUser, title);
        return ResultDTO.okOf(new PageInfo<>(questionDTOS));

    }


    @GetMapping("/question/getById")
    @ResponseBody
    public ResultDTO question(@RequestParam Long id, boolean isPv) {
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
    public ResultDTO create(QuestionDTO questionDTO,@RequestParam("file") MultipartFile file) throws IOException {
        questionService.create(questionDTO, file);
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

    @PostMapping("/article/update")
    @ResponseBody
    public ResultDTO update( QuestionDTO questionDTO,@RequestParam("file") MultipartFile file) throws IOException {
        questionService.edit(questionDTO, file);
        return ResultDTO.okOf();
    }

    @PostMapping("/question/uploadPicture")
    @ResponseBody
    public ResultDTO uploadPicture(@RequestParam("picture") MultipartFile picture, HttpServletRequest httpServletRequest) throws IOException, URISyntaxException {
        String url = questionService.picture(picture);
        return ResultDTO.okOf(url);
    }

}
