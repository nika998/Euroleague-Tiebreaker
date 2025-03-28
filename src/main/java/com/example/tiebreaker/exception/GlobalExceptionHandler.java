package com.example.tiebreaker.exception;

import com.example.tiebreaker.dto.MatchDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({RemainingMatchesException.class})
    public ResponseEntity<List<MatchDTO>> handleRemainingMatchesException(RemainingMatchesException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ex.getRemainingMatches());
    }

    @ExceptionHandler({ExistingMatchesException.class})
    public ResponseEntity<List<MatchDTO>> handleExistingMatchesException(ExistingMatchesException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getExistingMatches());
    }
}
