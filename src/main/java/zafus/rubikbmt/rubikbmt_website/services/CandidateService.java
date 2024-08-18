package zafus.rubikbmt.rubikbmt_website.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.repositories.ICandidateRepository;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateCandidate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class CandidateService {
    private final ICandidateRepository candidateRepository;
    public List<Candidate> findAll() {
        return candidateRepository.findAll();
    }

    public Candidate findById(String id) {
        return candidateRepository.findById(id).orElse(null);
    }

    public Candidate add(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public Candidate update(Candidate candidate) {
        return candidateRepository.save(candidate);
    }
    public Candidate updateCandidate(RequestUpdateCandidate request) {
        try {
            Candidate existingCandidate = candidateRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Candidate not found"));

            if (request.getLastName() != null && !request.getLastName().isEmpty()) {
                existingCandidate.setLastName(request.getLastName());
            }
            if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
                existingCandidate.setFirstName(request.getFirstName());
            }
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                existingCandidate.setEmail(request.getEmail());
            }
            if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
                existingCandidate.setPhoneNumber(request.getPhoneNumber());
            }
            if (request.getEvents() != null) {
                existingCandidate.setEvents(request.getEvents());
            }
            if (request.getDateOfBirth() != null) {
                existingCandidate.setDateOfBirth(request.getDateOfBirth());
            }

            if (request.getCompetition() != null){
                existingCandidate.setCompetition(request.getCompetition());
            }
            return candidateRepository.save(existingCandidate);
        } catch (Exception ex) {
            throw new RuntimeException("Error updating candidate: " + ex.getMessage());
        }
    }

    public void delete(Candidate candidate) {
        candidateRepository.delete(candidate);
    }

    public Page<Candidate> searchCandidates(String keyword, Pageable pageable) {
        return candidateRepository.findByEmailContainingOrPhoneNumberContaining(keyword, keyword, pageable);
    }
    public List<String> getEmailSuggestions(String email) {
        return candidateRepository.findByEmailContaining(email).stream()
                .map(Candidate::getEmail)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getPhoneNumberSuggestions(String phoneNumber) {
        return candidateRepository.findByPhoneNumberContaining(phoneNumber).stream()
                .map(Candidate::getPhoneNumber)
                .distinct()
                .collect(Collectors.toList());
    }
}
