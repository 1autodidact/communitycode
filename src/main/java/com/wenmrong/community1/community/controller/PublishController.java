package com.wenmrong.community1.community.controller;

import com.wenmrong.community1.community.dto.QuestionDTO;
import com.wenmrong.community1.community.mapper.QuestionMapper;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionService questionService;




    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id")Long id, Model model){
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        return "publish";
    }
    @GetMapping("/publish")
    public String publish(){
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
            model.addAttribute("title",title);
            model.addAttribute("description",description);
            model.addAttribute("tag",tag);

            if (title == null || title ==""){
                model.addAttribute("error","title is empty");
                return "publish";
            }
            if (description == null || description ==""){
                model.addAttribute("error","description is empty");
                return "publish";
            }
            if (tag == null || tag ==""){
                model.addAttribute("error","tag is empty");
                return "publish";
            }
            User user =(User)request.getSession().getAttribute("user");


            if (user ==null){
                model.addAttribute("error","用户未登录");
                System.out.println("0000");
                return "publish";
            }
            Question question = new Question();
            question.setTitle(title);
            question.setDescription(description);
            question.setTag(tag);
            question.setCreator(user.getId());
            question.setId(id);
            questionService.createOrUpdate(question);
            return "redirect:/";
    }


}

