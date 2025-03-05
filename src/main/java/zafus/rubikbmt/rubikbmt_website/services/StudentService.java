package zafus.rubikbmt.rubikbmt_website.services;


import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zafus.rubikbmt.rubikbmt_website.entities.Role;
import zafus.rubikbmt.rubikbmt_website.entities.Student;
import zafus.rubikbmt.rubikbmt_website.entities.User;
import zafus.rubikbmt.rubikbmt_website.repositories.IStudentRepository;
import zafus.rubikbmt.rubikbmt_website.repositories.IUserRepository;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestCreateStudent;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateRegisterStudent;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateStudent;
import zafus.rubikbmt.rubikbmt_website.utilities.PasswordGenerator;

import java.time.LocalDateTime;
import java.util.List;
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
            // Tìm kiếm người dùng theo email hoặc số điện thoại
            User existingUser = userRepository.findUserByEmail(requestUser.getEmail());
            if (existingUser == null) {
                existingUser = userRepository.findUserByPhoneNumber(requestUser.getPhoneNumber());
            }

            if (existingUser == null) {
                // Tạo mới Student nếu không tìm thấy User
                Student student = createStudentFromRequest(requestUser, roles);
                userRepository.save(student);
            } else {
                // Kiểm tra nếu user đã có vai trò Student
                boolean hasStudentRole = existingUser.getRoles().stream()
                        .anyMatch(role -> "Student".equalsIgnoreCase(role.getRoleName()));

                if (hasStudentRole) {
                    throw new IllegalStateException("Người dùng đã tồn tại dưới dạng Student.");
                }

                // Thêm vai trò Student và tạo đối tượng Student
                existingUser.getRoles().addAll(roles);
                Student student = new Student(existingUser, 0, requestUser.getParentName(), requestUser.getParentPhoneNumber());
                userRepository.save(student);
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lưu học viên mới: " + e.getMessage(), e);
        }
    }
    @Transactional
    public void saveStudentFromUser(User user, RequestCreateStudent requestUser, Set<Role> updatedRoles) {
        if (user instanceof Student) {
            throw new IllegalStateException("Người dùng đã tồn tại dưới dạng Student.");
        }
        studentRepository.insertStudent(
                requestUser.getBirthday(),
                requestUser.getStudiedCourse(),
                requestUser.getParentName(),
                requestUser.getParentPhoneNumber(),
                user.getId()
        );
    }
    // Phương thức tạo Student từ Request
    private Student createStudentFromRequest(RequestCreateStudent requestUser, Set<Role> roles) {
        Student student = new Student();
        student.setUserName(requestUser.getEmail());
        student.setEmail(requestUser.getEmail());
        student.setPasswordHash(new BCryptPasswordEncoder().encode(PasswordGenerator.generatePassword(requestUser.getEmail(), requestUser.getPhoneNumber())));
        student.setFirstName(requestUser.getFirstName());
        student.setLastName(requestUser.getLastName());
        student.setRoles(roles);
        student.setPhoneNumber(requestUser.getPhoneNumber());
        student.setBirthday(requestUser.getBirthday());
        student.setParentName(requestUser.getParentName());
        student.setParentPhoneNumber(requestUser.getParentPhoneNumber());
        student.setCreateDate(LocalDateTime.now());
        student.setEnabled(true);
        student.setStudiedCourse(0);
        return student;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { Exception.class, Throwable.class })
    public void update(@NotNull RequestUpdateStudent requestUser) {
        try {
            Student user = studentRepository.findById(requestUser.getId()).orElse(null);
            user.setEmail(requestUser.getEmail());
            user.setPhoneNumber(requestUser.getPhoneNumber());
            user.setFirstName(requestUser.getFirstName());
            user.setLastName(requestUser.getLastName());
            user.setBirthday(requestUser.getBirthday());
            user.setParentName(requestUser.getParentName());
            user.setParentPhoneNumber(requestUser.getParentPhoneNumber());
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public Student getStudentById(String id) {
        return studentRepository.findById(id).orElse(null);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
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