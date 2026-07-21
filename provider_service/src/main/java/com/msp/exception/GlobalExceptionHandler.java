package com.msp.exception;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handleBaseException(BaseException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp", LocalDateTime.now());
        error.put("status", ex.getStatus().value());
        error.put("error", ex.getStatus().name());
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class
    })
    public ResponseEntity<?> handleValidationException(Exception ex) {

        Map<String, String> validationErrors = new HashMap<>();

        if (ex instanceof MethodArgumentNotValidException methodException) {

            for (FieldError fieldError :
                    methodException.getBindingResult().getFieldErrors()) {

                validationErrors.put(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                );
            }

        } else if (ex instanceof BindException bindException) {

            for (FieldError fieldError :
                    bindException.getBindingResult().getFieldErrors()) {

                validationErrors.put(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                );
            }
        }

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", HttpStatus.BAD_REQUEST.name());
        error.put("message", "Validation failed");
        error.put("validationErrors", validationErrors);

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        String message;

        if (ex.getRequiredType() != null
                && ex.getRequiredType().isEnum()) {

            message = "Invalid value '" + ex.getValue()
                    + "' for parameter '" + ex.getName()
                    + "'. Allowed values: "
                    + Arrays.toString(
                            ex.getRequiredType().getEnumConstants()
                    );

        } else {

            message = "Invalid value '" + ex.getValue()
                    + "' for parameter '" + ex.getName() + "'";
        }

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", HttpStatus.BAD_REQUEST.name());
        error.put("message", message);

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(
            IllegalArgumentException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", HttpStatus.BAD_REQUEST.name());
        error.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", HttpStatus.INTERNAL_SERVER_ERROR.name());
        error.put("message", "An unexpected error occurred");

        return ResponseEntity.internalServerError().body(error);
    }
}