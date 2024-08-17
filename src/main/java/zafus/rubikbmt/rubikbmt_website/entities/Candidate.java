package zafus.rubikbmt.rubikbmt_website.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String lastName;
    private String firstName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String gmail;

    @ManyToOne
    private Competition competition;

    private LocalDateTime registrationTime;
    private boolean isConfirmed;

    @ManyToMany
    private List<Event> events;
}