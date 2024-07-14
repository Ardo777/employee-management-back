package am.itspace.employeemanagement.service;

import am.itspace.employeemanagement.entity.User;

public interface UserService {
    User findByEmail(String email);

    User register(User user);

    boolean verifyUser(User user, String verificationCode);
}
