package com.project1.backend.exception;

import com.project1.backend.payload.ApiResponseData;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;



@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseData<Object>> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request){


        ApiResponseData<Object> response = new ApiResponseData<>(
                false,
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                null

        );

        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseData<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ){

        Map<String,String> errors = new HashMap<>();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> {
                    errors.put(
                            fieldError.getField(),
                            fieldError.getDefaultMessage()
                    );
                } );

        ApiResponseData<Object> response = new ApiResponseData<>(
                false,
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors

        );



        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponseData<Object>> handleDuplicateResourceException(DuplicateResourceException e, HttpServletRequest request){

        ApiResponseData<Object> response = new ApiResponseData<>(
                false,
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                null

        );


        return new ResponseEntity<>(response,HttpStatus.CONFLICT);

    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String,String>> handleBadRequestException(BadRequestException e){

        Map<String,String> error = new HashMap<>();
        error.put("message",e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);

    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ApiResponseData<Object>> handleOptimisticLockException(OptimisticLockException e, HttpServletRequest request){
        ApiResponseData<Object> response = new ApiResponseData<>(
                false,
                LocalDateTime.now(),
                HttpStatus.CONTINUE.value(),
                e.getMessage(),
                null

        );


        return new ResponseEntity<>(response,HttpStatus.CONFLICT);
    }

}
