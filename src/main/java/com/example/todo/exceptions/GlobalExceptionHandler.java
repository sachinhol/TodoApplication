package com.example.todo.exceptions;

import com.example.todo.model.RestCustomErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
/*
 * Author: Sachin Hol
 * Date: 27-Oct-24
 * This class is used to handle global exception in application
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle TodoNotFoundException
    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<RestCustomErrorResponse> handleTodoNotFoundException(TodoNotFoundException ex) {
        logger.error("TodoNotFoundException: {}", ex.getMessage());
        RestCustomErrorResponse errorResponse = new RestCustomErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Handle other general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestCustomErrorResponse> handleGeneralException(Exception ex) {
        logger.error("Exception: {}", ex.getMessage());
        RestCustomErrorResponse errorResponse = new RestCustomErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestCustomErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        RestCustomErrorResponse errorResponse = new RestCustomErrorResponse(HttpStatus.BAD_REQUEST.value(), message);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
