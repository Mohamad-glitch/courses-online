package com.example.authservice.services;

import auth_user_communication.LoginUserResponse;
import com.example.authservice.dto.LoginRequestDto;
import com.example.authservice.dto.NewPasswordDto;
import com.example.authservice.dto.ResetPasswordRequestDto;
import com.example.authservice.exceptions.*;
import com.example.authservice.grpc.AuthUserGrpcServiceClient;
import com.example.authservice.model.ResetPasswordToken;
import com.example.authservice.utils.JwtUtils;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final AuthUserGrpcServiceClient authUserGrpcServiceClient;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final MailSenderService mailSenderService;
    private final ResetPasswordTokenService resetPasswordTokenService;

    public AuthService(AuthUserGrpcServiceClient authUserGrpcServiceClient, JwtUtils jwtUtils, PasswordEncoder passwordEncoder, UserService userService, MailSenderService mailSenderService, ResetPasswordTokenService resetPasswordTokenService) {
        this.authUserGrpcServiceClient = authUserGrpcServiceClient;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.mailSenderService = mailSenderService;
        this.resetPasswordTokenService = resetPasswordTokenService;
    }

    public String login(LoginRequestDto loginRequestDto) {

        try {
            LoginUserResponse response =
                    authUserGrpcServiceClient.loginUserInfo(loginRequestDto.email(), loginRequestDto.password());

            if (response.getMessage().equals("User not found")) {
                throw new InvalidPasswordOrEmail("invalid email or password");
            }


            if (passwordEncoder.matches(loginRequestDto.password(), response.getPassword())) {
                return jwtUtils.generateToken(response.getEmail(), response.getRole(), response.getFullName());
            } else {
                throw new InvalidPasswordOrEmail("invalid email or password");
            }

        } catch (InvalidPasswordOrEmail | StatusRuntimeException ex) {
            log.warn(ex.getMessage());
            throw ex;
        }

    }


    public boolean validateToken(String token) {
        jwtUtils.validateToken(token);
        return true;
    }

    public String resetPasswordToken(String email) {
        return jwtUtils.resetPasswordToken(email);
    }

    public void resetPasswordEmail(ResetPasswordRequestDto resetPasswordRequestDto) {

        if (!userService.userExists(resetPasswordRequestDto.email())) {
            log.info("user with this email {} does not exist", resetPasswordRequestDto.email());
            throw new InvalidEmail("user was not found with this email " + resetPasswordRequestDto.email());
        }

        String token = resetPasswordToken(resetPasswordRequestDto.email());

        mailSenderService.sendResetPassword(resetPasswordRequestDto.email()
                , "http://localhost:4000/auth/reset-password/" + token);

        // this part to save the generated token for resetting the password to add if the user token has expired or not used or not
        // is this token for this email will be faster than decode the token to check if the token fo the same email or not

        ResetPasswordToken saveToken = new ResetPasswordToken();
        saveToken.setToken(token);
        saveToken.setEmail(resetPasswordRequestDto.email());
        saveToken.setAccessed(false);
        saveToken.setCreatedAt(new Date(System.currentTimeMillis()));
        saveToken.setExpiredAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)); // 2 hours
        resetPasswordTokenService.save(saveToken);

    }

    public void resetPassword(String token, NewPasswordDto newPasswordDto){

        ResetPasswordToken savedToken = resetPasswordTokenService.findByToken(token);

        if(savedToken == null){
            throw new InvalidCredentials("invalid credentials");
        } else if (savedToken.isAccessed()) {
            throw new UrlHasBeenUsed("URL has been used");
        } else if (new Date().after(savedToken.getExpiredAt())) {
            throw new TokenExpiredException("URl expired");
        }

        try{
            String message =  authUserGrpcServiceClient.updateUserPassword(savedToken.getEmail()
                    , passwordEncoder.encode(newPasswordDto.password())).getMessage();

            if(!message.equals("ok")) {
                throw new Exception(message);
            }

        }catch(StatusRuntimeException e){
            throw e;

        }catch (Exception e){
            log.warn(e.getMessage());
            throw new InvalidCredentials("something went wrong");
        }

        savedToken.setAccessed(true);
        savedToken.setAccessedAt(new Date(System.currentTimeMillis()));
        resetPasswordTokenService.save(savedToken);
    }


}
