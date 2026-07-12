package io.github.bartlomiejgora.trucks;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler
    ResponseEntity<String> handleException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());

    }
    @ExceptionHandler(exception = NoSuchElementException.class)
    ResponseEntity<String> handleNoSuchElementExceptionException() {
        return ResponseEntity.notFound().build();

    }
}
