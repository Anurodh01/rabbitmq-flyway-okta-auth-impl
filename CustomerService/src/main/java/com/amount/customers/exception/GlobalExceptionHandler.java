package com.amount.customers.exception;

import com.amount.customers.dto.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validationExceptionHandler(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error(errors.toString());
        return errors;
    }
    @ExceptionHandler(UnAuthorizedAccessException.class)
    public ResponseEntity<APIResponse> unauthorizedExceptionHandler(UnAuthorizedAccessException ex){
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new APIResponse("fail",ex.getMessage(), HttpStatus.UNAUTHORIZED, LocalDateTime.now()));
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse("fail",ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()));
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<APIResponse> methodArgumentMismatchExceptionHandler(MethodArgumentTypeMismatchException ex){
        String message= "For "+ ex.getName() +" Integer value is expected!";
        log.error(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIResponse("fail",message, HttpStatus.BAD_REQUEST, LocalDateTime.now()));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<APIResponse> customExceptionHandler(CustomException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIResponse("fail",ex.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIResponse> parseExceptionHandler(HttpMessageNotReadableException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIResponse("fail",ex.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> globalExceptionHandler(Exception ex){
        log.error(ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse("fail",  ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR , LocalDateTime.now()));
    }

}
