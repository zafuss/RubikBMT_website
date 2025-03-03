package zafus.rubikbmt.rubikbmt_website.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zafus.rubikbmt.rubikbmt_website.entities.*;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateSession;
import zafus.rubikbmt.rubikbmt_website.services.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private CourseService courseService;

//    @Autowired
//    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ClassroomService classroomService;
//    @GetMapping()
//    public String index(Model model,
//                        @RequestParam(defaultValue = "0") int page,
//                        @RequestParam(defaultValue = "10") int size,
//                        @RequestParam(defaultValue = "") String keyword,
//                        @RequestParam(defaultValue = "") String searchType) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Session> userPage = sessionService.searchSession(keyword, searchType, pageable);
//
//        model.addAttribute("sessions", userPage.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", userPage.getTotalPages());
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("size", size);
//
//        return "session/index";
//    }
//    @GetMapping("/{id}")
//    public String getSessionById(@PathVariable String id, Model model) {
//        model.addAttribute("session", sessionService.getSessionById(id));
//        return "session/detail";
//    }

    @GetMapping("/by-teacher/{teacherId}")
    public String getSessionsByTeacher(@PathVariable String teacherId, Model model) {
        model.addAttribute("sessions", sessionService.getSessionsByTeacher(teacherService.getTeacherById(teacherId)));
        return "session/index";
    }

    @GetMapping("/by-classroom")
    public String getSessionsByClassroom(@RequestParam String classroomId, Model model) {
        List<Session> sessions = sessionService
                .getSessionsByClassroom(classroomService.getClassroomById(classroomId))
                .stream()
                .sorted(Comparator.comparing(Session::getStartDate))
                .collect(Collectors.toList());
        model.addAttribute("sessions", sessions);
        model.addAttribute("classroomId", classroomId);
        return "session/index";
    }

    @GetMapping("/schedule-by-classroom")
    public String getSessionsScheduleByClassroom(@RequestParam String classroomId, Model model) {
        model.addAttribute("classroomId", classroomId);
        return "session/classroom-schedule";
    }

    @GetMapping("/addSession")
    public String showCreateForm(Model model, @RequestParam String classroomId) {
        Session session = new Session();

        session.setClassroom(classroomService.getClassroomById(classroomId));
        model.addAttribute("thisSession", session);

        return "session/add";
    }

    @PostMapping("/addSession")
    public String createSession(@ModelAttribute Session session) {
        sessionService.saveSession(session);
        return "redirect:/sessions/by-classroom?classroomId=" + session.getClassroom().getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteSession(@PathVariable String id) {
        sessionService.deleteSession(id);
        return "redirect:/sessions";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model, @RequestParam("callbackUrl") String callbackUrl) {
        Session session = sessionService.getSessionById(id);
        LocalDateTime formattedStartDate = session.getStartDate();
        LocalDateTime formattedEndDate = session.getEndDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        model.addAttribute("session", session);
        model.addAttribute("callbackUrl", callbackUrl);
        model.addAttribute("startDate", formattedStartDate != null ? formattedStartDate.format(formatter) : "");
        model.addAttribute("endDate",formattedEndDate != null ? formattedEndDate.format(formatter) : "");
        return "session/form";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute RequestUpdateSession session,  @RequestParam("callbackUrl") String callbackUrl) {
        sessionService.update(session);
        return callbackUrl ;
    }
}
