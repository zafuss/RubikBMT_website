package zafus.rubikbmt.rubikbmt_website.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import zafus.rubikbmt.rubikbmt_website.services.CandidateService;
import zafus.rubikbmt.rubikbmt_website.validators.annotations.ValidCandidateEmail;

@RequiredArgsConstructor
public class ValidCandidateEmailValidator implements ConstraintValidator<ValidCandidateEmail, String> {
    @Autowired
    private CandidateService candidateService;
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (candidateService == null)
            return true;
        return candidateService.findByEmail(email) == null;
    }
}
