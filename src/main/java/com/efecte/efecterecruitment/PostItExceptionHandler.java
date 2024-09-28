package com.efecte.efecterecruitment;

import com.efecte.efecterecruitment.exception.ConflictException;
import com.efecte.efecterecruitment.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PostItExceptionHandler {
    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<Void> handleConflict(ConflictException conflictException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Void> handleNotFound(ResourceNotFoundException resourceNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(illegalArgumentException.getMessage());
    }
}
