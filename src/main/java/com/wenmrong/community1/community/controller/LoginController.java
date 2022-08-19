package com.wenmrong.community1.community.controller;

import com.wenmrong.community1.community.dto.ResultDTO;
import com.wenmrong.community1.community.mapper.UserMapper;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.model.UserExample;
import com.wenmrong.community1.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@RestController
public class LoginController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/login")
    public String LoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String Login(HttpServletRequest request, HttpServletResponse response, Model model) {
        String account = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberFlag = request.getParameter("rememberFlag");
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(account)
                .andPasswordEqualTo(password);

        List<User> users = userMapper.selectByExample(userExample);
        if (users != null && users.size() != 0) {
            User user = users.get(0);
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            userMapper.updateByPrimaryKey(user);
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            if (rememberFlag != null) {
                cookie.setMaxAge(60 * 60 * 24);
            }
            response.addCookie(cookie);
            return "redirect:/";
        } else {
            model.addAttribute("loginFail", "fail");
            return "login";
        }


    }
    @GetMapping("/getCurrentUserRights")
    public ResultDTO<User> getCurrentUserRights() {
        User user = userMapper.selectByPrimaryKey(1l);
        return ResultDTO.okOf(user);
    }


}
