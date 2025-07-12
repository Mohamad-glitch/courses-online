package com.example.authservice.services;

import auth_user_communication.LoginUserResponse;
import com.example.authservice.dto.LoginRequestDto;
import com.example.authservice.exceptions.InvalidPasswordOrEmail;
import com.example.authservice.exceptions.UserNotFound;
import com.example.authservice.grpc.AuthUserGrpcServiceClient;
import com.example.authservice.utils.JwtUtils;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final AuthUserGrpcServiceClient authUserGrpcServiceClient;
    private final JwtUtils  jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthUserGrpcServiceClient authUserGrpcServiceClient, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.authUserGrpcServiceClient = authUserGrpcServiceClient;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(LoginRequestDto loginRequestDto) {

        try{
            LoginUserResponse response =
                    authUserGrpcServiceClient.loginUserInfo(loginRequestDto.email(), loginRequestDto.password());

            if(response.getMessage().equals("User not found")) {
                throw new InvalidPasswordOrEmail("invalid email or password");
            }


            if(passwordEncoder.matches(loginRequestDto.password(), response.getPassword())) {
                return jwtUtils.generateToken(response.getEmail(), response.getRole(), response.getFullName());
            }else{
                throw new InvalidPasswordOrEmail("invalid email or password");
            }

        }catch (InvalidPasswordOrEmail | StatusRuntimeException ex){
            log.warn(ex.getMessage());
            throw ex;
        }

    }


}
