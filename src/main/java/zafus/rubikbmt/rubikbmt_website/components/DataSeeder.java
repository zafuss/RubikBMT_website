package zafus.rubikbmt.rubikbmt_website.components;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import zafus.rubikbmt.rubikbmt_website.entities.Schedule;
import zafus.rubikbmt.rubikbmt_website.repositories.ScheduleRepository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {
    private final ScheduleRepository scheduleRepository;

    public DataSeeder(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public void run(String... args) {
        if (scheduleRepository.count() == 0) {
            scheduleRepository.saveAll(List.of(
                new Schedule("Toán", "Cô Lan", LocalDateTime.of(2024, 12, 1, 8, 0), LocalDateTime.of(2024, 12, 1, 10, 0), DayOfWeek.MONDAY),
                new Schedule("Vật Lý", "Thầy Minh", LocalDateTime.of(2024, 12, 3, 13, 0), LocalDateTime.of(2024, 12, 3, 15, 0), DayOfWeek.FRIDAY)
            ));
        }
    }
}