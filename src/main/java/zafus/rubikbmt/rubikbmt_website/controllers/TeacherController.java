package zafus.rubikbmt.rubikbmt_website.controllers;


import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zafus.rubikbmt.rubikbmt_website.entities.Role;
import zafus.rubikbmt.rubikbmt_website.entities.Student;
import zafus.rubikbmt.rubikbmt_website.entities.Teacher;
import zafus.rubikbmt.rubikbmt_website.entities.User;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestCreateTeacher;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestCreateUser;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateStudent;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateTeacher;
import zafus.rubikbmt.rubikbmt_website.services.RoleService;
import zafus.rubikbmt.rubikbmt_website.services.TeacherService;
import zafus.rubikbmt.rubikbmt_website.services.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
    @Autowired
    private UserService userService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private RoleService roleService;
    @GetMapping()
    public String teacherIndex(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "") String keyword,
                               @RequestParam(defaultValue = "") String searchType) {

        Pageable pageable = PageRequest.of(page, size);
//        Pageable pageable = PageRequest.of(page, size, Sort.by(
//                Sort.Order.asc("isConfirmed"),
//                Sort.Order.desc("registrationTime")
//        ));
        Page<Teacher> userPage = teacherService.searchTeachers(keyword, searchType, pageable);
        model.addAttribute("teachers", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);
        return "teacher/index";
    }

    @GetMapping("/addTeacher")
    public String showFormAddTeacher(Model model) {
        model.addAttribute("teacher", new RequestCreateTeacher());
        return "teacher/add";
    }

    @GetMapping("/edit")
    public String editTeacherForm(@RequestParam("id") String id, Model model) {
        model.addAttribute("teacher", teacherService.getTeacherById(id));
        return "teacher/form";
    }


    @PostMapping("/addTeacher")
    public String addTeacher(@ModelAttribute RequestCreateTeacher teacher,Model model, @RequestParam(value = "confirm", required = false) Boolean confirm) {
        User existingUser = userService.findByEmail(teacher.getEmail());
        if (existingUser == null) {
            existingUser = userService.findByPhone(teacher.getPhoneNumber());
        }
        if (existingUser != null) {
            boolean isTeacher = existingUser.getRoles().stream()
                    .anyMatch(role -> "Teacher".equalsIgnoreCase(role.getRoleName()));

            if (isTeacher) {
                model.addAttribute("error", "Người dùng này đã là học viên.");
                model.addAttribute("teacher", teacher);
                return "teacher/add";
            }

            // Nếu chưa xác nhận thì hiển thị thông tin user để xác nhận
            if (confirm == null || !confirm) {
                model.addAttribute("existingUser", existingUser);
                model.addAttribute("teacher", teacher);
                model.addAttribute("showConfirmation", true);
                return "teacher/add";
            }

            // Thêm vai trò Student cho User
            Role teacherRole = roleService.findById("TEACHER");

            // Tạo một bản sao mới của Set<Role> và thêm studentRole
            Set<Role> updatedRoles = new HashSet<>(existingUser.getRoles());
            updatedRoles.add(teacherRole);

            // Cập nhật lại danh sách roles
            existingUser.setRoles(updatedRoles);

            // Lưu lại user đã cập nhật
            userService.saveDefaultUser(existingUser);
            // Tạo mới Student từ User đã có
            teacherService.saveTeacherFromUser(existingUser, teacher, updatedRoles );

            return "redirect:/teachers";
        }
        List<Role> roles = roleService.findAll();
        Set<Role> selectedRoles = roles.stream()
                .filter(r -> "Teacher".equals(r.getRoleName())).collect(Collectors.toSet());

        teacherService.saveTeacher(teacher, selectedRoles);
        return "redirect:/teachers";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute RequestUpdateTeacher user) {
        teacherService.update(user);
        return "redirect:/teachers/detail?id=" + user.getId();
    }
    @GetMapping("/detail")
    public String detail(@RequestParam("id") String id, Model model) {
        Teacher user = teacherService.getTeacherById(id);
        model.addAttribute("user", user);
        return "teacher/detail";
    }
}
