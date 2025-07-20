package t1.workshop4.SyntheticHumanCoreStarter.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import t1.workshop4.SyntheticHumanCoreStarter.model.AuditMode;

@Target(ElementType.METHOD)  // Аннотация применяется к методам
@Retention(RetentionPolicy.RUNTIME)  // Доступна в runtime через reflection
public @interface WeylandWatchingYou {
  /**
   * Режим аудита (по умолчанию CONSOLE)
   */
  AuditMode mode() default AuditMode.CONSOLE;

  /**
   * Дополнительное описание действия (необязательное)
   */
  String description() default "";
}