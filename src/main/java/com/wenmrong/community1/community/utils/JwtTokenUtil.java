package com.wenmrong.community1.community.utils;

import com.alibaba.fastjson.JSONObject;
import com.wenmrong.community1.community.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    public String secret;
    @Value("${jwt.expire}")
    public int expire;
    @Value("${jwt.header}")
    public String header;

    /**
     * 生成token
     *
     * @param subject 加密的数据
     * @return
     */
    public String createToken(String subject) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(subject)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 获取token中注册信息
     *
     * @param token
     * @return
     */
    public Claims getTokenClaim(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证token是否过期失效
     *
     * @param expirationTime
     * @return
     */
    public boolean isTokenExpired(Date expirationTime) {
        try {
            return expirationTime.before(new Date());
        } catch (Exception e) {
            log.error("isTokenExpired异常",e);
            return true;
        }
    }

    /**
     * 获取token失效时间
     *
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        try {
            return getTokenClaim(token).getExpiration();
        } catch (Exception e) {
           return new Date(1661146635210l);
        }
    }

    /**
     * 获取用户名从token中
     */
    public String getUserInfoFromToken(String token) {
        return getTokenClaim(token).getSubject();
    }

    /**
     * 获取jwt发布时间
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getTokenClaim(token).getIssuedAt();
    }

    public User getUserFromToken(String token) {
        String userInfoJson = this.getUserInfoFromToken(token);
        JSONObject parse = (JSONObject) JSONObject.parse(userInfoJson);
        return parse.toJavaObject(User.class);
    }
}
