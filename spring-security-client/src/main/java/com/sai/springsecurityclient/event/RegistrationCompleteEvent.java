package com.sai.springsecurityclient.event;

import com.sai.springsecurityclient.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private String url;
    private User user;
    public RegistrationCompleteEvent(User user, String url) {
        super(user);
        this.url= url;
        this.user = user;
    }

}
