package zafus.rubikbmt.rubikbmt_website.requestEntities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.entities.Competition;
import zafus.rubikbmt.rubikbmt_website.entities.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestUpdateCandidate {
    private String id;
    @Size(min = 1, max = 50, message = "Họ và tên đệm phải có từ 1 đến 50 ký tự")
    private String lastName;
    @Size(min = 1, max = 50, message = " Tên phải có từ 1 đến 50 ký tự")
    private String firstName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Ngày tháng không được bỏ trống.")
    private LocalDate dateOfBirth;
    @Length(min = 10, max = 10, message = "Phone phải có 10 số")
    @Pattern(regexp = "^[0-9]*$", message = "Phone phải là số")
    private String phoneNumber;
    @Size(min = 1, max = 50, message = "Email phải có từ 1 đến 50 ký tự")
    @Email(message = "Không đúng định dạng Email")
    private String email;

    @ManyToOne
    private Competition competition;
    @ManyToMany
    private List<Event> events;

    public static RequestUpdateCandidate fromEntity(Candidate candidate) {
        RequestUpdateCandidate dto = new RequestUpdateCandidate();
        dto.setId(candidate.getId());
        dto.setLastName(candidate.getLastName());
        dto.setFirstName(candidate.getFirstName());
        dto.setDateOfBirth(candidate.getDateOfBirth());
        dto.setPhoneNumber(candidate.getPhoneNumber());
        dto.setEmail(candidate.getEmail());

        // Assuming you want to use IDs for competition and events
        if (candidate.getCompetition() != null) {
            dto.setCompetition(candidate.getCompetition());
        }
        if (candidate.getEvents() != null) {
            dto.setEvents(candidate.getEvents());
        }

        return dto;
    }
    private boolean isConfirmed;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime timeConfirmed;
}
