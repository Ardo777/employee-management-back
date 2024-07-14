package am.itspace.employeemanagement.endpoint;

import am.itspace.employeemanagement.entity.AuthenticationRequest;
import am.itspace.employeemanagement.entity.AuthenticationResponse;
import am.itspace.employeemanagement.entity.User;
import am.itspace.employeemanagement.service.UserService;
import am.itspace.employeemanagement.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserEndpoint {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registeredUser = userService.register(user);
        log.info("User redirected to email verification: {}", registeredUser);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/emailVerification")
    public ResponseEntity<?> emailVerification(@RequestBody Map<String, Object> payload) {
        User user = new ObjectMapper().convertValue(payload.get("user"), User.class);
        String verificationCode = (String) payload.get("verificationCode");
        boolean isVerified = userService.verifyUser(user, verificationCode);
        if (isVerified) {
            log.info("User verified: {}", user);
            return ResponseEntity.ok(user);
        } else {
            log.warn("Verification failed for user: {}", user);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Verification code is incorrect");
        }


    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.username(), authenticationRequest.password())
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        log.info("JWT token: {}", jwt);
        log.info("User authenticated: {}", userDetails.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
