package com.wenmrong.community1.community.config;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.ByteArrayMessageConverter;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
@Component
public class RocketMQConfig {

    @Bean(name = "rocketMQTemplate")
    public RocketMQTemplate rocketMQTemplate(DefaultMQProducer mqProducer,
                                             RocketMQMessageConverter rocketMQMessageConverter) {
        RocketMQTemplate rocketMQTemplate = new RocketMQTemplate();
        rocketMQTemplate.setProducer(mqProducer);

        List<MessageConverter> messageConverters = new ArrayList<>();
        // 自定义的转换器
        CustomMessageConverter customMessageConverter = new CustomMessageConverter();
        messageConverters.add(customMessageConverter);
        CompositeMessageConverter compositeMessageConverter = new CompositeMessageConverter(messageConverters);



        rocketMQTemplate.setMessageConverter(compositeMessageConverter);
        // 默认的配置
        rocketMQTemplate.setMessageConverter( rocketMQMessageConverter.getMessageConverter());
        return rocketMQTemplate;
    }
}
