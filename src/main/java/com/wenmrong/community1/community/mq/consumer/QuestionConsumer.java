//package com.wenmrong.community1.community.mq.consumer;
//
//import cn.hutool.core.util.ReflectUtil;
//import com.alibaba.fastjson.JSONObject;
//import com.google.common.reflect.Reflection;
//import com.wenmrong.community1.community.dto.ResultDTO;
//import com.wenmrong.community1.community.mapper.QuestionMapper;
//import com.wenmrong.community1.community.model.Question;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.common.message.Message;
//import org.apache.rocketmq.common.message.MessageConst;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.apache.rocketmq.spring.core.RocketMQReplyListener;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.lang.reflect.Field;
//import java.util.HashMap;
//
//@Component
//@RocketMQMessageListener(topic = "question_topic",consumerGroup = "question_consumer")
//@Slf4j
//public class QuestionConsumer implements RocketMQReplyListener<Question, Question> {
//    @Resource
//    QuestionMapper questionMapper;
//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;
//    @Override
//    public Question onMessage(Question message) {
//        questionMapper.insert(message);
//        log.error( "线程名称：{}" +"message {}",Thread.currentThread().getName(),JSONObject.toJSONString(message));
//        return message;
//    }
//}
