package com.drevish.social.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

public interface ValidationExceptionHandling {
    @ExceptionHandler(ConstraintViolationException.class)
    default void validationError(ConstraintViolationException e, Model model) {
        Optional<ConstraintViolation<?>> violation = e.getConstraintViolations().stream().findFirst();
        violation.ifPresent(constraintViolation -> model.addAttribute("error", constraintViolation.getMessage()));
    }
}
