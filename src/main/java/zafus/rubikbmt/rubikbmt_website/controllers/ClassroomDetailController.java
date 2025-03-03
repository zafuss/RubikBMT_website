package zafus.rubikbmt.rubikbmt_website.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zafus.rubikbmt.rubikbmt_website.entities.*;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateClassroomDetail;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateStudent;
import zafus.rubikbmt.rubikbmt_website.services.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/classroomDetails")
public class ClassroomDetailController {

    @Autowired
    private ClassroomDetailService classroomDetailService;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private StudentService studentService;

    public ClassroomDetailController(ClassroomDetailService classroomDetailService) {
        this.classroomDetailService = classroomDetailService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "") String keyword,
                        @RequestParam(defaultValue = "") String searchType) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ClassroomDetail> userPage = classroomDetailService.searchClassroomDetail(keyword, searchType, pageable);

        model.addAttribute("classroomDetails", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);

        return "classroomDetail/index";
    }

    @GetMapping("/{id}")
    public String getClassroomDetailById(@PathVariable String id, Model model) {
        model.addAttribute("classroomDetail", classroomDetailService.getClassroomDetailById(id));
        return "classroomDetail/detail";
    }

    @GetMapping("/by-classroom")
    public String getClassroomDetailsByClassroom(@RequestParam String classroomId, Model model) {
        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        model.addAttribute("classroomDetails", classroomDetailService.getClassroomDetailsByClassroom(classroom));
        model.addAttribute("currentClassroom", classroomService.getClassroomById(classroomId));
        return "classroomDetail/by-classroom";
    }

    @GetMapping("/by-student")
    public String getClassroomDetailsByStudent(@RequestParam String studentId, Model model) {
        Student student = new Student();
        student.setId(studentId);
        model.addAttribute("classroomDetails", classroomDetailService.getClassroomDetailsByStudent(student));
        model.addAttribute("currentStudent", studentService.getStudentById(studentId));
        return "classroomDetail/by-student";
    }

    @GetMapping("/addClassroomDetail")
    public String showCreateForm(@RequestParam(required = false) String classroomId,
                                 @RequestParam(required = false) String studentId,
                                 Model model) {

        model.addAttribute("classrooms", classroomService.getAllClassrooms());
        model.addAttribute("students", studentService.getAllStudents());

        if (classroomId != null) {
            ClassroomDetail classroomDetail = new ClassroomDetail();
            classroomDetail.setClassroom(classroomService.getClassroomById(classroomId));
            model.addAttribute("classroomDetail", classroomDetail);
        }
        else if (studentId != null) {
            ClassroomDetail classroomDetail = new ClassroomDetail();
            classroomDetail.setStudent(studentService.getStudentById(studentId));
            model.addAttribute("classroomDetail", classroomDetail);
        } else {
            model.addAttribute("classroomDetail", new ClassroomDetail());
        }
        return "classroomDetail/add";
    }

    @PostMapping("/addClassroomDetail")
    public String createClassroomDetail(@ModelAttribute ClassroomDetail classroomDetail) {
        classroomDetailService.saveClassroomDetail(classroomDetail);
        return "redirect:/classroomDetails";
    }

    @GetMapping("/delete/{id}")
    public String deleteClassroomDetail(@PathVariable String id) {
        classroomDetailService.deleteClassroomDetail(id);
        return "redirect:/classroomDetails";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") String id, Model model, @RequestParam(value = "callbackUrl", required = false) String callbackUrl) {
        ClassroomDetail classroomDetail = classroomDetailService.getClassroomDetailById(id);
        model.addAttribute("classroomDetail", classroomDetail);
        model.addAttribute("callbackUrl", callbackUrl);
        model.addAttribute("classrooms", classroomService.getAllClassrooms());
        model.addAttribute("students", studentService.getAllStudents());
        return "classroomDetail/form";
    }
    @PostMapping("/edit")
    public String edit(@ModelAttribute RequestUpdateClassroomDetail classroomDetail, @RequestParam(value = "callbackUrl", required = false) String callbackUrl) {
        classroomDetailService.update(classroomDetail);
        if (!callbackUrl.isEmpty()) {
            return "redirect:" + callbackUrl;
        }
        return "redirect:/classroomDetails";
    }
}
