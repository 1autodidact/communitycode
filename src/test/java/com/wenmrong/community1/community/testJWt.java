package com.wenmrong.community1.community;

import com.alibaba.fastjson.JSONObject;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.utils.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * <h3>community</h3>
 * <p></p>
 *
 * @author : Autodidact
 * @date : 2022-08-23 20:50
 **/
@SpringBootTest
public class
testJWt {
    @Resource
    JwtTokenUtil jwtTokenUtil;
    @Test
    public void testJwt() {
        User user = new User();
        user.setName("aaaa");
        user.setStatus(1);
        String admin = jwtTokenUtil.createToken(JSONObject.toJSONString(user));
        String usernameFromToken = jwtTokenUtil.getUserInfoFromToken(admin);

        System.out.println("aaa");
    }
}
