package am.itspace.employeemanagement.service;

import am.itspace.employeemanagement.entity.User;

public interface MailService {


    void sendVerificationEmail(User existingUser);


}
