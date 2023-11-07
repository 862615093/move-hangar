package snegrid.move.hangar.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import snegrid.move.hangar.annotation.ProcessLogToEs;

/**
 * 切面
 *
 * @author wangwei
 */
@Aspect
@Component
public interface ProcessLogToEsAspect {

    @AfterReturning(value = "@annotation(processLogToEs)", returning = "methodResult")
    void processLogToEs(JoinPoint joinPoint, ProcessLogToEs processLogToEs, Object methodResult);
}