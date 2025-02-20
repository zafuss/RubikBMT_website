package zafus.rubikbmt.rubikbmt_website.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    private Student student;
    @ManyToOne
    private Course course;
    @ManyToOne
    private Teacher teacher;
    private long fee;
    private long paid;
    private int studied;
    private Date startDate;
    private Date expectedEndDate;
    private Date actualEndDate;
}
