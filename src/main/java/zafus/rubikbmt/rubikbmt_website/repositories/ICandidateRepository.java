package zafus.rubikbmt.rubikbmt_website.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import java.util.List;
public interface ICandidateRepository extends JpaRepository<Candidate, String> {

    Page<Candidate> findByEmailContaining(String email, Pageable pageable);

    Page<Candidate> findByPhoneNumberContaining(String phoneNumber, Pageable pageable);

    Page<Candidate> findByFullNameContaining(String fullName, Pageable pageable);

    List<Candidate> findByEmailContaining(String email);

    List<Candidate> findByPhoneNumberContaining(String phoneNumber);

    List<Candidate> findByLastNameContainingOrFirstNameContaining(String lastName, String firstName);
    Page<Candidate> findAll(Specification<Candidate> spec, Pageable pageable);
    @Query("SELECT c FROM Candidate c WHERE c.isConfirmed = true ORDER BY c.timeConfirmed DESC")
    Page<Candidate> findUnconfirmedCandidates(Pageable pageable,int size);
    Candidate findByEmail(String email);
    Candidate findByPhoneNumber(String phoneNumber);

}
