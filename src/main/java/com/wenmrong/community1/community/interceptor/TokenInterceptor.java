package com.wenmrong.community1.community.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.wenmrong.community1.community.mapper.UserMapper;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.model.UserExample;
import com.wenmrong.community1.community.service.NotificationService;
import com.wenmrong.community1.community.utils.JwtTokenUtil;
import netscape.javascript.JSObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.wenmrong.community1.community.constants.CommunityConstants.COMMUNITY_USER_TOKEN;

@Service
public class TokenInterceptor implements HandlerInterceptor {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        String userInfoJson = jwtTokenUtil.getUserInfoFromToken(token);
        if (StringUtils.isBlank(userInfoJson)) {
            return false;
        }
        User user = null;
        try {
            JSONObject parse = (JSONObject) JSONObject.parse(userInfoJson);
            user = parse.toJavaObject(User.class);
        } catch (Exception e) {
            return false;
        }
        return redisTemplate.opsForValue().get(COMMUNITY_USER_TOKEN + user.getName()) != null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
