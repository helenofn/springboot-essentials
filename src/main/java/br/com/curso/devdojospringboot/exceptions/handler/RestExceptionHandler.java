package br.com.curso.devdojospringboot.exceptions.handler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.curso.devdojospringboot.exceptions.BadRequestException;
import br.com.curso.devdojospringboot.exceptions.BadRequestExceptionDetaills;
import br.com.curso.devdojospringboot.exceptions.ExceptionDetails;
import br.com.curso.devdojospringboot.exceptions.ValidationExceptionDetails;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetaills> handleBadRequestException(BadRequestException exception){
        return new ResponseEntity<>(
                BadRequestExceptionDetaills.builder()
                    .timeStamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .title("Bad request exception, check the documentation")
                    .details("Check the field(s) error")
                    .developerMessage(exception.getClass().getName())
                    .build(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

       List<FieldError> fieldErros = ex.getBindingResult().getFieldErrors();
       String fields = fieldErros.stream().map(FieldError::getField).collect(Collectors.joining(","));
       String fieldsMessage = fieldErros.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
        return new ResponseEntity<>(
            ValidationExceptionDetails.builder()
                    .timeStamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .title("Bad request exception, invalid fields")
                    .details(ex.getMessage())
                    .developerMessage(ex.getClass().getName())
                    .fields(fields)
                    .fieldMessage(fieldsMessage)
                    .build(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
            .timeStamp(LocalDateTime.now())
            .status(status.value())
            .title(ex.getCause().getMessage())
            .details(ex.getMessage())
            .developerMessage(ex.getClass().getName())
            .build();
		
		return new ResponseEntity<>(exceptionDetails, headers, status);
	}

}
