package zafus.rubikbmt.rubikbmt_website.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subject;       // Môn học (Toán, Lý, Hóa,...)
    private String teacher;       // Giáo viên (Cô Lan, Thầy Minh,...)
    private LocalDateTime startTime; // Ngày & Giờ bắt đầu
    private LocalDateTime endTime;   // Ngày & Giờ kết thúc
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    public Schedule(String subject, String teacher
            , LocalDateTime startTime, LocalDateTime endTime, DayOfWeek dayOfWeek) {
        this.subject = subject;
        this.teacher = teacher;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
    }
}