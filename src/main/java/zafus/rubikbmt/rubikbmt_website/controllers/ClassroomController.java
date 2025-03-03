package zafus.rubikbmt.rubikbmt_website.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zafus.rubikbmt.rubikbmt_website.entities.*;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateClassroom;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateStudent;
import zafus.rubikbmt.rubikbmt_website.services.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/classrooms")
public class ClassroomController {

    private final ClassroomService classroomService;

    @Autowired
    private CourseService courseService;

//    @Autowired
//    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SessionService sessionService;

    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "") String keyword,
                        @RequestParam(defaultValue = "") String searchType) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Classroom> userPage = classroomService.searchClassroom(keyword, searchType, pageable);

        model.addAttribute("classrooms", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);

        return "classroom/index";
    }
    @GetMapping("/{id}")
    public String getClassroomById(@PathVariable String id, Model model) {
        model.addAttribute("classroom", classroomService.getClassroomById(id));
        return "classroom/detail";
    }

    @GetMapping("/by-teacher/{teacherId}")
    public String getClassroomsByTeacher(@PathVariable String teacherId, Model model) {
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        model.addAttribute("classrooms", classroomService.getClassroomsByTeacher(teacher));
        return "classroom/list";
    }

    @GetMapping("/by-course/{courseId}")
    public String getClassroomsByCourse(@PathVariable String courseId, Model model) {
        Course course = new Course();
        course.setId(courseId);
        model.addAttribute("classrooms", classroomService.getClassroomsByCourse(course));
        return "classroom/list";
    }

    @GetMapping("/addClassroom")
    public String showCreateForm(Model model) {
        model.addAttribute("classroom", new Classroom());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("teachers", teacherService.findAll());
        return "classroom/add";
    }

    @PostMapping("/addClassroom")
    public String createClassroom(@ModelAttribute Classroom classroom, @RequestParam("sessionDates") List<LocalDateTime> sessionDates) {
        classroom.setExpectedEndDate(sessionDates.getLast());
        classroomService.saveClassroom(classroom);

        for (LocalDateTime sessionDate : sessionDates) {
            Session session = new Session();
            session.setClassroom(classroom);
            session.setStartDate(sessionDate);
            int totalMinutes = sessionDate.getMinute() + classroom.getCourse().getMinutesPerSession();
            int extraHours = totalMinutes / 60;  // Lấy số giờ dư
            int finalMinutes = totalMinutes % 60; // Lấy số phút còn lại

            session.setEndDate(LocalDateTime.of(sessionDate.getYear(), sessionDate.getMonth(), sessionDate.getDayOfMonth(),
                    sessionDate.getHour() + extraHours, finalMinutes));
            sessionService.saveSession(session);
        }
        return "redirect:/classrooms";
    }

    @GetMapping("/delete/{id}")
    public String deleteClassroom(@PathVariable String id) {
        classroomService.deleteClassroom(id);
        return "redirect:/classrooms";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") String id, Model model) {
        Classroom classroom = classroomService.getClassroomById(id);
        model.addAttribute("classroom", classroom);
        model.addAttribute("teachers", teacherService.findAll());
        return "classroom/form";
    }
    @PostMapping("/edit")
    public String edit(@ModelAttribute RequestUpdateClassroom classroom) {
        classroomService.update(classroom);
        return "redirect:/classrooms";
    }
}
