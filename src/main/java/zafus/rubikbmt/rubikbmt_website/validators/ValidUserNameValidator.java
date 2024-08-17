package zafus.rubikbmt.rubikbmt_website.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import zafus.rubikbmt.rubikbmt_website.services.UserService;
import zafus.rubikbmt.rubikbmt_website.validators.annotations.ValidUserName;

@RequiredArgsConstructor
public class ValidUserNameValidator implements ConstraintValidator<ValidUserName, String> {
    @Autowired
    private UserService userService;
    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (userService == null)
            return true;
        return userService.findByUserName(username) == null ;
    }
}