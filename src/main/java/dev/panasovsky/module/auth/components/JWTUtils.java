package dev.panasovsky.module.auth.components;

import dev.panasovsky.module.auth.model.Role;
import dev.panasovsky.module.auth.services.RoleService;
import dev.panasovsky.module.auth.model.jwt.JWTAuthentication;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.HashSet;


@Component
@RequiredArgsConstructor
public class JWTUtils {

    private final RoleService roleService;


    public JWTAuthentication generate(final Claims claims) {

        final JWTAuthentication jwtInfoToken = new JWTAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setLogin(claims.getSubject());

        return jwtInfoToken;
    }

    private Set<Role> getRoles(final Claims claims) {

        final String rolename = claims.get("role", String.class);

        final Set<Role> roles = new HashSet<>();
        roles.add(roleService.findByRolename(rolename));
        return roles;
    }

}