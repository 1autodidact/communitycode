package com.wenmrong.community1.community;

import com.wenmrong.community1.community.dto.QuestionDTO;
import com.wenmrong.community1.community.dto.UserDto;
import com.wenmrong.community1.community.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
@SpringBootTest
public class TestAop {
    @Autowired
    QuestionService questionService;
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
}
