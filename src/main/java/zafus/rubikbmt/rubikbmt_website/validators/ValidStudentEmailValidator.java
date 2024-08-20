package zafus.rubikbmt.rubikbmt_website.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import zafus.rubikbmt.rubikbmt_website.services.CandidateService;
import zafus.rubikbmt.rubikbmt_website.services.StudentService;
import zafus.rubikbmt.rubikbmt_website.validators.annotations.ValidEmail;
import zafus.rubikbmt.rubikbmt_website.validators.annotations.ValidStudentEmail;

public class ValidStudentEmailValidator implements ConstraintValidator<ValidStudentEmail, String> {
    @Autowired
    private StudentService studentService;
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (studentService == null)
            return true;
        return studentService.findByEmail(email) == null;
    }
}
