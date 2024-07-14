package am.itspace.employeemanagement.security;

import am.itspace.employeemanagement.entity.User;
import am.itspace.employeemanagement.exception.UserNotFoundException;
import am.itspace.employeemanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.module.ResolutionException;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Object user = userRepository.findByEmail(username).orElseThrow(() ->
                    new ResolutionException("user with email " + username + " does not exist"));

            return new CurrentUser((User) user);
        };
    }
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> {
//            User user = (User) userRepository.findByEmail(username).orElseThrow(() ->
//                    new UserNotFoundException("user with email " + username + " does not exist"));
//            return new CurrentUser(user);
//        };
//    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}