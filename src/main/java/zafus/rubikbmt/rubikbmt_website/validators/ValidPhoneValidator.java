package zafus.rubikbmt.rubikbmt_website.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import zafus.rubikbmt.rubikbmt_website.services.UserService;
import zafus.rubikbmt.rubikbmt_website.validators.annotations.ValidPhoneNumber;

public class ValidPhoneValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (userService == null)
            return true;
        return userService.findByPhone(phone) == null ;
    }
}
