package zafus.rubikbmt.rubikbmt_website.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import zafus.rubikbmt.rubikbmt_website.entities.*;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateStudent;
import zafus.rubikbmt.rubikbmt_website.services.LearningTypeService;
import zafus.rubikbmt.rubikbmt_website.services.MentorService;
import zafus.rubikbmt.rubikbmt_website.services.StudentService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/registerStudents")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private MentorService mentorService;
    @Autowired
    private LearningTypeService learningTypeService;
    private List<Mentor> mentors;
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("student", new RegisterStudent());
        return "home/index";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterStudent registerStudent, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            List<LearningType> learningTypes = learningTypeService.findAll();
            model.addAttribute("errors", errors);
            model.addAttribute("learningTypes", learningTypes);
            return "home/index";
        }
        registerStudent.setRegistrationDate(LocalDateTime.now());
        registerStudent.setConfirmationDate(null);
        studentService.add(registerStudent);
        return "redirect:/?success";
    }

    @GetMapping("")
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "") String keyword,
                        @RequestParam(defaultValue = "") String searchType) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RegisterStudent> candidatePage = studentService.searchStudents(keyword, searchType, pageable);
        model.addAttribute("registerStudents", candidatePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", candidatePage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);
        return "student/index";
    }


    @GetMapping("/detail")
    public String viewStudentDetails(@RequestParam("id") String id, Model model) {
        RegisterStudent registerStudent = studentService.findById(id);
        model.addAttribute("student", registerStudent);
        return "student/detail";
    }

    @GetMapping("/edit")
    public String editStudent(@RequestParam("id") String id, Model model) {
        RegisterStudent registerStudent = studentService.findById(id);
        mentors = mentorService.findAll();
        boolean isConfirmed = registerStudent.getConfirmationDate() != null;
        model.addAttribute("student", registerStudent);
        model.addAttribute("mentors", mentors);
        model.addAttribute("isConfirmed",isConfirmed);
        return "student/edit";
    }

    @PostMapping("/updateStudent")
    public String updateStudent(@ModelAttribute("student") @Valid RequestUpdateStudent student,
                                BindingResult bindingResult,
                                @RequestParam("isConfirmed") boolean isConfirmed,  Model model ) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            model.addAttribute("isConfirmed",isConfirmed);
            model.addAttribute("mentors", mentors);
            return "student/edit";
        }
        if (isConfirmed) {
            student.setConfirmed(true);
        } else {
            student.setConfirmed(false);
        }
        studentService.updateStudent(student);
        return "redirect:/students/detail?id=" + student.getId();
    }

}
