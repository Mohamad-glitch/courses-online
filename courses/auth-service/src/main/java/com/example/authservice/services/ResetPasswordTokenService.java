package com.example.authservice.services;

import com.example.authservice.model.ResetPasswordToken;
import com.example.authservice.repo.ResetPasswordTokenDao;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordTokenService {

    private final ResetPasswordTokenDao resetPasswordTokenDao;

    public ResetPasswordTokenService(ResetPasswordTokenDao resetPasswordTokenDao) {
        this.resetPasswordTokenDao = resetPasswordTokenDao;
    }

    @Transactional
    public void save(ResetPasswordToken resetPasswordToken) {
        resetPasswordTokenDao.save(resetPasswordToken);
    }

    public ResetPasswordToken findByToken(String token) {
        return resetPasswordTokenDao.findResetPasswordTokenByToken(token);
    }


}
