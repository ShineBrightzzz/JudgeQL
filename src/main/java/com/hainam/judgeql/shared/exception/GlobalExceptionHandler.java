package com.hainam.judgeql.shared.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.hainam.judgeql.shared.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;

import org.apache.catalina.connector.Response;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import jakarta.validation.ConstraintViolationException;

import java.util.stream.Collectors;

import com.hainam.judgeql.shared.exception.ResourceNotFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<?>> handleBaseException(BaseException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(ApiResponse.error(msg));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid value for parameter: " + ex.getName()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<ApiResponse<?>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.error("Phương thức " + "không được hỗ trợ"));
        }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(ConstraintViolationException ex) {
        String msg = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(ApiResponse.error(msg));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        String msg = "Missing required parameter: " + ex.getParameterName();
        return ResponseEntity.badRequest().body(ApiResponse.error(msg));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        String message = ex.getMessage();
        if (cause != null) {
            String causeMsg = cause.getMessage();
            if (causeMsg != null && (causeMsg.contains("missing property") || causeMsg.contains("Missing required creator property"))) {
                int start = causeMsg.indexOf("'"), end = causeMsg.indexOf("'", start + 1);
                String field = (start != -1 && end != -1) ? causeMsg.substring(start + 1, end) : "unknown";
                return ResponseEntity.badRequest().body(ApiResponse.error("Thiếu trường bắt buộc: " + field));
            }
        }
        if (message != null && message.contains("Missing required creator property")) {
            int start = message.indexOf("'"), end = message.indexOf("'", start + 1);
            String field = (start != -1 && end != -1) ? message.substring(start + 1, end) : "unknown";
            return ResponseEntity.badRequest().body(ApiResponse.error("Thiếu trường bắt buộc: " + field));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("Malformed JSON request"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDenied(AccessDeniedException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Bạn không quá quyền truy cập endpoint này"));
    }

    @ExceptionHandler(MismatchedInputException.class)
    public ResponseEntity<ApiResponse<?>> handleMismatchedInput(MismatchedInputException ex) {
        String message = ex.getMessage();
        if (message != null && message.contains("missing property")) {
            int start = message.indexOf("'"), end = message.indexOf("'", start + 1);
            String field = (start != -1 && end != -1) ? message.substring(start + 1, end) : "unknown";
            return ResponseEntity.badRequest().body(ApiResponse.error("Thiếu trường bắt buộc: " + field));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("Thiếu trường dữ liệu hoặc dữ liệu không hợp lệ"));
    }

    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleHibernateConstraintViolation(org.hibernate.exception.ConstraintViolationException ex) {
        String msg = ex.getSQLException() != null ? ex.getSQLException().getMessage() : ex.getMessage();
        if (msg != null && msg.contains("null value in column")) {
            int colIdx = msg.indexOf("\"", msg.indexOf("column"));
            int colEnd = msg.indexOf("\"", colIdx + 1);
            String field = (colIdx != -1 && colEnd != -1) ? msg.substring(colIdx + 1, colEnd) : "unknown";
            return ResponseEntity.badRequest().body(ApiResponse.error("Thiếu trường bắt buộc: " + field));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("Dữ liệu không hợp lệ hoặc thiếu trường bắt buộc"));
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolation(org.springframework.dao.DataIntegrityViolationException ex) {
        String msg = ex.getMessage();
        if (msg != null && msg.contains("null value in column")) {
            int colIdx = msg.indexOf('"', msg.indexOf("column"));
            int colEnd = msg.indexOf('"', colIdx + 1);
            String field = (colIdx != -1 && colEnd != -1) ? msg.substring(colIdx + 1, colEnd) : "unknown";
            return ResponseEntity.badRequest().body(ApiResponse.error("Thiếu trường bắt buộc: " + field));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("Dữ liệu không hợp lệ hoặc thiếu trường bắt buộc"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleOther(Exception ex) {
        log.error("Unexpected error: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal Server Error"));
    }
}
