package com.wenmrong.community1.community.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.service.QuestionService;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
@Component
@RocketMQMessageListener(topic = "question_topic",consumerGroup = "question_consumer")
public class QuestionConsumer implements RocketMQListener<Question> {
    private static final Logger log = LoggerFactory.getLogger(QuestionConsumer.class);

    @Autowired
    QuestionService questionService;
    @Override
    public void onMessage(Question message) {
//        questionService.createOrUpdate(message);
        log.error( "线程名称：" +Thread.currentThread().getName() + "XXXXXXXXXXXXXXXXXXXX" +"topic:question_topic {}", JSONObject.toJSONString(message));
    }
}
