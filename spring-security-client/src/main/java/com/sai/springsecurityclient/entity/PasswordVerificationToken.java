package com.sai.springsecurityclient.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordVerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date expireTime;
    private String token;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
    private User user;

    public PasswordVerificationToken(User user, String token){
        super();
        this.user=user;
        this.token= token;
        this.expireTime = calculateExpireTime();
    }
    public PasswordVerificationToken(String token){
        super();
        this.token= token;
        this.expireTime = calculateExpireTime();
    }
    public static Date calculateExpireTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, 10);
        return new Date(calendar.getTime().getTime());
    }
}
