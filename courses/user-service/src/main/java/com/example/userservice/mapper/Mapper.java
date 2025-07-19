package com.example.userservice.mapper;

import com.example.userservice.dto.CreateNewUserRequest;
import com.example.userservice.dto.ShowUserInfoDto;
import com.example.userservice.model.Users;

import java.util.Date;


public class Mapper {

    public static Users CreateNewUserToUsers(CreateNewUserRequest createNewUserRequest) {
        Users users = new Users();

        users.setEmail(createNewUserRequest.email());
        users.setPassword(createNewUserRequest.password());
        users.setFullName(createNewUserRequest.fullName());
        users.setRole("STUDENT");
        users.setCreationDate(new Date(System.currentTimeMillis()));


        return users;
    }

    public static ShowUserInfoDto fromUserToShowUserInfoDto(Users users) {

        return new ShowUserInfoDto(
                users.getEmail(), users.getFullName(), users.getBio()
        );
    }


}
