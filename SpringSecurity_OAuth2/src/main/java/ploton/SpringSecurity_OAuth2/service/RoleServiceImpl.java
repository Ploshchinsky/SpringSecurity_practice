package ploton.SpringSecurity_OAuth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ploton.SpringSecurity_OAuth2.entity.Role;
import ploton.SpringSecurity_OAuth2.repository.RoleRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
}
