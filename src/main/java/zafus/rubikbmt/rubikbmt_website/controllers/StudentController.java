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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zafus.rubikbmt.rubikbmt_website.entities.Student;
import zafus.rubikbmt.rubikbmt_website.entities.Competition;
import zafus.rubikbmt.rubikbmt_website.entities.Event;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateStudent;
import zafus.rubikbmt.rubikbmt_website.services.StudentService;
import zafus.rubikbmt.rubikbmt_website.services.CompetitionService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute Student student, BindingResult bindingResult, Model model) {
        student.setConfirmed(false);
        studentService.add(student);
        return "redirect:/?success";
    }

    @GetMapping("")
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "") String keyword) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Student> studentPage = studentService.searchStudents(keyword, pageable);
        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);
        return "student/index";
    }
    @GetMapping("/edit")
    public String editStudent(@RequestParam("id") String id, Model model) {
        Student student = studentService.findById(id);
        model.addAttribute("student", student);
        return "student/edit";
    }

    @GetMapping("/detail")
    public String viewStudentDetails(@RequestParam("id") String id, Model model) {
        Student student = studentService.findById(id);
        model.addAttribute("student", student);
        return "student/detail";
    }

    @PostMapping("/updateStudent")
    public String updateStudent(@ModelAttribute RequestUpdateStudent student) {
        studentService.updateStudent(student);
        return "redirect:/students/detail?id=" + student.getId();
    }

}
