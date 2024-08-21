package zafus.rubikbmt.rubikbmt_website.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zafus.rubikbmt.rubikbmt_website.entities.Candidate;
import zafus.rubikbmt.rubikbmt_website.entities.Student;
import zafus.rubikbmt.rubikbmt_website.repositories.IStudentRepository;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateStudent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class StudentService {
    private final IStudentRepository studentRepository;
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(String id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student add(Student student) {
        student.setFullName();
        return studentRepository.save(student);
    }

    public Student update(Student student) {
        return studentRepository.save(student);
    }
    public Student updateStudent(RequestUpdateStudent request) {
        try {
            Student existingStudent = studentRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            if (request.getLastName() != null && !request.getLastName().isEmpty()) {
                existingStudent.setLastName(request.getLastName());
            }
            if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
                existingStudent.setFirstName(request.getFirstName());
            }
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                existingStudent.setEmail(request.getEmail());
            }
            if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
                existingStudent.setPhoneNumber(request.getPhoneNumber());
            }
            if (request.getDateOfBirth() != null) {
                existingStudent.setDateOfBirth(request.getDateOfBirth());
            }
            existingStudent.setParentName(request.getParentName());
            existingStudent.setNote(request.getNote());
            existingStudent.setConfirmationDate(request.isConfirmed() ?
                    (existingStudent.getConfirmationDate() != null ? existingStudent.getConfirmationDate() : LocalDateTime.now()) : null);
            return studentRepository.save(existingStudent);
        } catch (Exception ex) {
            throw new RuntimeException("Error updating student: " + ex.getMessage());
        }
    }

    public void delete(Student student) {
        studentRepository.delete(student);
    }

    public Page<Student> searchStudents(String keyword, String searchType, Pageable pageable) {
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
                .map(Student::getEmail)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getPhoneNumberSuggestions(String phoneNumber) {
        return studentRepository.findByPhoneNumberContaining(phoneNumber).stream()
                .map(Student::getPhoneNumber)
                .distinct()
                .collect(Collectors.toList());
    }
    public List<String> getLastnameFirstnameSuggestions(String input) {
        return studentRepository.findByLastNameContainingOrFirstNameContaining(input, input).stream()
                .map(student -> student.getFirstName() + " " +  student.getLastName() )
                .distinct()
                .collect(Collectors.toList());
    }

    public Student findByPhone(String phone) {
        return studentRepository.findByPhoneNumber(phone);
    }

    public Student findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
}
