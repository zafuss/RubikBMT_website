package zafus.rubikbmt.rubikbmt_website.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import zafus.rubikbmt.rubikbmt_website.services.RegisterStudentService;
import zafus.rubikbmt.rubikbmt_website.validators.annotations.ValidStudentEmail;

public class ValidStudentEmailValidator implements ConstraintValidator<ValidStudentEmail, String> {
    @Autowired
    private RegisterStudentService registerStudentService;
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (registerStudentService == null)
            return true;
        return registerStudentService.findByEmail(email) == null;
    }
}
