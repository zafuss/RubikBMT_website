package zafus.rubikbmt.rubikbmt_website.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zafus.rubikbmt.rubikbmt_website.services.CandidateService;

import java.util.List;

@RestController
@RequestMapping("/autocomplete")
public class AutocompleteController {
    @Autowired
    private CandidateService candidateService;

    @GetMapping("/emails")
    public List<String> getEmailSuggestions(@RequestParam String term) {
        return candidateService.getEmailSuggestions(term);
    }

    @GetMapping("/phoneNumbers")
    public List<String> getPhoneNumberSuggestions(@RequestParam String term) {
        return candidateService.getPhoneNumberSuggestions(term);
    }
}
