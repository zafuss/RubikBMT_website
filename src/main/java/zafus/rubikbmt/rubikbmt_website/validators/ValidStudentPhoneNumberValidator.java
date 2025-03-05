package zafus.rubikbmt.rubikbmt_website.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import zafus.rubikbmt.rubikbmt_website.services.RegisterStudentService;
import zafus.rubikbmt.rubikbmt_website.validators.annotations.ValidStudentPhoneNumber;

public class ValidStudentPhoneNumberValidator implements ConstraintValidator<ValidStudentPhoneNumber, String> {
    @Autowired
    private RegisterStudentService registerStudentService;
    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (registerStudentService == null)
            return true;
        return registerStudentService.findByPhone(phone) == null;
    }
}
