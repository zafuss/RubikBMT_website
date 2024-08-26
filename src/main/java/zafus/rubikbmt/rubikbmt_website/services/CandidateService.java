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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class CandidateService {
    private final ICandidateRepository candidateRepository;
    private  String outputDate;

    public static String formatDate(String inputDate) {
        SimpleDateFormat inputFormat;
        SimpleDateFormat outputFormat;
        inputDate = inputDate.replace("-", "/");
        try {
            switch (inputDate.length()) {
                case 2: // dd hoặc MM hoặc yyyy
                case 4: // yyyy
                    return inputDate; // Không cần chuyển đổi

                case 5: // dd/MM
                    inputFormat = new SimpleDateFormat("dd/MM");
                    outputFormat = new SimpleDateFormat("MM-dd");
                    break;

                case 10: // dd/MM/yyyy
                    inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    break;

                default:
                    throw new IllegalArgumentException("Định dạng ngày không hợp lệ");
            }

            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Lỗi phân tích ngày";
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
            existingCandidate.setNote(request.getNote());
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
            case "date":
                return candidateRepository.findByDateOfBirthAndFullName(outputDate,keyword,pageable);
            case "name":
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
        outputDate = formatDate(dateOfBirth);
        return candidateRepository.findByDateOfBirth(outputDate).stream()
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
