package com.wenmrong.community1.community.controller;

import com.wenmrong.community1.community.cache.TagCache;
import com.wenmrong.community1.community.dto.QuestionDTO;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.GenericMessage;
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
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

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
        if (StringUtils.isBlank(title)) {
            model.addAttribute("error", "title is empty");
            return "publish";
        }
        if (StringUtils.isBlank(description)) {
            model.addAttribute("error", "description is empty");
            return "publish";
        }
        if (StringUtils.isBlank(tag)) {
            model.addAttribute("error", "tag is empty");
            return "publish";
        }
        String invalid = TagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalid)) {
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
        final GenericMessage<Question> questionGenericMessage = new GenericMessage<>(question);

        rocketMQTemplate.asyncSend("question_topic", questionGenericMessage, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("send Success");
            }

            @Override
            public void onException(Throwable e) {
                System.out.println("send error");

            }
        });
        return "redirect:/";
    }


}

