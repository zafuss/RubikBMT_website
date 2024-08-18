package zafus.rubikbmt.rubikbmt_website.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zafus.rubikbmt.rubikbmt_website.services.CandidateService;

import java.util.List;

@RestController
@RequestMapping("")
public class AutoCompleteController {
    @Autowired
    private CandidateService candidateService;

    @GetMapping("/api/candidates/autocomplete")
    public List<String> autocomplete(
            @RequestParam String input,
            @RequestParam String type) {
        switch (type) {
            case "email":
                return candidateService.getEmailSuggestions(input);
            case "phone":
                return candidateService.getPhoneNumberSuggestions(input);
            default:
                return  candidateService.getLastnameFirstnameSuggestions(input);
        }
    }
}
