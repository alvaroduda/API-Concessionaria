package com.concessionaria.carros.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {
    
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    
    @Test
    void handleBusinessException_DeveRetornarBadRequest() {
        BusinessException exception = new BusinessException("Erro de negócio");
        
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = handler.handleBusinessException(exception);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro de negócio", response.getBody().message());
    }
    
    @Test
    void handleValidationExceptions_DeveRetornarBadRequest() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        FieldError fieldError = new FieldError("objeto", "campo", "mensagem de erro");
        when(exception.getBindingResult().getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        
        ResponseEntity<Map<String, String>> response = handler.handleValidationExceptions(exception);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("mensagem de erro", response.getBody().get("campo"));
    }
    
} 