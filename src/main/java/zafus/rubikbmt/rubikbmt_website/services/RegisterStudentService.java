package zafus.rubikbmt.rubikbmt_website.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zafus.rubikbmt.rubikbmt_website.entities.RegisterStudent;
import zafus.rubikbmt.rubikbmt_website.repositories.IRegisterStudentRepository;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateRegisterStudent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class RegisterStudentService {
    private final IRegisterStudentRepository studentRepository;
    public List<RegisterStudent> findAll() {
        return studentRepository.findAll();
    }

    public RegisterStudent findById(String id) {
        return studentRepository.findById(id).orElse(null);
    }

    public RegisterStudent add(RegisterStudent registerStudent) {
        registerStudent.setFullName();
        return studentRepository.save(registerStudent);
    }

    public RegisterStudent update(RegisterStudent registerStudent) {
        return studentRepository.save(registerStudent);
    }
    public RegisterStudent updateStudent(RequestUpdateRegisterStudent request) {
        try {
            RegisterStudent existingRegisterStudent = studentRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            if (request.getLastName() != null && !request.getLastName().isEmpty()) {
                existingRegisterStudent.setLastName(request.getLastName());
            }
            if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
                existingRegisterStudent.setFirstName(request.getFirstName());
            }
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                existingRegisterStudent.setEmail(request.getEmail());
            }
            if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
                existingRegisterStudent.setPhoneNumber(request.getPhoneNumber());
            }
            if (request.getDateOfBirth() != null) {
                existingRegisterStudent.setDateOfBirth(request.getDateOfBirth());
            }
            existingRegisterStudent.setParentName(request.getParentName());
            existingRegisterStudent.setNote(request.getNote());
            existingRegisterStudent.setConfirmationDate(request.isConfirmed() ?
                    (existingRegisterStudent.getConfirmationDate() != null ? existingRegisterStudent.getConfirmationDate() : LocalDateTime.now()) : null);
            return studentRepository.save(existingRegisterStudent);
        } catch (Exception ex) {
            throw new RuntimeException("Error updating student: " + ex.getMessage());
        }
    }

    public void delete(RegisterStudent registerStudent) {
        studentRepository.delete(registerStudent);
    }

    public Page<RegisterStudent> searchStudents(String keyword, String searchType, Pageable pageable) {
        switch (searchType) {
            case "email":
                return studentRepository.findByEmailContaining(keyword, pageable);
            case "phone":
                return studentRepository.findByPhoneNumberContaining(keyword, pageable);
            case "name":
                return studentRepository.findByFullNameContaining(keyword, pageable);
            default:
                return studentRepository.findAll(pageable);
        }
    }
    public List<String> getEmailSuggestions(String email) {
        return studentRepository.findByEmailContaining(email).stream()
                .map(RegisterStudent::getEmail)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getPhoneNumberSuggestions(String phoneNumber) {
        return studentRepository.findByPhoneNumberContaining(phoneNumber).stream()
                .map(RegisterStudent::getPhoneNumber)
                .distinct()
                .collect(Collectors.toList());
    }
    public List<String> getLastnameFirstnameSuggestions(String input) {
        return studentRepository.findByLastNameContainingOrFirstNameContaining(input, input).stream()
                .map(student -> student.getFirstName() + " " +  student.getLastName() )
                .distinct()
                .collect(Collectors.toList());
    }

    public RegisterStudent findByPhone(String phone) {
        return studentRepository.findByPhoneNumber(phone);
    }

    public RegisterStudent findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
}
