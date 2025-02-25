package zafus.rubikbmt.rubikbmt_website.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Sử dụng chiến lược Joined Table
@Table(name = "user") // Đổi tên bảng tránh trùng từ khóa SQL
public class User implements UserDetails {
    private static final long serialVersionUID = 1113799434508676095L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private String id;

    private String userName;

    @Column(nullable = false)
    private String passwordHash;

    @Column(unique = true, nullable = false)
    private String email;

    private String phoneNumber;
    private String avatarUrl;
    private LocalDateTime createDate;
    private String firstName;
    private String lastName;
    private int countFail;
    private LocalDateTime lockExpired;
    private boolean enabled;
    private String resetPasswordToken;
    private Date resetPasswordTokenExpired;

    @Column(name = "provider", length = 50)
    private String provider;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    // Các phương thức bắt buộc của UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .toList();
    }
    public User(String userName, String email, String passwordHash, String firstName, String lastName,
                Set<Role> roles, String phoneNumber) {
        this.userName = userName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
        this.phoneNumber = phoneNumber;
        this.createDate = LocalDateTime.now(); // Đặt mặc định
        this.enabled = true;                   // Đặt mặc định
    }
    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return lockExpired == null || lockExpired.isAfter(LocalDateTime.now());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String disPlayAvatar() {
        return avatarUrl;
    }
}
