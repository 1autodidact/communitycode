package com.wenmrong.community1.community;

import cn.hutool.core.util.ReflectUtil;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageConst;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * <h3>community</h3>
 * <p></p>
 *
 * @author : Autodidact
 * @date : 2022-08-27 14:38
 **/
public class TestReflect {
    @Test
    public void testReflect() throws IllegalAccessException {
        HashMap<String, String> map = new HashMap<>();
        map.put(MessageConst.PROPERTY_CLUSTER,"cluster");
        Message replyMessage = new Message();
        Field properties = ReflectUtil.getField(replyMessage.getClass(), "properties");
        ReflectUtil.setAccessible(properties);
        ReflectUtil.setFieldValue(replyMessage,properties,map);
        Object o = properties.get(replyMessage);
        System.out.println("aa");
    }


}
