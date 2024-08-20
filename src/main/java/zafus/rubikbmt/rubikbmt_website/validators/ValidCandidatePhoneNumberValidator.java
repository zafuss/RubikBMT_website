package zafus.rubikbmt.rubikbmt_website.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import zafus.rubikbmt.rubikbmt_website.services.CandidateService;
import zafus.rubikbmt.rubikbmt_website.validators.annotations.ValidCandidatePhoneNumber;

@RequiredArgsConstructor
public class ValidCandidatePhoneNumberValidator implements ConstraintValidator<ValidCandidatePhoneNumber, String> {
    @Autowired
    private CandidateService candidateService;
    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (candidateService == null)
            return true;
        return candidateService.findByPhone(phone) == null;
    }
}
