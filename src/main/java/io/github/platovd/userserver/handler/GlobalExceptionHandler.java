package io.github.platovd.userserver.handler;

import io.github.platovd.userserver.exception.PatchException;
import io.github.platovd.userserver.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handle(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handle(ConstraintViolationException exception) {
        var errors = new HashMap<String, String>();
        exception.getConstraintViolations().forEach(e -> {
                    String fieldName = "";
                    for (Path.Node node : e.getPropertyPath()) {
                        fieldName = node.getName();
                    }
                    String errorInfo = e.getMessage();
                    errors.put(fieldName, errorInfo);
                }
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errors));
    }

    @ExceptionHandler(PatchException.class)
    public ResponseEntity<String> handle(PatchException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
