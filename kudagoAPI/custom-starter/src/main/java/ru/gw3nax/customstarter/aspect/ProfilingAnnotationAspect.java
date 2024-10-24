package ru.gw3nax.customstarter.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class ProfilingAnnotationAspect {

    @Around("@within(ru.gw3nax.customstarter.aspect.Profiling) || @annotation(ru.gw3nax.customstarter.aspect.Profiling)")
    private Object profile(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Class<?> clazz = method.getDeclaringClass();
        Object value;
        log.info("Profiling [" + method.getName() + "] from class [" + clazz.getName() + "]");
        Long startTime = System.currentTimeMillis();
        try {
            value = pjp.proceed();
        } catch (Throwable t) {
            throw t;
        } finally {
            Long endTime = System.currentTimeMillis();
            log.info("Profiling [" + method.getName() + "] from class [" + clazz.getName() + "] took " + (endTime - startTime) + "ms");
        }
        return value;
    }
}
