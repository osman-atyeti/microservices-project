package com.osman.quizservice.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.osman.quizservice.controller.*.*(..)) || execution(* com.osman.quizservice.service.*.*(..))")
    public void logBeforeMethodExecution(JoinPoint joinPoint) {
        System.out.println("Before Executing method: " + joinPoint.getTarget().getClass().getSimpleName() + "." + joinPoint.getSignature().getName());
    }

    @AfterReturning(value = "execution(* com.osman.quizservice.service.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("After Method executed: " + joinPoint.getSignature().getName() + ", Result: " + result);
    }
}