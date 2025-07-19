package com.example.userservice.services;

import com.example.userservice.dto.CreateNewUserRequest;
import com.example.userservice.mapper.Mapper;
import com.example.userservice.model.Users;
import com.example.userservice.repo.UsersDAO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class UsersGrpcService {

    private final UsersDAO usersDAO;

    public UsersGrpcService(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    @Transactional
    public String createNewUser(@Valid CreateNewUserRequest newUserRequest) {
        if (usersDAO.existsUsersByEmail(newUserRequest.email())) {
            return "exists";
        }

        usersDAO.save(Mapper.CreateNewUserToUsers(newUserRequest));

        return "created";
    }

    public Users getUserByEmail(String email) {

        return usersDAO.findUsersByEmail(email);
    }

    public boolean userExists(String email) {
        return usersDAO.existsUsersByEmail(email);
    }

    @Transactional
    public void updateUser(Users user) {
        usersDAO.save(user);
    }

}
