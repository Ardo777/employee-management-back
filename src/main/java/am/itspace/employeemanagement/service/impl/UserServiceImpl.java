package am.itspace.employeemanagement.service.impl;

import am.itspace.employeemanagement.entity.User;
import am.itspace.employeemanagement.repository.UserRepository;
import am.itspace.employeemanagement.service.MailService;
import am.itspace.employeemanagement.service.UserService;
import am.itspace.employeemanagement.util.VerificationCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VerificationCodeUtil verificationCodeUtil;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findByEmail(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        return byEmail.orElse(null);
    }

    @Override
    @Transactional
    public User register(User user) {
        User existingUser = findByEmail(user.getEmail());
        if (existingUser != null) {
            if (existingUser.isVerified()) {
                return existingUser;
            } else {
                user.setVerificationCode(verificationCodeUtil.createVerificationCode());
                user.setVerified(false);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                User saveUser = userRepository.save(user);
                mailService.sendVerificationEmail(user);
                return saveUser;
            }
        }
        user.setVerificationCode(verificationCodeUtil.createVerificationCode());
        user.setVerified(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);
        mailService.sendVerificationEmail(user);
        return saved;

    }

    @Override
    public boolean verifyUser(User user, String verificationCode) {
        User storageUser = findByEmail(user.getEmail());
        if (storageUser.getVerificationCode().equals(verificationCode)) {
            log.info("User verified: {}", storageUser);
            storageUser.setVerified(true);
            storageUser.setVerificationCode(null);
            userRepository.save(storageUser);
            return true;
        } else {
            log.warn("Verification failed for user: {}", storageUser);
            return false;
        }
    }

}
