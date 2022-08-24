package com.wenmrong.community1.community.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author wenmingrong@kungeek.com
 * @since
 */
@Component
public class CommunityContextHolder implements ApplicationContextAware, SmartInitializingSingleton {
    public  ApplicationContext applicationContext = null;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        applicationContext.getBean("webMvcConfigurationSupport");
        System.out.println("aa");
    }
}
