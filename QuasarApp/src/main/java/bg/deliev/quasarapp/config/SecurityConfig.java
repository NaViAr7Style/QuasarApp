package bg.deliev.quasarapp.config;

import bg.deliev.quasarapp.model.enums.UserRoleEnum;
import bg.deliev.quasarapp.repository.UserRepository;
import bg.deliev.quasarapp.service.authentication.QuasarUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final String rememberMeKey;

    public SecurityConfig(@Value("${quasar.remember.me.key}") String rememberMeKey) {
        this.rememberMeKey = rememberMeKey;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                // Allow anyone to see the home page, login and register pages
                                .requestMatchers("/", "/users/register", "/users/activate").permitAll()
                                .requestMatchers("/users/login", "/users/login-error").permitAll()
                                .requestMatchers("/games/all", "/game/**").permitAll()
                                .requestMatchers("/publishers/all", "/publisher/**").permitAll()
                                .requestMatchers("/contacts", "/faq", "/about").permitAll()
                                .requestMatchers("/user/profile").authenticated()
                                .anyRequest().hasRole(UserRoleEnum.ADMIN.name())
                ).formLogin(
                        formLogin -> formLogin
                                .loginPage("/users/login")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/")
                                .failureForwardUrl("/users/login-error")
                ).logout(
                        logout -> logout
                                .logoutUrl("/users/logout")
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true)
                ).rememberMe(
                        rememberMe -> rememberMe
                                .key(rememberMeKey)
                                .rememberMeParameter("rememberme")
                                .rememberMeCookieName("rememberme")
                                .tokenValiditySeconds(1800)
                ).csrf(
                        csrf -> csrf.ignoringRequestMatchers("/api/users/**")
                ).build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new QuasarUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }
}
