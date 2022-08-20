package com.wenmrong.community1.community.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wenmrong.community1.community.dto.ResultDTO;
import com.wenmrong.community1.community.mapper.QuestionMapper;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
}
