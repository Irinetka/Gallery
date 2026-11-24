package com.example.gallery.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // Общий формат JSON-ответа об ошибке
    private ResponseEntity<Map<String, Object>> buildBody(HttpStatus status, String message,
                                                          String hint, String type) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);   // что произошло
        body.put("hint", hint);         // как исправить
        body.put("exceptionType", type);
        return ResponseEntity.status(status).body(body);
    }

    // 400 Bad Request — некорректные входные данные
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(ValidationException ex) {
        return buildBody(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getHint(), "ValidationException");
    }

    // 404 Not Found — объект отсутствует в базе
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(EntityNotFoundException ex) {
        return buildBody(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getHint(), "EntityNotFoundException");
    }

    // 422 Unprocessable Entity — нарушено бизнес-правило
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessRule(BusinessRuleException ex) {
        return buildBody(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex.getHint(), "BusinessRuleException");
    }

    // 401 Unauthorized — неверный логин/пароль
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleCredentials(InvalidCredentialsException ex) {
        return buildBody(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex.getHint(), "InvalidCredentialsException");
    }

    // 409 Conflict — дубликат (id или username уже занят)
    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateEntityException ex) {
        return buildBody(HttpStatus.CONFLICT, ex.getMessage(), ex.getHint(), "DuplicateEntityException");
    }

    // 403 Forbidden — недостаточно прав
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        return buildBody(HttpStatus.FORBIDDEN, ex.getMessage(), ex.getHint(), "AccessDeniedException");
    }

    // Отдельная обработка исключения Spring Security
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleSpringAccessDenied(
            org.springframework.security.access.AccessDeniedException ex) {
        return buildBody(HttpStatus.FORBIDDEN, "Access denied by Spring Security",
                "Check your role permissions or authenticate again",
                "SpringAccessDeniedException");
    }

    // Фолбэк: любая непредвиденная ошибка — без «голого» стектрейса клиенту
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpected(Exception ex) {
        return buildBody(HttpStatus.INTERNAL_SERVER_ERROR,
               "Unexpected server error: " + ex.getMessage(),
                "Try again later or contact the administrator",
                ex.getClass().getSimpleName());
    }
}