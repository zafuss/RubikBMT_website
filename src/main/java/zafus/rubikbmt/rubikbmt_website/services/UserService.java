package zafus.rubikbmt.rubikbmt_website.services;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import zafus.rubikbmt.rubikbmt_website.entities.User;
import zafus.rubikbmt.rubikbmt_website.repositories.IRoleRepository;
import zafus.rubikbmt.rubikbmt_website.repositories.IUserRepository;
import zafus.rubikbmt.rubikbmt_website.requestEntities.RequestCreateUser;
import zafus.rubikbmt.rubikbmt_website.utilities.RandomUtils;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserService  implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    public List<User> getAllUser() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public User addNewUser(RequestCreateUser requestUser, MultipartFile multipartFile) {
        try {
            User user = new User();
            user.setUserName(requestUser.getUserName());
            user.setEmail(requestUser.getEmail());
            user.setFirstName(requestUser.getFirstName());
            user.setLastName(requestUser.getLastName());
            user.setAvatarUrl(requestUser.getAvatarUrl());
            user.setPasswordHash(requestUser.getPasswordHash());
            user.setPhoneNumber(requestUser.getPhoneNumber());
            Date date = new Date(System.currentTimeMillis());
            user.setCreateDate(date);
            userRepository.save(user);
            return user;
        } catch (Exception ex) {
            throw new RuntimeException("loi add");
        }
    }
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { Exception.class, Throwable.class })
    public void save(@NotNull RequestCreateUser requesUser) {
        try {
            User user = new User();
            user.setUserName(requesUser.getUserName());
            user.setEmail(requesUser.getEmail());
            user.setPasswordHash(new BCryptPasswordEncoder()
                    .encode(requesUser.getPasswordHash()));
            user.setFirstName(requesUser.getFirstName());
            user.setLastName(requesUser.getLastName());
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { Exception.class, Throwable.class })
    public void setDefaultRole(String username) {
        userRepository.findUserByUserName(username)
                .getRoles()
                .add(roleRepository
                        .findById("CLIENT").orElseThrow());
    }
    public User findById(String id) {
        return userRepository.findFirstById(id);
    }

    public User findByUserName(String userName) {
        return userRepository.findUserByUserName(userName);
    }

    public User findByPhone(String phone) {
        return userRepository.findUserByPhoneNumber(phone);
    }

    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserName(userName);
        return user;

    }

    public void updatePrincipal(User user) {
        UserDetails userDetails = user;
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public User ChangePassword(User user, String password) {
        user.setPasswordHash(new BCryptPasswordEncoder().encode(password));
        return userRepository.save(user);
    }public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User createTokenResetPassword(User user) {
        String token = RandomUtils.generateRandomString(45);
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpired(new Date(System.currentTimeMillis() + 10 * 60 * 1000));
        return userRepository.save(user);
    }

//    public User findUserByResetPasswordToken(String token) {
//        return userRepository.findByToken(token);
//    }

    public User updateResetPasswordToken(User user, String password) {
        user.setPasswordHash(new BCryptPasswordEncoder().encode(password));
        user.setResetPasswordToken("");
        user.setResetPasswordTokenExpired(null);
        return userRepository.save(user);
    }

//    public void SendMail(String DesMail, String URL, String username) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("todream@gmail.com");
//        message.setTo(DesMail);
//        message.setSubject("Reset Your Password");
//        String emailContent = "Hello, " + username + "\n" +
//                "You have requested to reset your password.\n" +
//                "Click the link below to change your password:" + URL + "\n" +
//                "Ignore this email if you remember your password or you have not made this request.\n" +
//                "Thank you!";
//        message.setText(emailContent);
//        emailSender.send(message);
//    }


//    public void UpdateCountFail(User user) {
//        if (user.isEnabled()) {
//            int count = userRepository.countFail(user.getUsername());
//            count += 1;
//            user.setCountFail(count);
//            if (count == 4) {
//                user.setEnabled(true);
//                user.setCountFail(0);
//                user.setLockExpired(new Date(System.currentTimeMillis() + 10 * 2 * 10000000));
//            }
//        } else {
//            if (user.getLockExpired() != null) {
//                if (user.getLockExpired().getTime() < System.currentTimeMillis()) {
//                    user.setLockExpired(null);
//                    user.setEnabled(true);
//                }
//            }
//        }
//        userRepository.save(user);
//    }

    public void resetLockAccount(User user) {
        user.setCountFail(0);
        user.setEnabled(false);
        user.setLockExpired(null);
        userRepository.save(user);
    }
    public void lockAccount(String id){
        User user = userRepository.findFirstById(id);
        user.setEnabled(true);
        user.setLockExpired(null);
        userRepository.save(user);
    }

    public void unlockAccount(String id){
        User user = userRepository.findFirstById(id);
        user.setEnabled(false);
        user.setLockExpired(null);
        userRepository.save(user);
    }
}
