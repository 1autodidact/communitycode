package com.wenmrong.community1.community.aop.aspect;

import com.wenmrong.community1.community.aop.ParseModel.ParseModel;
import com.wenmrong.community1.community.aop.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.NamedThreadLocal;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Spring AOP实现日志管理
 *
 * @author liuyanzhao
 */
@Aspect
@Component
@Slf4j
public class SystemLogAspect {
    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    private static final ThreadLocal<Date> beginTimeThreadLocal = new NamedThreadLocal<Date>("ThreadLocal beginTime");

    private ExpressionParser parser = new SpelExpressionParser();

    @Autowired(required = false)
    private HttpServletRequest request;

    /**
     * Controller层切点,注解方式
     */
    //@Pointcut("execution(* *..controller..*Controller*.*(..))")
    @Pointcut("@annotation(com.wenmrong.community1.community.aop.annotation.SystemLog)")
    public void controllerAspect() {
    }

    /**
     * 前置通知 (在方法执行之前返回)用于拦截Controller层记录用户的操作的开始时间
     *
     * @param joinPoint 切点
     * @throws InterruptedException
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) throws InterruptedException {
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
            // 判断是否存在@ExtCacheable注解
            if (method.isAnnotationPresent(SystemLog.class)) {
                SystemLog annotation = method.getAnnotation(SystemLog.class);
                Class<? extends ParseModel> model = annotation.model();

                ParseModel parseModel = model.newInstance();
                Long customId = (Long) parseModel.doConvert(joinPoint);
                System.out.println("customId" + customId);
                Expression expression = parser.parseExpression(annotation.traceId());
                EvaluationContext context = this.bindParam(method, args);
                String id = expression.getValue(context, String.class);
                System.out.println("traceId" + id);
            }

        } catch (Exception e) {
        }

    }

    /**
     * 后置通知(在方法执行之后并返回数据) 用于拦截Controller层无异常的操作
     *
     * @param joinPoint 切点
     */
    @AfterReturning("controllerAspect()")
    public void after(JoinPoint joinPoint) {

    }

    private EvaluationContext bindParam(Method method, Object[] args) {
        //获取方法的参数名
        String[] params = discoverer.getParameterNames(method);
        //将参数名与参数值对应起来
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], args[len]);
        }
        return context;
    }


}