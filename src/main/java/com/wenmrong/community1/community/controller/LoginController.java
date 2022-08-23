package com.wenmrong.community1.community.controller;

import com.wenmrong.community1.community.dto.ResultDTO;
import com.wenmrong.community1.community.mapper.UserMapper;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.model.UserExample;
import com.wenmrong.community1.community.service.NotificationService;
import com.wenmrong.community1.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

@Controller
public class LoginController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
    @GetMapping("/login")
    public String LoginPage() {
        return "login";
    }

//    @PostMapping("/login")
//    public String Login(HttpServletRequest request, HttpServletResponse response, Model model) {
//        String account = request.getParameter("email");
//        String password = request.getParameter("password");
//        String rememberFlag = request.getParameter("rememberFlag");
//        UserExample userExample = new UserExample();
//        userExample.createCriteria()
//                .andAccountIdEqualTo(account)
//                .andPasswordEqualTo(password);
//
//        List<User> users = userMapper.selectByExample(userExample);
//        if (users != null && users.size() != 0) {
//            User user = users.get(0);
//            String token = UUID.randomUUID().toString();
//            user.setToken(token);
//            userMapper.updateByPrimaryKey(user);
//            Cookie cookie = new Cookie("token", token);
//            cookie.setPath("/");
//            if (rememberFlag != null) {
//                cookie.setMaxAge(60 * 60 * 24);
//            }
//            response.addCookie(cookie);
//            return "redirect:/";
//        } else {
//            model.addAttribute("loginFail", "fail");
//            return "login";
//        }
//
//
//    }



    @PostMapping("/login")
    @ResponseBody
    public ResultDTO Login(@RequestBody User user) {
        return ResultDTO.okOf( userService.login(user));
    }


    @GetMapping("/getCurrentUserRights")
    @ResponseBody
    public ResultDTO<User> getCurrentUserRights(@RequestHeader("token")String token) {
        User user = userMapper.selectByPrimaryKey(1l);
        return ResultDTO.okOf();
    }


}
