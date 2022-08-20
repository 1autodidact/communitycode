package com.wenmrong.community1.community.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wenmrong.community1.community.cache.HotTagCache;
import com.wenmrong.community1.community.dto.PaginationDTO;
import com.wenmrong.community1.community.dto.ResultDTO;
import com.wenmrong.community1.community.dto.StatisticData;
import com.wenmrong.community1.community.mapper.CommentMapper;
import com.wenmrong.community1.community.mapper.QuestionMapper;
import com.wenmrong.community1.community.mapper.UserMapper;
import com.wenmrong.community1.community.service.QuestionService;
import com.wenmrong.community1.community.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private HotTagCache hotTagCache;
    @Autowired
    StatisticService statisticService;
    @Resource
    UserMapper userMapper;
    @Resource
    QuestionMapper questionMapper;
    @Resource
    CommentMapper commentMapper;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "7") Integer size,
                        @RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "tag", required = false) String tag,
                        @RequestParam(name = "sort", required = false) String sort) {

        PaginationDTO pagination = questionService.list(search, tag, page, size, sort);
        List<String> tags = hotTagCache.getHots();
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        model.addAttribute("tags", tags);
        model.addAttribute("tag", tag);
        model.addAttribute("sort", sort);
        return "index";
    }
    @GetMapping("/getArticleCommentVisitTotal")
    @ResponseBody
    public ResultDTO getArticleCommentVisitTotal() {
        StatisticData statistic = statisticService.statistic();
        return ResultDTO.okOf(statistic);
    }
}