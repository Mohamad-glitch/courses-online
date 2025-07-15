package com.example.authservice.grpc;

import auth_user_communication.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthUserGrpcServiceClient {
    private final AuthServiceGrpc.AuthServiceBlockingStub stub;

    public AuthUserGrpcServiceClient(
            @Value("${user.server.address:localhost}") String serverAddress,
            @Value("${user.server.port:9001}") int port
    ) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, port).usePlaintext().build();

        stub = AuthServiceGrpc.newBlockingStub(channel);
    }


    public CreateUserResponse createUser(String email, String password, String username, String role) {

        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .setFullName(username)
                .setRole(role)
                .build();

        return stub.createNewUser(request);
    }

    public LoginUserResponse loginUserInfo(String email, String password) {

        LoginUserRequest request = LoginUserRequest.newBuilder()
                .setEmail(email)
                .build();


        return stub.loginUser(request);
    }

    public UserExistsResponse checkUserExists(String email) {
        UserExistsRequest request = UserExistsRequest.newBuilder()
                .setEmail(email)
                .build();

        return stub.userExists(request);
    }

    public NewPasswordResponse updateUserPassword(String email, String newPassword){
        NewPasswordRequest request = NewPasswordRequest.newBuilder()
                .setEmail(email)
                .setPassword(newPassword)
                .build();

        return stub.updateUserPassword(request);
    }
}
