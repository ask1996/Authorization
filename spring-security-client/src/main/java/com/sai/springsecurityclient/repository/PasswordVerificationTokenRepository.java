package com.sai.springsecurityclient.repository;

import com.sai.springsecurityclient.entity.PasswordVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordVerificationTokenRepository extends JpaRepository<PasswordVerificationToken, Long> {
    PasswordVerificationToken findByToken(String token);
}
