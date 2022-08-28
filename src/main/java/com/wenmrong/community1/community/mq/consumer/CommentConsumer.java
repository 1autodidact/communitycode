package com.wenmrong.community1.community.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.wenmrong.community1.community.model.Comment;
import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.service.CommentService;
import com.wenmrong.community1.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <h3>community</h3>
 * <p></p>
 *
 * @author : Autodidact
 * @date : 2022-08-26 20:40
 **/
@Component
@RocketMQMessageListener(topic = "comment_topic",consumerGroup = "comment_consumer")
@Slf4j
public class CommentConsumer implements RocketMQListener<Comment> {

    @Autowired
    CommentService commentService;
    @Override
    public void onMessage(Comment message) {
//        questionService.createOrUpdate(message);
        log.error( "线程名称：" +Thread.currentThread().getName() + "XXXXXXXXXXXXXXXXXXXX" +"topic:question_topic {}", JSONObject.toJSONString(message));
    }
}
