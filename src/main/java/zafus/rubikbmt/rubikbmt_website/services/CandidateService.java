package zafus.rubikbmt.rubikbmt_website.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.entities.User;
import zafus.rubikbmt.rubikbmt_website.repositories.ICandidateRepository;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateCandidate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class CandidateService {
    private final ICandidateRepository candidateRepository;

    private String normalizeDateInput(String input) {
        input = input.trim();

        String[] possibleFormats = {"dd-MM-yyyy", "dd/MM/yyyy", "yyyy-MM-dd"};
        for (String format : possibleFormats) {
            Optional<String> normalized = convertDateString(input, format);
            if (normalized.isPresent()) {
                return normalized.get();
            }
        }

        return input;
    }

    private Optional<String> convertDateString(String dateString, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate date = LocalDate.parse(dateString, formatter);
            return Optional.of(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
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
            existingCandidate.setFullName();
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
            if ( request.isConfirmed()){
                existingCandidate.setConfirmed(true);
                existingCandidate.setTimeConfirmed(LocalDateTime.now());
            }else{
                existingCandidate.setConfirmed(false);
                existingCandidate.setTimeConfirmed(null);
            }
            return candidateRepository.save(existingCandidate);
        } catch (Exception ex) {
            throw new RuntimeException("Error updating candidate: " + ex.getMessage());
        }
    }

    public void delete(Candidate candidate) {
        candidateRepository.delete(candidate);
    }

    public List<Object[]> getCountCandidatesByEvent() {
        return candidateRepository.countCandidatesByEvent();
    }

    public Page<Candidate> searchCandidates(String keyword, String searchType, Pageable pageable) {
        switch (searchType) {
            case "email":
                return candidateRepository.findByEmailContaining(keyword, pageable);
            case "phone":
                return candidateRepository.findByPhoneNumberContaining(keyword, pageable);
            case "name":
            case "date":
                return candidateRepository.findByFullNameContaining(keyword, pageable);
            case "event":
                return candidateRepository.findByEventNameContaining(keyword, pageable);
            default:
                return candidateRepository.findAll(pageable);
        }
    }
    public List<Candidate> getCandidatesByEventName(String eventName) {
        return candidateRepository.findCandidatesByEventName(eventName);
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
    public List<String> getLastnameFirstnameSuggestions(String input) {
        return candidateRepository.findByLastNameContainingOrFirstNameContaining(input, input).stream()
                .map(candidate -> candidate.getLastName() + " " +  candidate.getFirstName() )
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getLastnameFirstnameSuggestionsbyDateofBirth(String dateOfBirth) {
        String normalizedInput = normalizeDateInput(dateOfBirth);
        return candidateRepository.findByDateOfBirth(normalizedInput).stream()
                .map(candidate -> candidate.getLastName() + " " +  candidate.getFirstName() )
                .distinct()
                .collect(Collectors.toList());
    }
    public Candidate findByPhone(String phone) {
        return candidateRepository.findByPhoneNumber(phone);
    }

    public Candidate findByEmail(String email) {
        return candidateRepository.findByEmail(email);
    }

    public Page<Candidate> findUnconfirmedCandidates(Pageable pageable, int size) {
        return candidateRepository.findUnconfirmedCandidates(pageable, size);
    }
}
