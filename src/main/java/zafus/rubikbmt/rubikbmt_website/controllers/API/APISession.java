package zafus.rubikbmt.rubikbmt_website.controllers.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zafus.rubikbmt.rubikbmt_website.entities.Classroom;
import zafus.rubikbmt.rubikbmt_website.services.ClassroomService;
import zafus.rubikbmt.rubikbmt_website.services.SessionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sessions")
public class APISession {

    private final SessionService sessionService;

    @Autowired
    private ClassroomService classroomService;
    public APISession(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public List<Map<String, Object>> getSessions() {
        return sessionService.getAllSessions().stream().map(session -> {
            Map<String, Object> event = new HashMap<>();
            String title = session.getClassroom().getName() + '-' + session.getClassroom().getCourse().getName();
            event.put("id", session.getId());
            event.put("title", title);
            event.put("start", session.getStartDate());
            event.put("end", session.getEndDate());
            return event;
        }).collect(Collectors.toList());
    }

    @GetMapping("/by-classroom")
    public List<Map<String, Object>> getSessionsByClassroom(@RequestParam String classroomId) {
        Classroom classroom = classroomService.getClassroomById(classroomId);
        return sessionService.getSessionsByClassroom(classroom).stream().map(session -> {
            Map<String, Object> event = new HashMap<>();
            String title = session.getClassroom().getName();
            event.put("id", session.getId());
            event.put("title", title);

            Map<String, String> extendedProps = new HashMap<>();
            extendedProps.put("teacher", session.getClassroom().getTeacher().getFullName());
            extendedProps.put("description", session.getClassroom().getDescription());
            extendedProps.put("course", session.getClassroom().getCourse().getName());

            event.put("extendedProps", extendedProps);

            event.put("start", session.getStartDate());
            event.put("end", session.getEndDate());
            event.put("url", "/sessions/edit/" + session.getId() + "?callbackUrl=redirect:/sessions/schedule-by-classroom?classroomId=" + classroomId);
            return event;
        }).collect(Collectors.toList());
    }
}