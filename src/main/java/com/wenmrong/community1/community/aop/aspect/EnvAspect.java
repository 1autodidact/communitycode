package com.wenmrong.community1.community.aop.aspect;

import com.wenmrong.community1.community.aop.annotation.Environment;
import com.wenmrong.community1.community.constants.ENV;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * <h3>community</h3>
 * <p></p>
 *
 * @author : Autodidact
 * @date : 2022-11-20 10:10
 **/
@Aspect
@Component
@Slf4j
//@Order(1)
public class EnvAspect {

    @Autowired
    RedisTemplate redisTemplate;
   
    
    
    @Pointcut("@annotation(com.wenmrong.community1.community.aop.annotation.Environment)")
    public void annotationAspect() {
    }


    /**
     * 前置通知 (在方法执行之前返回)用于拦截Controller层记录用户的操作的开始时间
     *
     * @param joinPoint 切点
     * @throws InterruptedException
     */
    @Around("annotationAspect()")
    public void envControl(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获得当前访问的class
        Class<?> className = joinPoint.getTarget().getClass();
        // 获得访问的方法名
        String methodName = joinPoint.getSignature().getName();
        // 得到方法的参数的类型
        Class<?>[] argClass = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        Object[] args = joinPoint.getArgs();


        try {
            // 得到访问的方法对象
            Method method = className.getMethod(methodName, argClass);
            method.setAccessible(true);
            if (method.isAnnotationPresent(Environment.class)) {
                Environment annotation = method.getAnnotation(Environment.class);
                String environment = annotation.environment();
                System.out.println("EnvAspect" + Thread.currentThread().getName());
                if (environment.equals(ENV.PRODUCT)) {
                    joinPoint.proceed(args);
                }

            }

        } catch (Exception e) {
            log.error(" 执行异常",e);
        }

    }
}
