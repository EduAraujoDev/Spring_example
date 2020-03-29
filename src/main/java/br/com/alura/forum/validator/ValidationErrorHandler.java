package br.com.alura.forum.validator;

import br.com.alura.forum.validator.dto.ValidationErrorOutputDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ValidationErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorOutputDto handleValidationError(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        ValidationErrorOutputDto validationErrors = new ValidationErrorOutputDto();

        fieldErrors.forEach(error -> {
            validationErrors.addFieldError(error.getField(), error.getDefaultMessage());
        });

        return validationErrors;
    }
}