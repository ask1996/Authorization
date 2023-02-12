package com.sai.springsecurityclient.service;

import com.sai.springsecurityclient.entity.PasswordVerificationToken;
import com.sai.springsecurityclient.entity.User;
import com.sai.springsecurityclient.entity.VerificationToken;
import com.sai.springsecurityclient.model.PasswordModel;
import com.sai.springsecurityclient.model.UserModel;

import java.util.Optional;

public interface UserService {
    public User registerUser(UserModel userModel);

    void saveTokenForUser(User user, String token);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);

    PasswordVerificationToken generatePasswordToken(User user);

    User getUserbyEmail(String email);

    String validatePasswordToken(String oldToken);

    Optional<User> getUserByPasswordtoken(String oldToken);

    void changePassword(PasswordModel passwordModel);
}
