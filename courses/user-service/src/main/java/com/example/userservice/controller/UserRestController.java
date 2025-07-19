package com.example.userservice.controller;

import com.example.userservice.dto.ShowUserInfoDto;
import com.example.userservice.dto.ShowUserInfoRequest;
import com.example.userservice.dto.UpdateUserInfoRequest;
import com.example.userservice.mapper.Mapper;
import com.example.userservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService UserService) {
        this.userService = UserService;
    }


    /*
    /    this method will return user info like full name, email, bio, avatar image (this may not do it)

    1- api gateway will take the request first authenticate the token in auth service then decode jwt token
    extract email user then send it in the main request

    2- after taking email as a parameter query from DB get user info that will be shown to user ex: name,email....
    with id to make update user request ready

     */

    @GetMapping("/")
    @Operation(description = "will display user info like: name, email, bio", summary = "get user info")
    public ResponseEntity<ShowUserInfoDto> getUserInfo(@Valid @RequestBody ShowUserInfoRequest request) {
        // DONE
        // here is the logic


        return ResponseEntity.status(HttpStatus.OK)
                .body(Mapper.fromUserToShowUserInfoDto(userService.getUserByEmail(request.email())));
    }


    @PatchMapping("/update")
    @Operation(description = "this endpoint will update user info", summary = "update user info")
    public ResponseEntity<Void> updateUserInfo(@Valid @RequestBody UpdateUserInfoRequest request) {
        // DONE

        userService.updateUserInfo(request);


        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
