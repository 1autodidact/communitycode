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
    public String registering(HttpServletRequest request, HttpServletResponse response, Model model) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String code = request.getParameter("code");
        Cookie[] cookies = request.getCookies();
        User user = new User();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("activeCode")) {
                String value = cookie.getValue();
                if (value.equals(code)) {
                    user.setStatus(1);
                    break;
                }
            }
        }
        if (user.getStatus() == 1) {
            user.setName(email);
            user.setPassword(password);
            user.setAccountId(email);
            user.setAvatarUrl("http://cdn.wenmrong.com/grey.png");
            userService.createOrUpdate(user);
            model.addAttribute("signupSuccess", "success");
            return "register";
        } else {
            //注册失败
            return "register";
        }

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
