package com.wenmrong.community1.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.wenmrong.community1.community.dto.ResultDTO;
import com.wenmrong.community1.community.dto.UserDto;
import com.wenmrong.community1.community.mapper.UserMapper;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.service.NotificationService;
import com.wenmrong.community1.community.service.UserService;
import com.wenmrong.community1.community.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    @ResponseBody
    public ResultDTO Login(@RequestBody User user) throws InterruptedException {
        return ResultDTO.okOf(userService.login(user));
    }


    @GetMapping("/getCurrentUserRights")
    @ResponseBody
    public ResultDTO<UserDto> getCurrentUserRights() {
        return ResultDTO.okOf(userService.getCurrentUserRights());
    }

    @GetMapping("/logout")
    @ResponseBody
    public ResultDTO logout() {
        userService.logout();
        return ResultDTO.okOf();
    }


}
