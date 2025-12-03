package com.osman.quizservice.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionHandlingAspect {

    @AfterThrowing(pointcut = "execution(* com.osman.quizservice.service.*.*(..))", throwing = "ex")
    public void handleException(JoinPoint joinPoint, Exception ex) {
        System.err.println("Exception in method: " + joinPoint.getTarget().getClass().getSimpleName()+"."+joinPoint.getSignature().getName() + ", Message: " + ex.getMessage());
    }
}