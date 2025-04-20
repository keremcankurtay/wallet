package com.kurtay.wallet.util;

import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtility {
    public static void validate(Object o) {
        try (var validator = Validation.buildDefaultValidatorFactory()) {
            validator.getValidator().validate(o).stream().findFirst().ifPresent(violation -> {
                throw new ValidationException(violation.getMessage());
            });
        }
    }
}
