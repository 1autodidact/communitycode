package com.wenmrong.community1.community.service;

import com.wenmrong.community1.community.dto.StatisticData;
import com.wenmrong.community1.community.mapper.CommentMapper;
import com.wenmrong.community1.community.mapper.QuestionMapper;
import com.wenmrong.community1.community.mapper.UserMapper;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <h3>community</h3>
 * <p></p>
 *
 * @author : Autodidact
 * @date : 2022-08-20 10:04
 **/
@Service
public class StatisticService {
    @Resource
    UserMapper userMapper;
    @Resource
    QuestionMapper questionMapper;
    @Resource
    CommentMapper commentMapper;
    public StatisticData statistic() {
        Integer usrCount = userMapper.selectCount(null);
        Integer questionCount = questionMapper.selectCount(null);
        Integer commentCount = commentMapper.selectCount(null);
        StatisticData statisticData = new StatisticData();
        statisticData.setUserCount(usrCount);
        statisticData.setCommentCount(commentCount);
        statisticData.setQuestionCount(questionCount);
        statisticData.setLevel("Lv2");
        statisticData.setLikeCount(5);
        return statisticData;
    }

}
