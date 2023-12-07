package bg.deliev.quasarapp.service.authentication;

import bg.deliev.quasarapp.repository.UserRepository;
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
                .map(QuasarUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found!"));
    }

}
