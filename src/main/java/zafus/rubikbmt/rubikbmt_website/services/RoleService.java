package zafus.rubikbmt.rubikbmt_website.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zafus.rubikbmt.rubikbmt_website.entities.Role;
import zafus.rubikbmt.rubikbmt_website.repositories.IRoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class RoleService {
    private final IRoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findById(String id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Role add(Role role) {
        return roleRepository.save(role);
    }

    public Role update(Role role) {
        return roleRepository.save(role);
    }

    public void delete(Role role) {
        roleRepository.delete(role);
    }


}