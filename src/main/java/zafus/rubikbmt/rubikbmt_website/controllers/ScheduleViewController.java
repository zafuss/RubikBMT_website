package zafus.rubikbmt.rubikbmt_website.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ScheduleViewController {

    @GetMapping("/schedule")
    public String showSchedulePage() {
        return "schedule"; // Tên file HTML trong thư mục templates (classroom-schedule.html)
    }
}