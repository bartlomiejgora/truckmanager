package io.github.bartlomiejgora.trucks;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler
    ResponseEntity<String> handleException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());

    }
}
