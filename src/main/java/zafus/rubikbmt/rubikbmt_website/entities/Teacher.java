package zafus.rubikbmt.rubikbmt_website.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "teachers") // Tạo bảng riêng cho Teacher
public class Teacher extends User {
    private String description;
}
