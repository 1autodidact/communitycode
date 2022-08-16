package com.wenmrong.community1.community.controller;
 
import com.wenmrong.community1.community.utils.SnowFlake;
import com.wenmrong.community1.community.dto.ResultDTO;
import com.wenmrong.community1.community.model.NftOrder;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
@RequestMapping("/mqMessageController")
public class MqMessageController {
    private static final Logger log = LoggerFactory.getLogger(MqMessageController.class);
    static SnowFlake snowFlake = new SnowFlake(2, 3);
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @RequestMapping("/pushMessage.action")
    public ResultDTO get(@RequestParam("id") int id) {
        rocketMQTemplate.convertAndSend("first-topic","你好,Java旅途" + id);
        ResultDTO msg = new ResultDTO();
        return msg;
    }
    @RequestMapping("/pushTsMsg")
    public ResultDTO get() throws MQClientException, InterruptedException {
        final long id = snowFlake.nextId();
        final NftOrder order = new NftOrder();
        order.setId(String.valueOf(id));
        Message<NftOrder> message = MessageBuilder.withPayload(order)
                .setHeader(RocketMQHeaders.TRANSACTION_ID, String.valueOf(id))
                .build();
        rocketMQTemplate.sendMessageInTransaction("ts_order_topic",message,null);
        ResultDTO msg = new ResultDTO();
        return msg;
    }
}