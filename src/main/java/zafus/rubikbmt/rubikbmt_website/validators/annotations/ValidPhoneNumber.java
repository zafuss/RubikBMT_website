package zafus.rubikbmt.rubikbmt_website.validators.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import zafus.rubikbmt.rubikbmt_website.validators.ValidPhoneValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidPhoneValidator.class)
@Documented

public @interface ValidPhoneNumber {
    String message() default "Số điện thoại đã tồn tại";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
