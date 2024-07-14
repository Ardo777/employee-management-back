package am.itspace.employeemanagement.service.impl;

import am.itspace.employeemanagement.entity.User;
import am.itspace.employeemanagement.service.MailService;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String email;

    private final JavaMailSender javaMailSender;


    @Override
    @Async
    @SneakyThrows
    public void sendVerificationEmail(User user) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        mimeMessage.setFrom(email);
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
        mimeMessage.setSubject("Verify your account | Employee Management System");
        mimeMessage.setText("Hi " + user.getName() + " this is your verification code " + user.getVerificationCode());
        javaMailSender.send(mimeMessage);
        log.info("Email sent");

    }

}

