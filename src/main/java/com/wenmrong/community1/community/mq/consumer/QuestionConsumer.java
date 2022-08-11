package com.wenmrong.community1.community.mq.consumer;

import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.service.QuestionService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
@Component
@RocketMQMessageListener(topic = "question_topic",consumerGroup = "question_consumer")
public class QuestionConsumer implements RocketMQListener<Question> {
    @Autowired
    QuestionService questionService;

    @Override
    public void onMessage(Question message) {
        questionService.createOrUpdate(message);
        System.out.println(message) ;
    }
}
