package com.example.authservice.services;

import com.example.authservice.dto.RegisterRequestDto;
import com.example.authservice.enums.Role;
import com.example.authservice.exceptions.UserAlreadyExists;
import com.example.authservice.grpc.AuthUserGrpcServiceClient;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    private final AuthUserGrpcServiceClient authUserGrpcServiceClient;

    public UserService(PasswordEncoder passwordEncoder, AuthUserGrpcServiceClient authUserGrpcServiceClient) {
        this.passwordEncoder = passwordEncoder;
        this.authUserGrpcServiceClient = authUserGrpcServiceClient;
    }

    public boolean createNewUser(RegisterRequestDto registerRequestDto) {

        try {

            String response = authUserGrpcServiceClient.createUser(registerRequestDto.email(),
                    passwordEncoder.encode(registerRequestDto.password()),
                    registerRequestDto.fullName(),Role.STUDENT.toString()).getMessage();


            if (response.equals("exists")) {
                throw new UserAlreadyExists("User with email " + registerRequestDto.email() + " already exists");
            }

            return true;
        } catch (UserAlreadyExists e) {
            // Let Spring handle this — don't catch here
            log.info("User with email {} already exists", registerRequestDto.email());
            throw e;

        } catch (StatusRuntimeException grpcException) {
            // this to let ExceptionHandler handle it
            log.warn("grpc exception: ", grpcException);
            throw grpcException;

        } catch (Exception e) {
            log.warn("error while creating user", e);
        }

        return false;
    }

    public boolean userExists(String email) {
        try{

            return authUserGrpcServiceClient.checkUserExists(email).getUserStatus();
        }catch (StatusRuntimeException grpcException) {
            // this to let ExceptionHandler handle it
            log.warn("grpc exception: ", grpcException);
            throw grpcException;

        }catch (Exception e){
            log.warn("error while communicating with user profile service ", e);
            throw e;
        }
    }
}
