package ploton.SpringSecurity_OAuth2.service;

import ploton.SpringSecurity_OAuth2.entity.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String roleName);

}
