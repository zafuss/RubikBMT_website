package zafus.rubikbmt.rubikbmt_website.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zafus.rubikbmt.rubikbmt_website.services.CandidateService;
import zafus.rubikbmt.rubikbmt_website.services.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AutoCompleteController {
    @Autowired
    private CandidateService candidateService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/candidates/autocomplete")
    public List<String> autocomplete(
            @RequestParam String input,
            @RequestParam String type) {
        switch (type) {
            case "email":
                return candidateService.getEmailSuggestions(input);
            case "phone":
                return candidateService.getPhoneNumberSuggestions(input);
            case "date":
                return candidateService.getLastnameFirstnameSuggestionsbyDateofBirth(input);
            default:
                return  candidateService.getLastnameFirstnameSuggestions(input);
        }
    }

    @GetMapping("/students/autocomplete")
    public List<String> autocompleteStudnet(
            @RequestParam String input,
            @RequestParam String type) {
        switch (type) {
            case "email":
                return studentService.getEmailSuggestions(input);
            case "phone":
                return studentService.getPhoneNumberSuggestions(input);
            default:
                return  studentService.getLastnameFirstnameSuggestions(input);
        }
    }

}
