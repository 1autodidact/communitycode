package com.wenmrong.community1.community;

import com.alibaba.fastjson.JSONObject;
import com.wenmrong.community1.community.mq.consumer.QuestionConsumer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <h3>community</h3>
 * <p></p>
 *
 * @author : Autodidact
 * @date : 2022-08-15 20:43
 **/
@SpringBootTest
public class TestLog {
    private static final Logger log = LoggerFactory.getLogger(TestLog.class);

    @Test
    public void log() {
        log.error("topic:question_topic{}", "aaa");
        System.out.println("aa");
    }
}
