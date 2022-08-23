package com.wenmrong.community1.community.controller;

import com.wenmrong.community1.community.dto.ResultDTO;
import com.wenmrong.community1.community.mapper.UserMapper;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.model.UserExample;
import com.wenmrong.community1.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class RegisterController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register() {
        return "register";
    }


    @PostMapping("/register")
    @ResponseBody
    public ResultDTO registering(@RequestBody User user) {
        String userId = userService.createUser(user);
        return ResultDTO.okOf(userId);
    }


    @ResponseBody
    @GetMapping("/sendActiveEmail/{email}")
    public ResultDTO sendActiveEmail(@PathVariable(name = "email") String email, HttpServletResponse response) {
        //使用cookie存储验证码用于校验
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(email);
        List<User> users = userMapper.selectByExample(userExample);
        if (users != null && users.size() != 0) {
            return ResultDTO.errorOf(300,"Email already signup,please go to login ");
        }
        String activeCode = userService.sendEmail(email,"ActiveCode");
        Cookie cookie = new Cookie("activeCode", activeCode);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 5);
        response.addCookie(cookie);
        return ResultDTO.okOf(activeCode);
    }


}
