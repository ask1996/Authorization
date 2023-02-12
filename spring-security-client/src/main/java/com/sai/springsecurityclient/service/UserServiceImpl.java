package com.sai.springsecurityclient.service;

import com.sai.springsecurityclient.entity.PasswordVerificationToken;
import com.sai.springsecurityclient.entity.User;
import com.sai.springsecurityclient.entity.VerificationToken;
import com.sai.springsecurityclient.model.PasswordModel;
import com.sai.springsecurityclient.model.UserModel;
import com.sai.springsecurityclient.repository.PasswordVerificationTokenRepository;
import com.sai.springsecurityclient.repository.UserRepository;
import com.sai.springsecurityclient.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository repository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PasswordVerificationTokenRepository passwordVerificationTokenRepository;
    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        repository.save(user);
        return user;
    }

    @Override
    public void saveTokenForUser(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken == null){
            return "Bad User";
        }
        User user  = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        Date expireTime = verificationToken.getExpireTime();
        if(expireTime.getTime() - cal.getTime().getTime() <= 0){
            verificationTokenRepository.delete(verificationToken);
            return "token expired";
        }
        user.setEnabled(true);
        repository.save(user);
        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpireTime(VerificationToken.calculateExpireTime());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public PasswordVerificationToken generatePasswordToken(User user) {
        String token = UUID.randomUUID().toString();
        PasswordVerificationToken passwordToken = new PasswordVerificationToken(user, token);
        passwordVerificationTokenRepository.save(passwordToken);
        return passwordToken;
    }

    @Override
    public User getUserbyEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public String validatePasswordToken(String oldToken) {
        PasswordVerificationToken passwordVerificationToken = passwordVerificationTokenRepository.findByToken(oldToken);
        if(passwordVerificationToken == null){
            return "Bad User";
        }
        User user  = passwordVerificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        Date expireTime = passwordVerificationToken.getExpireTime();
        if(expireTime.getTime() - cal.getTime().getTime() <= 0){
            passwordVerificationTokenRepository.delete(passwordVerificationToken);
            return "token expired";
        }
        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordtoken(String oldToken) {
        return Optional.ofNullable(passwordVerificationTokenRepository.findByToken(oldToken).getUser());
    }

    @Override
    public void changePassword(PasswordModel passwordModel, User user) {
        user.setPassword(passwordEncoder.encode(passwordModel.getNewPassword()));
        repository.save(user);
    }
}
