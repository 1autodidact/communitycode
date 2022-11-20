package com.wenmrong.community1.community.aop.aspect;

import com.wenmrong.community1.community.aop.ParseModel.ParseModel;
import com.wenmrong.community1.community.aop.annotation.DsLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * <h3>community</h3>
 * <p></p>
 *
 * @author : Autodidact
 * @date : 2022-11-17 20:34
 **/
@Aspect
@Component
@Slf4j
public class DsLockAspect {

    @Autowired
    RedisTemplate redisTemplate;
    /**
     * Controller层切点,注解方式
     */
    //@Pointcut("execution(* *..controller..*Controller*.*(..))")
    @Pointcut("@annotation(com.wenmrong.community1.community.aop.annotation.DsLock)")
    public void annotationAspect() {
    }


    /**
     * 前置通知 (在方法执行之前返回)用于拦截Controller层记录用户的操作的开始时间
     *
     * @param joinPoint 切点
     * @throws InterruptedException
     */
    @Around("annotationAspect()")
    public void lock(ProceedingJoinPoint joinPoint) throws Throwable {
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
            if (method.isAnnotationPresent(DsLock.class)) {
                DsLock annotation = method.getAnnotation(DsLock.class);
                String key = annotation.key();
                String value = annotation.value();
                long timeout = annotation.timeout();
                boolean lock = redisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.MILLISECONDS) == Boolean.TRUE;
                System.out.println("DSLockASpect" + Thread.currentThread().getName() + "  加锁 " +lock);

                if (lock) {
                    joinPoint.proceed(args);
                    log.error(Thread.currentThread().getName() + "  分布式锁释放");
                }

            }

        } catch (Exception e) {
            log.error("分布式锁aop 执行异常",e);
        }

    }


}
