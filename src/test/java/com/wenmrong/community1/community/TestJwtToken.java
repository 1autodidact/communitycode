package com.wenmrong.community1.community;

import com.alibaba.fastjson.JSONObject;
import com.wenmrong.community1.community.model.User;
import com.wenmrong.community1.community.utils.JwtTokenUtil;
import com.wenmrong.community1.community.utils.UserInfoProfile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestJwtToken {
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Test
    public void testExpired() {
        User user = new User();
        user.setName("aa");
        String token = jwtTokenUtil.createToken(JSONObject.toJSONString(user));
        boolean tokenExpired = jwtTokenUtil.isTokenExpired(jwtTokenUtil.getExpirationDateFromToken(token));
        System.out.println("aaa");
    }
}
