package zafus.rubikbmt.rubikbmt_website.services;


import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zafus.rubikbmt.rubikbmt_website.entities.*;
import zafus.rubikbmt.rubikbmt_website.repositories.IRoleRepository;
import zafus.rubikbmt.rubikbmt_website.repositories.ITeacherRepository;
import zafus.rubikbmt.rubikbmt_website.repositories.IUserRepository;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestCreateStudent;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestCreateTeacher;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateStudent;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestUpdateTeacher;
import zafus.rubikbmt.rubikbmt_website.utilities.PasswordGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class TeacherService  {
    @Autowired
    private ITeacherRepository teacherRepository;

    @Autowired
    private IUserRepository userRepository;

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    @Transactional
    public void saveTeacher(@NotNull RequestCreateTeacher requestUser, Set<Role> roles) {
        try {
            String password = PasswordGenerator.generatePassword(requestUser.getEmail(), requestUser.getPhoneNumber());
            Teacher teacher = new Teacher();
            teacher.setUserName(requestUser.getEmail());
            teacher.setEmail(requestUser.getEmail());
            teacher.setPasswordHash(new BCryptPasswordEncoder().encode(password));
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

    @Transactional
    public void saveTeacherFromUser(User user, RequestCreateTeacher requestUser, Set<Role> updatedRoles) {
        if (user instanceof Student) {
            throw new IllegalStateException("Người dùng đã tồn tại dưới dạng giáo viên.");
        }
        teacherRepository.insertTeacher(
                requestUser.getDescription(),
                user.getId()
        );
    }

    public Teacher getTeacherById(String id) {
        return teacherRepository.findById(id).orElse(null);
    }


    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { Exception.class, Throwable.class })
    public void update(@NotNull RequestUpdateTeacher requestUser) {
        try {
            Teacher user = teacherRepository.findById(requestUser.getId()).orElse(null);
            user.setEmail(requestUser.getEmail());
            user.setPhoneNumber(requestUser.getPhoneNumber());
            user.setFirstName(requestUser.getFirstName());
            user.setLastName(requestUser.getLastName());
            user.setDescription(requestUser.getDescription());

            teacherRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
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