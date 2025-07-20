package com.example.courseservice.grpc;

import auth_user_communication.AuthServiceGrpc;
import auth_user_communication.UserExistsRequest;
import auth_user_communication.UserExistsResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CourseUserGrpcService {

    private final AuthServiceGrpc.AuthServiceBlockingStub stub;

    public CourseUserGrpcService(
            @Value("${user.server.address:localhost}") String serverAddress,
            @Value("${user.server.port:9001}") int port
    ) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, port).usePlaintext().build();

        stub = AuthServiceGrpc.newBlockingStub(channel);
    }


    public UserExistsResponse checkUserExists(String email) {
        UserExistsRequest request = UserExistsRequest.newBuilder()
                .setEmail(email)
                .build();

        return stub.userExists(request);
    }




}
