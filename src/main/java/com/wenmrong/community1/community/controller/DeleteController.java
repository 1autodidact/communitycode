package com.wenmrong.community1.community.controller;

import com.wenmrong.community1.community.dto.PaginationDTO;
import com.wenmrong.community1.community.mapper.QuestionMapper;
import com.wenmrong.community1.community.mapper.UserMapper;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.model.QuestionExample;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Controller
public class DeleteController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/profile/questions/delete/{id}")
    public String delete(@PathVariable(name = "id") Long id,
                         HttpServletRequest request,
                         Model model,
                         @RequestParam(name = "page", defaultValue = "1") Integer page,
                         @RequestParam(name = "size", defaultValue = "7") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        boolean flag = false;
        model.addAttribute("section", "questions");
        model.addAttribute("sectionName", "My Questions");
        questionMapper.deleteByPrimaryKey(id);
        PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
        model.addAttribute("pagination", paginationDTO);
        String userStars = user.getStar();
        List<String> starList = Arrays.asList(userStars.substring(0, userStars.length() - 1).split("="));
        //避免unsupportedException
        List<String> starListByArrayList = new ArrayList(starList);
        //避免java.util.ConcurrentModificationException
        Iterator<String> starIterator = starListByArrayList.iterator();
        List<Question> questions = questionMapper.selectByExample(new QuestionExample());
        //避免因为已经删除的问题出现在收藏文章中
        while (starIterator.hasNext()) {
            String next = starIterator.next();
            for (Question question : questions) {
                if (question.getId().equals(Long.parseLong(next))) {
                    flag = true;
                }
            }
            if (!flag) {

                starIterator.remove();

            }
            flag = false;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String s : starListByArrayList) {
            stringBuilder.append(s+"=");
        }
        user.setStar(stringBuilder.toString());
        userMapper.updateByPrimaryKey(user);
        return "profile";
    }

}

