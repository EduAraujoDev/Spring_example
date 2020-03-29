package br.com.alura.forum.validator.dto;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorOutputDto {

    private List<FieldErrorOutputDto> fieldErrors = new ArrayList<>();

    public void addFieldError(String field, String message) {
        FieldErrorOutputDto fieldError = new FieldErrorOutputDto(field, message);
        fieldErrors.add(fieldError);
    }

    public List<FieldErrorOutputDto> getErrors() {
        return fieldErrors;
    }

    public int getNumberOfErrors() {
        return fieldErrors.size();
    }
}