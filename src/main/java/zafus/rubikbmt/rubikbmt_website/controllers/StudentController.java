package zafus.rubikbmt.rubikbmt_website.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zafus.rubikbmt.rubikbmt_website.entities.Role;
import zafus.rubikbmt.rubikbmt_website.entities.Student;
import zafus.rubikbmt.rubikbmt_website.entities.Teacher;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestCreateStudent;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestCreateTeacher;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestCreateUser;
import zafus.rubikbmt.rubikbmt_website.services.RoleService;
import zafus.rubikbmt.rubikbmt_website.services.StudentService;
import zafus.rubikbmt.rubikbmt_website.services.TeacherService;
import zafus.rubikbmt.rubikbmt_website.services.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private RoleService roleService;
    @GetMapping()
    public String index(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "") String keyword,
                               @RequestParam(defaultValue = "") String searchType) {

        Pageable pageable = PageRequest.of(page, size);
//        Pageable pageable = PageRequest.of(page, size, Sort.by(
//                Sort.Order.asc("isConfirmed"),
//                Sort.Order.desc("registrationTime")
//        ));
        Page<Student> userPage = studentService.searchStudents(keyword, searchType, pageable);
        model.addAttribute("students", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);
        return "student/index";
    }

    @GetMapping("/addStudent")
    public String add(Model model) {
        model.addAttribute("student", new RequestCreateTeacher());
        return "student/add";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") String id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        return "student/form";
    }

    @PostMapping("/addStudent")
    public String add(@ModelAttribute RequestCreateStudent student) {
        List<Role> roles = roleService.findAll();
        Set<Role> selectedRoles = roles.stream()
                .filter(r -> "Student".equals(r.getRoleName())).collect(Collectors.toSet());

        studentService.saveStudent(student, selectedRoles);
        return "redirect:/students";
    }
}
