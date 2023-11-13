package bg.deliev.quasarapp.services.impl;

import bg.deliev.quasarapp.models.entities.UserEntity;
import bg.deliev.quasarapp.models.entities.UserRoleEntity;
import bg.deliev.quasarapp.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class QuasarUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public QuasarUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepository
                .findByEmail(email)
                .map(QuasarUserDetailsService::map)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found!"));
        // TODO: Handle exception
    }

    private static UserDetails map(UserEntity user) {
        return User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().stream().map(QuasarUserDetailsService::map).toList())
                .build();
    }

    private static GrantedAuthority map(UserRoleEntity userRoleEntity) {
        return new SimpleGrantedAuthority("ROLE_" + userRoleEntity.getRole().name());
    }
}
