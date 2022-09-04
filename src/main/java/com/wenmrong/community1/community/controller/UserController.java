package com.wenmrong.community1.community.controller;

import com.github.pagehelper.PageInfo;
import com.wenmrong.community1.community.dto.ResultDTO;
import com.wenmrong.community1.community.dto.UserDto;
import com.wenmrong.community1.community.model.UserLike;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    @ResponseBody
    public ResultDTO updateLikeState(@RequestBody UserLike articleInfo) {
        userService.updateLikeState(articleInfo.getArticleId());
        return ResultDTO.okOf();
    }

    @GetMapping("/user/isValidUser")
    public ResultDTO isValidUser(String username) {
        userService.validateUserInfo(username);
        return ResultDTO.okOf();
    }

    @GetMapping("/user/getFollowCount")
    public ResultDTO getFollowCount(String userId) {
        Integer followCount = userService.getFollowCount(userId);
        return ResultDTO.okOf(followCount);
    }

    @GetMapping("/getFollowUsers")
    @ResponseBody
    public ResultDTO getFollowUsers(
            @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(name = "pageSize", defaultValue = "7") Integer pageSize,
            @RequestParam(required = false) String bigCow,
            @RequestParam(required = false) String fan) {

        List<UserDto> followUsers = userService.getFollowUsers(currentPage, pageSize, bigCow, fan);
        return ResultDTO.okOf(new PageInfo<>(followUsers));
    }
    @PostMapping("/updateFollowState")
    @ResponseBody
    public ResultDTO updateFollowState(@RequestBody UserDto user) {
        userService.updateFollowState(user);
        return ResultDTO.okOf();
    }
}
