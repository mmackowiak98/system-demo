package org.dpd.handler;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception exception) {
        log.error("An exception occurred");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(
            MethodArgumentNotValidException exception) {
        log.error("Validation error occurred");

        List<String> errorMessages =
                exception.getBindingResult().getAllErrors().stream()
                        .map(
                                objectError -> {
                                    if (objectError instanceof FieldError) {
                                        return ((FieldError) objectError).getField()
                                                + " "
                                                + objectError.getDefaultMessage();
                                    } else {
                                        return objectError.getDefaultMessage();
                                    }
                                })
                        .collect(Collectors.toList());

        String errorMessage = String.join(", ", errorMessages);

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
}
