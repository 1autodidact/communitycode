package com.wenmrong.community1.community.controller;

import com.wenmrong.community1.community.dto.ResultDTO;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <h3>community</h3>
 * <p></p>
 *
 * @author : Autodidact
 * @date : 2022-08-20 08:52
 **/
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/getHotAuthorsList")
    @ResponseBody
    public ResultDTO getHotAuthorsList(@RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                                       @RequestParam(name = "pageSize", defaultValue = "7") Integer pageSize) {

        return ResultDTO.okOf(userService.getHotAuthorsList());
    }


    @GetMapping("user/getUserInfo")
    public ResultDTO getUserInfo(@RequestParam String userId) {
        User userInfo = userService.getUserInfo(userId);
        return ResultDTO.okOf(userInfo);
    }

    @PostMapping("/user/updateLikeState")
    public ResultDTO updateLikeState(String articleId) {

        userService.updateLikeState(articleId);
        return ResultDTO.okOf();
    }

    @GetMapping("/user/isValidUser")
    public ResultDTO isValidUser(String username) {
        userService.validateUserInfo(username);
        return ResultDTO.okOf();
    }
}
