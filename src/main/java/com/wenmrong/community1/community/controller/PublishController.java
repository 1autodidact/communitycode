package com.wenmrong.community1.community.controller;

import com.wenmrong.community1.community.cache.TagCache;
import com.wenmrong.community1.community.dto.QuestionDTO;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.service.QuestionService;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionService questionService;


    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Long id, Model model) {
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tags", TagCache.get());
        return "publish";

    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title") String title,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "tag") String tag,
            @RequestParam(value = "id") Long id,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", TagCache.get());
        if (title == null || title == "") {
            model.addAttribute("error", "title is empty");
            return "publish";
        }
        if (description == null || description == "") {
            model.addAttribute("error", "description is empty");
            return "publish";
        }
        if (tag == null || tag == "") {
            model.addAttribute("error", "tag is empty");
            return "publish";
        }
        String invalid = TagCache.filterInvalid(tag);
        if (!StringUtils.isNullOrEmpty(invalid)) {
            model.addAttribute("error", "tag is unreasonable" + invalid);
            return "publish";
        }
        User user = (User) request.getSession().getAttribute("user");
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        //通过input的hidden隐藏了获取id的值 表单提交获取id值判断是编辑还是新键
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }


}

