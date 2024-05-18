package io.knifer.moghostwebui.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * 路径校验器
 *
 * @author Knifer
 * @version 1.0.0
 */
public class PathValidator implements ConstraintValidator<Path, String> {

    private static final Pattern PATTERN = Pattern.compile(
            "(/.*|[a-zA-Z]\\\\(([^<>:\"/\\\\|?*!]*[^<>:\"/\\\\|?*.!]\\\\|..\\\\)*([^<>:\"/\\\\|?*.!]*[^<>:\"/\\\\|?*.!]\\\\?|..\\\\))?)"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || PATTERN.matcher(value).matches();
    }
}
