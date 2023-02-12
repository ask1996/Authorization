package com.sai.springsecurityclient.controller;

import com.sai.springsecurityclient.entity.PasswordVerificationToken;
import com.sai.springsecurityclient.entity.User;
import com.sai.springsecurityclient.entity.VerificationToken;
import com.sai.springsecurityclient.event.RegistrationCompleteEvent;
import com.sai.springsecurityclient.model.PasswordModel;
import com.sai.springsecurityclient.model.UserModel;
import com.sai.springsecurityclient.repository.VerificationTokenRepository;
import com.sai.springsecurityclient.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.util.Optional;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    private VerificationTokenRepository verificationTokenRepository;
    @PostMapping("/register")
    public String register(@RequestBody UserModel userModel, final HttpServletRequest request){

        User user =  userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(user, "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()));
        return "Success";
    }

    @GetMapping("/verifyRegistration")
    public String verifyUser(@RequestParam("token") String token){
        return userService.validateVerificationToken(token);
    }
    @GetMapping("/resendVerifyToken")
    public String resendToken(@RequestParam("token") String oldToken, final HttpServletRequest request){
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
        return resendVerifyEmail(request, verificationToken);
    }
    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, final HttpServletRequest request){
        User user = userService.getUserbyEmail(passwordModel.getEmail());
        PasswordVerificationToken token = userService.generatePasswordToken(user);
        return sendResetEmail(request, token);
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String oldToken, @RequestBody PasswordModel passwordModel){
        if(userService.validatePasswordToken(oldToken).equalsIgnoreCase("valid")){
            Optional<User> user = userService.getUserByPasswordtoken(oldToken);
            if(user.isPresent()){
                userService.changePassword(passwordModel, user.get());
            }else {
                return "invalid token 1";
            }
        } else {
            return "invalid token 2";
        }
    }

    private String resendVerifyEmail(HttpServletRequest request, VerificationToken verificationToken) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + "/verifyRegistration?token="
                + verificationToken.getToken();
    }
    private String sendResetEmail(HttpServletRequest request, PasswordVerificationToken token) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + "/savePassword?token="
                + token.getToken();
    }
}
