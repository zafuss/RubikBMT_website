package zafus.rubikbmt.rubikbmt_website.services;


import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zafus.rubikbmt.rubikbmt_website.entities.Role;
import zafus.rubikbmt.rubikbmt_website.entities.Student;
import zafus.rubikbmt.rubikbmt_website.entities.Teacher;
import zafus.rubikbmt.rubikbmt_website.repositories.IRoleRepository;
import zafus.rubikbmt.rubikbmt_website.repositories.IStudentRepository;
import zafus.rubikbmt.rubikbmt_website.repositories.ITeacherRepository;
import zafus.rubikbmt.rubikbmt_website.repositories.IUserRepository;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestCreateStudent;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestCreateTeacher;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class StudentService {
    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private IUserRepository userRepository;

    @Transactional
    public void saveStudent(@NotNull RequestCreateStudent requestUser, Set<Role> roles) {
        try {
            Student student = new Student();
            student.setUserName(requestUser.getUserName());
            student.setEmail(requestUser.getEmail());
            student.setPasswordHash(new BCryptPasswordEncoder().encode(requestUser.getPasswordHash()));
            student.setFirstName(requestUser.getFirstName());
            student.setLastName(requestUser.getLastName());
            student.setRoles(roles);
            student.setPhoneNumber(requestUser.getPhoneNumber());
            student.setCreateDate(LocalDateTime.now());
            student.setEnabled(true);
            student.setStudiedCourse(0);

            userRepository.save(student);  // Đảm bảo save vào repository chính xác
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lưu học viên mới: " + e.getMessage());
        }
    }

    public Student getStudentById(String id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Page<Student> searchStudents(String keyword, String searchType, Pageable pageable) {
        switch (searchType) {
            case "email":
                return studentRepository.findStudentsByEmailContaining(keyword, pageable);
            case "phone":
                return studentRepository.findStudentsByPhoneNumberContaining(keyword, pageable);
            default:
                return studentRepository.findAllStudents(pageable);
        }
    }
}