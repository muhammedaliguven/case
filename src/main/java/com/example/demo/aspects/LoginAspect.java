package com.example.demo.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;


@Aspect
@Component
public class LoginAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = ZonedDateTime.now().toInstant().toEpochMilli();
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            logger.error("EXECUTION {} ({} throws an exception {})", joinPoint.getSignature(), joinPoint.getArgs(), e.getMessage(), e);
            throw e;
        } finally {
            long longExecutionTime = ZonedDateTime.now().toInstant().toEpochMilli() - start;
            if (longExecutionTime > 5) {
                logger.warn("{} executed  in {} ms", joinPoint.getSignature(), longExecutionTime);
            }
        }

    }
}
