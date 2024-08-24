package zafus.rubikbmt.rubikbmt_website.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;

import java.time.LocalDate;
import java.util.List;

public interface ICandidateRepository extends JpaRepository<Candidate, String> {

    Page<Candidate> findByEmailContaining(String email, Pageable pageable);

    Page<Candidate> findByPhoneNumberContaining(String phoneNumber, Pageable pageable);

    Page<Candidate> findByFullNameContaining(String fullName, Pageable pageable);

    @Query("SELECT c FROM Candidate c WHERE " +
            "STR(c.dateOfBirth) LIKE %:input% " +
            "AND c.fullName LIKE %:fullname%")
    Page<Candidate> findByDateOfBirthAndFullName(@Param("input") String input, @Param("fullname") String fullname,Pageable pageable);

    List<Candidate> findByEmailContaining(String email);

    List<Candidate> findByPhoneNumberContaining(String phoneNumber);

    @Query("SELECT c FROM Candidate c WHERE " +
            "STR(c.dateOfBirth) LIKE %:input%")
    List<Candidate> findByDateOfBirth(@Param("input") String input);


    List<Candidate> findByLastNameContainingOrFirstNameContaining(String lastName, String firstName);

    @Query("SELECT c FROM Candidate c JOIN c.events e WHERE e.name = :eventName")
    Page<Candidate> findByEventNameContaining(@Param("eventName") String eventName, Pageable pageable);

    Page<Candidate> findAll(Specification<Candidate> spec, Pageable pageable);

    @Query("SELECT c FROM Candidate c WHERE c.isConfirmed = true ORDER BY c.timeConfirmed DESC")
    Page<Candidate> findUnconfirmedCandidates(Pageable pageable, int size);

    Candidate findByEmail(String email);

    Candidate findByPhoneNumber(String phoneNumber);

    @Query("SELECT e.name, COUNT(c) FROM Candidate c JOIN c.events e GROUP BY e.name")
    List<Object[]> countCandidatesByEvent();

    @Query("SELECT c FROM Candidate c JOIN c.events e WHERE e.name = :eventName")
    List<Candidate> findCandidatesByEventName(@Param("eventName") String eventName);


}
