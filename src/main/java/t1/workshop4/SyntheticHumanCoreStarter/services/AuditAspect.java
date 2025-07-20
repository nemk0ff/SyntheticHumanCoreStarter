package t1.workshop4.SyntheticHumanCoreStarter.services;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import t1.workshop4.SyntheticHumanCoreStarter.annotations.WeylandWatchingYou;
import t1.workshop4.SyntheticHumanCoreStarter.model.AuditMode;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {
  private final KafkaTemplate<String, String> kafkaTemplate;

  @Around("@annotation(weylandWatchingYou)")
  public Object auditMethod(ProceedingJoinPoint joinPoint, WeylandWatchingYou weylandWatchingYou) throws Throwable {
    String methodName = joinPoint.getSignature().getName();
    String className = joinPoint.getTarget().getClass().getSimpleName();
    Object[] args = joinPoint.getArgs();

    // Формируем строку параметров
    String params = Arrays.stream(args)
        .map(arg -> arg != null ? arg.toString() : "null")
        .collect(Collectors.joining(", "));

    long startTime = System.currentTimeMillis();
    Object result;

    try {
      result = joinPoint.proceed();
    } catch (Exception e) {
      // Логируем исключения
      String errorMessage = String.format(
          "[AUDIT FAIL] Class: %s, Method: %s, Params: [%s], Error: %s",
          className, methodName, params, e.getMessage());

      if (weylandWatchingYou.mode() == AuditMode.KAFKA) {
        sendKafkaMessage("android-audit-errors", errorMessage);
      }
      log.error(errorMessage);
      throw e;
    }

    long executionTime = System.currentTimeMillis() - startTime;

    // Форматируем полное сообщение для аудита
    String auditMessage = String.format(
        "[AUDIT] Class: %s, Method: %s, Params: [%s], Result: %s, ExecutionTime: %dms",
        className, methodName, params,
        result != null ? result.toString() : "void",
        executionTime);

    // Отправляем в Kafka или логируем в консоль
    if (weylandWatchingYou.mode() == AuditMode.KAFKA) {
      sendKafkaMessage("android-audit", auditMessage);
    } else {
      log.info(auditMessage);
    }

    return result;
  }

  private void sendKafkaMessage(String topic, String message) {
    try {
      kafkaTemplate.send(topic, message)
          .thenAccept(result ->
              log.debug("Message sent to Kafka topic {}: {}", topic, message)
          )
          .exceptionally(ex -> {
            log.error("Failed to send message to Kafka topic {} : {}", topic, message);
            return null;
          });
    } catch (Exception e) {
      log.error("Kafka communication error", e);
    }
  }
}