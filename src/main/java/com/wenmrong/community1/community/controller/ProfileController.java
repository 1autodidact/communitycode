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

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request, @PathVariable(name = "action") String action, Model model,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "7") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "My Questions");
            PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination", paginationDTO);
        } else if ("replies".equals(action)) {
            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
            model.addAttribute("section", "replies");
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName", "Newly Replies");

        } else if ("star".equals(action)) {
            PaginationDTO paginationDTO = starService.list(user.getId(), page, size);
//            List list = paginationDTO.getData();
//            List<User> users = new ArrayList<>();
//            for (Object question : list) {
//                QuestionExample example = new QuestionExample();
//
//                Question oneQuestion = (Question) question;
//                example.createCriteria()
//                        .andIdEqualTo(oneQuestion.getId());
//                List<Question> questions = questionMapper.selectByExample(example);
//                users.add(userMapper.selectByPrimaryKey(questions.get(0).getCreator()));
//            }
//            model.addAttribute("authors",users);
            model.addAttribute("section", "star");
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName", "My Star");
        }

        return "profile";
    }
}
