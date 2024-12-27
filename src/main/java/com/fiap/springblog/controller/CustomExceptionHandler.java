package com.fiap.springblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler {
    //Encapsular tratamento de excessões customizadas

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationException(MethodArgumentNotValidException ex){
        //Map para pegar lista de erros de validação (spring-validation)
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField()
                    + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
