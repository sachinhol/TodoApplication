package com.example.todo.exceptions;

import com.example.todo.model.RestCustomErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleTodoNotFoundException() {
        TodoNotFoundException exception = new TodoNotFoundException("Todo not found");

        ResponseEntity<RestCustomErrorResponse> response = globalExceptionHandler.handleTodoNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Todo not found", response.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
    }

    @Test
    public void testHandleGeneralException() {
        Exception exception = new Exception("General error occurred");

        ResponseEntity<RestCustomErrorResponse> response = globalExceptionHandler.handleGeneralException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
    }

    @Test
    public void testHandleValidationExceptions() {
        FieldError fieldError = new FieldError("todo", "title", "Title must not be blank");
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(mock(org.springframework.validation.BindingResult.class));
        when(exception.getBindingResult().getFieldError()).thenReturn(fieldError);

        ResponseEntity<RestCustomErrorResponse> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Title must not be blank", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
    }

}