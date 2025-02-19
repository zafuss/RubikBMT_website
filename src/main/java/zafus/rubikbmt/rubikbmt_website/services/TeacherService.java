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
import zafus.rubikbmt.rubikbmt_website.entities.Teacher;
import zafus.rubikbmt.rubikbmt_website.repositories.IRoleRepository;
import zafus.rubikbmt.rubikbmt_website.repositories.ITeacherRepository;
import zafus.rubikbmt.rubikbmt_website.repositories.IUserRepository;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestCreateTeacher;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class TeacherService  {
    @Autowired
    private ITeacherRepository teacherRepository;

    @Autowired
    private IUserRepository userRepository;

    @Transactional
    public void saveTeacher(@NotNull RequestCreateTeacher requestUser, Set<Role> roles) {
        try {
            Teacher teacher = new Teacher();
            teacher.setUserName(requestUser.getUserName());
            teacher.setEmail(requestUser.getEmail());
            teacher.setPasswordHash(new BCryptPasswordEncoder().encode(requestUser.getPasswordHash()));
            teacher.setFirstName(requestUser.getFirstName());
            teacher.setLastName(requestUser.getLastName());
            teacher.setRoles(roles);
            teacher.setPhoneNumber(requestUser.getPhoneNumber());
            teacher.setCreateDate(LocalDateTime.now());
            teacher.setEnabled(true);
            teacher.setDescription(requestUser.getDescription());

            userRepository.save(teacher);  // Đảm bảo save vào repository chính xác
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lưu giáo viên: " + e.getMessage());
        }
    }

    public Teacher getTeacherById(String id) {
        return teacherRepository.findById(id).orElse(null);
    }

    public Page<Teacher> searchTeachers(String keyword, String searchType, Pageable pageable) {
        switch (searchType) {
            case "email":
                return teacherRepository.findTeachersByEmailContaining(keyword, pageable);
            case "phone":
                return teacherRepository.findTeachersByPhoneNumberContaining(keyword, pageable);
            default:
                return teacherRepository.findAllTeachers(pageable);
        }
    }
}