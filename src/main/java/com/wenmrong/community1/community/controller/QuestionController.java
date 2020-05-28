package com.wenmrong.community1.community.controller;

import com.wenmrong.community1.community.dto.CommentDTO;
import com.wenmrong.community1.community.dto.QuestionDTO;
import com.wenmrong.community1.community.enums.CommentTypeEnum;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.service.CommentService;
import com.wenmrong.community1.community.service.QuestionHistoryService;
import com.wenmrong.community1.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model,
                           HttpServletRequest request, HttpServletResponse response) {
        User user = (User)request.getSession().getAttribute("user");
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        String history = questionHistoryService.createHistory(id.toString(),request);
        ArrayList<Question> questionHistory = questionHistoryService.showHistory(history);
        Cookie cookie = new Cookie("history",history);
        cookie.setMaxAge(24*60*60);
        cookie.setPath("/");
        response.addCookie(cookie);
        if (user != null) {
            String star = user.getStar();
            if (star != null && !star.equals("")) {
                List<String> starList = Arrays.asList(star.substring(0, star.length() - 1).split("="));
                if (starList.contains(Long.toString(id))) {
                    model.addAttribute("starFlag",true);
                }
            }
        }

        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        model.addAttribute("questionHistory", questionHistory);
        return "question";
    }

}
