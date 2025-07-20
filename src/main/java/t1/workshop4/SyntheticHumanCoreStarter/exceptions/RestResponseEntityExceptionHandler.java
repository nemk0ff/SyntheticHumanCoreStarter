package t1.workshop4.SyntheticHumanCoreStarter.exceptions;

import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex, @NotNull HttpHeaders headers,
      @NotNull HttpStatusCode status, @NotNull WebRequest request) {
    log.warn("Некорректный JSON: {}", ex.getMessage());

    ProblemDetail problemDetail = problemDetailBuilder(
        "Некорректный запрос", request, ex);
    problemDetail.setDetail("Проверьте формат и типы данных в теле запроса");

    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ProblemDetail> handleTypeMismatch(
      MethodArgumentTypeMismatchException ex, WebRequest request) {
    log.warn("Ошибка парсинга параметра '{}': ожидался тип {}",
        ex.getName(), ex.getRequiredType());

    ProblemDetail problemDetail = problemDetailBuilder(
        "Неверный тип параметра", request, ex);

    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ProblemDetail> handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {
    log.warn("Ошибка валидации параметров: {}", ex.getMessage());

    ProblemDetail problemDetail = problemDetailBuilder(
        "Ошибка валидации параметров",
        request,
        ex);

    List<String> errors = ex.getConstraintViolations().stream()
        .map(v -> v.getPropertyPath() + ": " + v.getMessage())
        .toList();
    problemDetail.setProperty("errors", errors);

    return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      @Nullable HttpHeaders headers,
      @Nullable HttpStatusCode status,
      @Nullable WebRequest request) {
    log.warn("Ошибка валидации данных: {}", ex.getMessage());

    ProblemDetail problemDetail = problemDetailBuilder("Ошибка валидации данных",
        request, ex);

    List<String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .toList();
    problemDetail.setProperty("errors", errors);

    return new ResponseEntity<>(problemDetail, headers, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(CommandQueueFullException.class)
  public ResponseEntity<String> handleQueueFull(CommandQueueFullException ex) {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(ex.getMessage());
  }

  private ProblemDetail problemDetailBuilder(String title, WebRequest request,
                                             Exception ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST, ex.getMessage());
    problemDetail.setTitle(title);
    problemDetail.setProperty("timestamp", Instant.now());
    problemDetail.setProperty("path", ((ServletWebRequest) request).getRequest().getRequestURI());
    return problemDetail;
  }
}