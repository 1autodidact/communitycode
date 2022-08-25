package com.wenmrong.community1.community;

import com.wenmrong.community1.community.model.Question;
import com.wenmrong.community1.community.utils.SnowFlake;
import org.apache.commons.lang3.RandomUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ConsumeModeTest {
    public  SnowFlake snowFlake = new SnowFlake(2,3);
    @Autowired
    RocketMQTemplate rocketMQTemplate;
    @Test
    public void testConsumeMode() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(20);
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                Question question = new Question();
                long num = snowFlake.nextId();
                System.out.println("numXXXX " + num);
                question.setId(num);
                GenericMessage genericMessage = new GenericMessage(question);
                rocketMQTemplate.syncSendOrderly("question_topic",genericMessage,"order");
                countDownLatch.countDown();
                System.out.println("发送的num" + num);
            }).start();

        }
        countDownLatch.await();

        Thread.sleep(400000);
    }


}
