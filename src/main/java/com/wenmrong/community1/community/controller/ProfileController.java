package com.wenmrong.community1.community.controller;

import com.wenmrong.community1.community.dto.PaginationDTO;
import com.wenmrong.community1.community.mapper.QuestionMapper;
import com.wenmrong.community1.community.mapper.UserMapper;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.model.QuestionExample;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.service.NotificationService;
import com.wenmrong.community1.community.service.QuestionService;
import com.wenmrong.community1.community.service.StarService;
import com.wenmrong.community1.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private StarService starService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;


}
