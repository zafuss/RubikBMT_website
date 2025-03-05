package zafus.rubikbmt.rubikbmt_website.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zafus.rubikbmt.rubikbmt_website.entities.Schedule;
import zafus.rubikbmt.rubikbmt_website.services.ScheduleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public List<Map<String, Object>> getSchedules() {
        return scheduleService.getAllSchedules().stream().map(schedule -> {
            Map<String, Object> event = new HashMap<>();
            event.put("title", schedule.getSubject() + " - " + schedule.getTeacher());
            event.put("start", schedule.getStartTime());
            event.put("end", schedule.getEndTime());
            return event;
        }).collect(Collectors.toList());
    }
}