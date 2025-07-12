package com.example.authservice.controller;

import com.example.authservice.dto.LoginRequestDto;
import com.example.authservice.dto.RegisterRequestDto;
import com.example.authservice.services.AuthService;
import com.example.authservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthRestController {
    private final UserService userService;
    private final AuthService authService;

    public AuthRestController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    /*
    /register this method will create new user by communicating with User Profile service

    steps:
    1- check if the email already exists
    2- hash the password using bcrypt
    3- create user with RegisterRequestDto data

     */

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) throws Exception {
        // DONE

        // here is the logic

        return userService.createNewUser(registerRequestDto) ?
                ResponseEntity.status(HttpStatus.CREATED).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /*
    /login this method will check if the user exits? return token: throw user not found;
     by communicating with User Profile service

    steps:
    1- check if the email already exists
    2- check if the password matches
    3- every thing is okay then return JWT token
     */

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {

        // here is the logic
        // DONE


        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequestDto));
    }

    /*
    /refresh-token this method will generate the new token and remove the old one

    steps:
    1- check if the token exists in the hashmap where the valid token are saved to do less request of validation
    2- if the token was valid then create new token with more time

     */

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody RegisterRequestDto registerRequestDto) {

        // here is the logic


        return ResponseEntity.ok("Refresh Token Successfully");
    }

    /*
    /verify this method will verify the token to see if it's valid or not

    steps:
    1- check if the token exists in the hashmap where the valid token are saved to do less request of validation
    2- check if the is token was valid

     */

    @GetMapping("/verify")
    public ResponseEntity<String> verifyToken(@RequestParam String token) {

        // here is the logic

        return ResponseEntity.ok("Verify Token Successfully");
    }

    /*
    /reset-password this method will generate an email and send it to the user who requests to reset the password

    steps:
    1- check if the user email exists in user profile service
    2- send an email with the url to reset the password with a given time to expire
    3- may and may not do a DB to store the token to check if the user used it once or not in case,
     if the server restart and lost the in memory data

     */

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPasswordEmail(@RequestParam String token) {

        // here is the logic

        return ResponseEntity.ok("Reset Password Successfully");
    }

    /*
    /reset-password/{token} this method will update user password

    steps:
        1- check if the token is valid
        2- reset the password

     */

    @PatchMapping("/reset-password/{token}")
    public ResponseEntity<Void> resetPassword(@PathVariable String token) {

        // here is the logic

        return ResponseEntity.ok().build();
    }
}
