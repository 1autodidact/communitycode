package com.wenmrong.community1.community;

import cn.hutool.core.util.RandomUtil;
import com.wenmrong.community1.community.dto.QuestionDTO;
import com.wenmrong.community1.community.dto.UserDto;
import com.wenmrong.community1.community.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
@SpringBootTest
public class TestAop {
    @Autowired
    QuestionService questionService;
    @Autowired
    RedisTemplate redisTemplate;
    @Test
    public void log() {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(232323232322l);
        UserDto userDto = new UserDto();
        userDto.setId(45343434343l);
        questionDTO.setUser(userDto);
        questionService.searchQuestion(questionDTO);
        System.out.println("aa");
    }
    @Test
    public void logAop() {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(232323232322l);
        UserDto userDto = new UserDto();
        userDto.setId(45343434343l);
        questionDTO.setUser(userDto);
        questionService.logInfoByParseModel(questionDTO);
        System.out.println("aa");
    }

    @Test
    public void addLock() throws InterruptedException {
        questionService.addLock();
//        for (int i = 0; i < 22; i++) {
//            new Thread(() -> {
//                try {
//                    Thread.sleep(RandomUtil.randomInt(1) * 1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                questionService.addLock();
//            }).start();
//        }
//        Thread.sleep(1500);
//        for (int i = 0; i < 12; i++) {
//            new Thread(() -> {
//                try {
//                    Thread.sleep(RandomUtil.randomInt(1) * 1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                questionService.addLock();
//            }).start();
//        }
        Thread.sleep(8000);
    }
}
