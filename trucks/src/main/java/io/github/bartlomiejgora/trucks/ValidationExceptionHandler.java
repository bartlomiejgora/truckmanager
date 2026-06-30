package io.github.bartlomiejgora.trucks;

import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler
    ResponseEntity<String> handleException(Exception exception){
        return ResponseEntity.badRequest().body(exception.getMessage());

    }
}
