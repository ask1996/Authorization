package com.sai.springsecurityclient.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String firstName;
    private String LastName;
    private String email;
    @Column(length = 60)
    private String password;
    private String role;
    private boolean enabled = true;
}
