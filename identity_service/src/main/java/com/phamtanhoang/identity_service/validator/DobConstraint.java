package com.phamtanhoang.identity_service.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;


@Target({FIELD})// target for validation
@Retention(RetentionPolicy.RUNTIME) //when handle validation
@Constraint(
    validatedBy = {DobValidator.class}
)
public @interface DobConstraint {
  String message() default "Invalid date of birth";

  int min();

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
