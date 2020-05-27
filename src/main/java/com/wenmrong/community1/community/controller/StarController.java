package com.wenmrong.community1.community.controller;

import com.wenmrong.community1.community.dto.ResultDTO;
import com.wenmrong.community1.community.mapper.UserMapper;
import com.wenmrong.community1.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class StarController {
    @Autowired
    private UserMapper userMapper;
    @RequestMapping(value = "/star", method = RequestMethod.POST)
    @ResponseBody
    public ResultDTO star(@RequestBody String starId, HttpServletRequest request) throws UnsupportedEncodingException {
        User user = (User) request.getSession().getAttribute("user");
        if (user.getStar() != null && !user.getStar().equals("")) {
            String userStars = user.getStar();
            List<String> starList = Arrays.asList(userStars.substring(0, userStars.length() - 1).split("="));
            if (starList.contains(starId.substring(0,starId.length() - 1))) {
                ResultDTO already_stared = new ResultDTO().errorOf(234, "Already Stared");
                System.out.println(already_stared.getCode());
                return already_stared;
            }
            String star = user.getStar();
            user.setStar(star + starId);
            userMapper.updateByPrimaryKey(user);
        }else {
            user.setStar(starId);
            userMapper.updateByPrimaryKey(user);
        }
        return new ResultDTO().okOf();
    }
    @RequestMapping(value = "/unstar/{id}")
    @ResponseBody
    public ResultDTO unStar(@PathVariable String id,HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        String userStars = user.getStar();
        List<String> starList = Arrays.asList(userStars.substring(0, userStars.length() - 1).split("="));
        List<String> starListByArrayList = new ArrayList(starList);
        for (String s : starList) {
            if (s.equals(id)) {
                starListByArrayList.remove(s);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : starListByArrayList) {
            stringBuilder.append(s+"=");
        }
        user.setStar(stringBuilder.toString());
        userMapper.updateByPrimaryKey(user);
        return ResultDTO.okOf();
    }
}
