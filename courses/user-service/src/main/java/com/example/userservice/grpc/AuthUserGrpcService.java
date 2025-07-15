package com.example.userservice.grpc;

import auth_user_communication.*;
import com.example.userservice.dto.CreateNewUserRequest;
import com.example.userservice.model.Users;
import com.example.userservice.services.UsersService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class AuthUserGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(AuthUserGrpcService.class);
    private final UsersService usersService;

    @Autowired
    public AuthUserGrpcService(UsersService usersService) {
        this.usersService = usersService;
    }


    private void infoLog(Object request, String message) {
        log.info(message, request.toString());
    }

    @Override
    public void createNewUser(CreateUserRequest request,
                              StreamObserver<CreateUserResponse> responseObserver) {

        // here where the request will be sent and here where the logic for user and auth begin

        infoLog(request, "user info {}");

        String saveUserResponse = usersService.createNewUser(new CreateNewUserRequest(request.getEmail(),
                request.getPassword(), request.getFullName(), request.getRole()));


        CreateUserResponse response = CreateUserResponse.newBuilder()
                .setMessage(saveUserResponse)
                .build();


        responseObserver.onNext(response);// this to tell what to send back can use it more than one
        responseObserver.onCompleted();// this to end the method and send the response

    }


    @Override
    public void loginUser(LoginUserRequest request,
                          StreamObserver<LoginUserResponse> responseObserver) {


        infoLog(request, "user login info {}");

        Users user = usersService.getUserByEmail(request.getEmail());
        LoginUserResponse response;
        if (user != null) {

            response = LoginUserResponse
                    .newBuilder()
                    .setEmail(user.getEmail())
                    .setPassword(user.getPassword())
                    .setFullName(user.getFullName())
                    .setRole(user.getRole())
                    .setMessage("ok")
                    .build();

        } else {
            response = LoginUserResponse
                    .newBuilder()
                    .setMessage("User not found")
                    .build();

        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public void userExists(UserExistsRequest request, StreamObserver<UserExistsResponse> responseObserver) {

        log.info("user exists request {}", request.toString());

        UserExistsResponse response = UserExistsResponse.newBuilder()
                .setUserStatus(usersService.userExists(request.getEmail()))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateUserPassword(NewPasswordRequest request, StreamObserver<NewPasswordResponse> responseObserver) {

        try {
            log.info("user update password request {}", request.toString());

            Users user = usersService.getUserByEmail(request.getEmail());

            user.setPassword(request.getPassword());

            usersService.updateUser(user);

            NewPasswordResponse response = NewPasswordResponse.newBuilder()
                    .setMessage("ok")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {

            NewPasswordResponse response = NewPasswordResponse.newBuilder()
                    .setMessage(e.getMessage())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

    }
}
