package zafus.rubikbmt.rubikbmt_website.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import zafus.rubikbmt.rubikbmt_website.services.UserService;
import zafus.rubikbmt.rubikbmt_website.validators.annotations.ValidEmail;


@RequiredArgsConstructor
public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {
    @Autowired
    private UserService userService;
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (userService == null)
            return true;
        return userService.findByEmail(email) == null ;
    }
}