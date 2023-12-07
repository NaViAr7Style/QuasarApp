package bg.deliev.quasarapp.service.authentication;

import bg.deliev.quasarapp.model.entity.UserEntity;
import bg.deliev.quasarapp.model.entity.UserRoleEntity;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class QuasarUserDetails implements UserDetails {

    private final long userId;
    private final String firstName;
    private final String lastName;
    private final String password;
    private final String email;
    private final Set<SimpleGrantedAuthority> roles;
    private final boolean isActive;

    public QuasarUserDetails(UserEntity user) {
        this.userId = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.roles = mapRoles(user.getRoles());
        this.isActive = user.isActive();
    }


    @Override
    public Set<SimpleGrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    private Set<SimpleGrantedAuthority> mapRoles(Set<UserRoleEntity> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().name()))
                .collect(Collectors.toSet());
    }

}
