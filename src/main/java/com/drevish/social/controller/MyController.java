package com.drevish.social.controller;

import com.drevish.social.util.Operation;
import org.springframework.ui.Model;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;

public abstract class MyController {
    /**
     * Runs operation. If validation exception was occurred, adds error attribute to model and
     * returns true, otherwise returns false.
     *
     * @param model     current web request model
     * @param operation lambda operation to be run
     * @return
     */
    protected boolean causesValidationException(Model model, Operation operation) {
        try {
            operation.run();
            return false;
        } catch (ConstraintViolationException e) {
            Optional<ConstraintViolation<?>> violation = e.getConstraintViolations().stream().findFirst();
            violation.ifPresent(constraintViolation -> model.addAttribute("error", constraintViolation.getMessage()));
            return true;
        }
    }
}
