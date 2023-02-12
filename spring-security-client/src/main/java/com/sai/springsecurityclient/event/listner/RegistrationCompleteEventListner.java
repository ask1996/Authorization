package com.sai.springsecurityclient.event.listner;

import com.sai.springsecurityclient.entity.User;
import com.sai.springsecurityclient.event.RegistrationCompleteEvent;
import com.sai.springsecurityclient.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RegistrationCompleteEventListner implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveTokenForUser(user, token);
        String url =  event.getUrl() + "/verifyRegistration?token=" + token;
        log.info(url + " verify reg url");

    }
}
