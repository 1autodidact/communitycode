package com.wenmrong.community1.community.controller;
 
import com.wenmrong.community1.community.dto.ResultDTO;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
@RequestMapping("/mqMessageController")
public class MqMessageController {
    private static final Logger log = LoggerFactory.getLogger(MqMessageController.class);
 
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
 
    @RequestMapping("/pushMessage.action")
    public ResultDTO get(@RequestParam("id") int id) {
        rocketMQTemplate.convertAndSend("first-topic","你好,Java旅途" + id);
        ResultDTO msg = new ResultDTO();
        return msg;
    }
 
}