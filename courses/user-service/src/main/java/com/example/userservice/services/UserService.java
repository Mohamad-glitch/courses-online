package com.example.userservice.services;

import com.example.userservice.dto.UpdateUserInfoRequest;
import com.example.userservice.exceptions.UserNotFound;
import com.example.userservice.model.Users;
import com.example.userservice.repo.UsersDAO;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UsersDAO usersDAO;

    public UserService(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public Users getUserByEmail(String email) {
        Users user = usersDAO.findUsersByEmail(email);

        if (user == null) {
            throw new UserNotFound("User not found with this email: " + email);
        }

        return user;
    }

    public void updateUserInfo(UpdateUserInfoRequest user) {

        Users temp = getUserByEmail(user.email());

        temp.setBio(user.bio());
        temp.setFullName(user.fullName());

        usersDAO.save(temp);

    }

}
