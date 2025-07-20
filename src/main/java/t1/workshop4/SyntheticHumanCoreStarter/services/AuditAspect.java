package t1.workshop4.SyntheticHumanCoreStarter.services;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuditAspect {
  @Around("@annotation(WeylandWatchingYou)")
  public Object auditMethod(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();

    log.info("AUDIT: Method {} called with args {}", methodName, args);

    long start = System.currentTimeMillis();
    Object result = joinPoint.proceed();
    long duration = System.currentTimeMillis() - start;

    log.info("AUDIT: Method {} executed in {} ms with result {}",
        methodName, duration, result);

    return result;
  }
}
