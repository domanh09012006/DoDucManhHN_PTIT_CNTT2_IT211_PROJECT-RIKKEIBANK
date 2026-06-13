package org.example.rikkeibank.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* org.example.rikkeibank.service.impl.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            log.info("✅ [{}] executed in {} ms", methodName, executionTime);
            return result;

        } catch (Exception ex) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error(" [{}] failed after {} ms. Error: {}",
                    methodName, executionTime, ex.getMessage());
            throw ex;
        }
    }
}