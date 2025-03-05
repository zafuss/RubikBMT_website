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
import zafus.rubikbmt.rubikbmt_website.entities.User;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestCreateStudent;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateRegisterStudent;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateStudent;
import zafus.rubikbmt.rubikbmt_website.services.RoleService;
import zafus.rubikbmt.rubikbmt_website.services.StudentService;
import zafus.rubikbmt.rubikbmt_website.services.UserService;
import java.util.HashSet;
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
        model.addAttribute("student", new RequestCreateStudent());
        return "student/add";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") String id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        return "student/form";
    }

    @PostMapping("/addStudent")
    public String add(@ModelAttribute RequestCreateStudent student, Model model, @RequestParam(value = "confirm", required = false) Boolean confirm) {
        User existingUser = userService.findByEmail(student.getEmail());
        if (existingUser == null) {
            existingUser = userService.findByPhone(student.getPhoneNumber());
        }

        if (existingUser != null) {
            boolean isStudent = existingUser.getRoles().stream()
                    .anyMatch(role -> "Student".equalsIgnoreCase(role.getRoleName()));

            if (isStudent) {
                model.addAttribute("error", "Người dùng này đã là học viên.");
                model.addAttribute("student", student);
                return "student/add";
            }

            // Nếu chưa xác nhận thì hiển thị thông tin user để xác nhận
            if (confirm == null || !confirm) {
                model.addAttribute("existingUser", existingUser);
                model.addAttribute("student", student);
                model.addAttribute("showConfirmation", true);
                return "student/add";
            }

            // Thêm vai trò Student cho User
            Role studentRole = roleService.findById("STUDENT");

            // Tạo một bản sao mới của Set<Role> và thêm studentRole
            Set<Role> updatedRoles = new HashSet<>(existingUser.getRoles());
            updatedRoles.add(studentRole);

            // Cập nhật lại danh sách roles
            existingUser.setRoles(updatedRoles);

            // Lưu lại user đã cập nhật
            userService.saveDefaultUser(existingUser);
            // Tạo mới Student từ User đã có
            studentService.saveStudentFromUser(existingUser, student, updatedRoles );

            return "redirect:/students";
        }

        // Nếu User chưa tồn tại, thêm mới User và Student
        List<Role> roles = roleService.findAll();
        Set<Role> selectedRoles = roles.stream()
                .filter(r -> "Student".equals(r.getRoleName()))
                .collect(Collectors.toSet());

        studentService.saveStudent(student, selectedRoles);
        return "redirect:/students";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute RequestUpdateStudent user) {
        studentService.update(user);
        return "redirect:/students/detail?id=" + user.getId();
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("id") String id, Model model) {
        Student user = studentService.getStudentById(id);
        model.addAttribute("user", user);
        return "student/detail";
    }
}