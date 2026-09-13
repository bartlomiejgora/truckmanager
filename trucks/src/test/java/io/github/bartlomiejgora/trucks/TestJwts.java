package io.github.bartlomiejgora.trucks;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

/**
 * Tokeny-atrapy dla testow MockMvc - odpowiadaja rolom realmu truckmanager po przejsciu
 * przez {@link KeycloakRealmRoleConverter}.
 */
final class TestJwts {

    static RequestPostProcessor admin() {
        return withRoles(Roles.ADMIN);
    }

    static RequestPostProcessor wlascicielFirmy() {
        return withRoles(Roles.WLASCICIEL_FIRMY);
    }

    static RequestPostProcessor kierowca() {
        return withRoles(Roles.KIEROWCA);
    }

    static RequestPostProcessor bezRoli() {
        return withRoles();
    }

    private static RequestPostProcessor withRoles(String... roles) {
        List<GrantedAuthority> authorities = Arrays.stream(roles)
                .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
        return jwt().authorities(authorities);
    }

    private TestJwts() {
    }
}
