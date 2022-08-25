package com.wenmrong.community1.community.interceptor;

import com.wenmrong.community1.community.mapper.UserMapper;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.service.NotificationService;
import com.wenmrong.community1.community.utils.JwtTokenUtil;
import com.wenmrong.community1.community.utils.UserInfoProfile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Service
public class requestInterceptor implements HandlerInterceptor {
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
        List<String> excludePath = Arrays.asList("/getArticleCommentVisitTotal", "/getHotAuthorsList", "/getLatestComment", "/getQuestions", "/getCurrentUserRights");

        String token = request.getHeader("token");
        if (StringUtils.isBlank(token) && !excludePath.contains(request.getRequestURI())) {
            return false;
        }

        if (excludePath.contains(request.getRequestURI()) && StringUtils.isBlank(token)) {
            return true;
        }
        boolean tokenExpired = jwtTokenUtil.isTokenExpired(jwtTokenUtil.getExpirationDateFromToken(token));
        if (tokenExpired) {
            return false;
        }
        User user = jwtTokenUtil.getUserFromToken(token);
        UserInfoProfile.setUserProfile(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserInfoProfile.remove();

    }
}
