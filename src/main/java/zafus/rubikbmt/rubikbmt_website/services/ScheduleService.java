package zafus.rubikbmt.rubikbmt_website.services;

import org.springframework.stereotype.Service;
import zafus.rubikbmt.rubikbmt_website.entities.Schedule;
import zafus.rubikbmt.rubikbmt_website.repositories.ScheduleRepository;

import java.time.DayOfWeek;
import java.util.List;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> getSchedulesByDay(DayOfWeek dayOfWeek) {
        return scheduleRepository.findByDayOfWeek(String.valueOf(dayOfWeek));
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
}