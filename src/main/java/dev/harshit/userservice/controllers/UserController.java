package dev.harshit.userservice.controllers;

import dev.harshit.userservice.dtos.*;
import dev.harshit.userservice.dtos.ResponseStatus;
import dev.harshit.userservice.exceptions.*;
import dev.harshit.userservice.models.Token;
import dev.harshit.userservice.models.User;
import dev.harshit.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto)
            throws UserNotFoundException, InvalidPasswordException {

        Token token = userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setToken(token);

        return responseDto;
    }

    @PostMapping("/signup")
    public SignUpResponseDto signUp(@RequestBody SignUpRequestDto signUpRequestDto)
            throws UserAlreadyExistsException {

        User user = userService.signUp(
                signUpRequestDto.getName(),
                signUpRequestDto.getEmail(),
                signUpRequestDto.getPassword()
                );

        SignUpResponseDto responseDto = new SignUpResponseDto();
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        responseDto.setResponseStatus(ResponseStatus.SUCCESS);

        return responseDto;
    }

    @PostMapping("/validate")
    public UserDto validateToken(@RequestHeader("Authorization") String token)
            throws InvalidTokenException {

        User user = userService.validateToken(token);

        return UserDto.fromUser(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(LogoutRequestDto logoutRequestDto)
            throws TokenNotFoundException {

        userService.logout(logoutRequestDto.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}



/* * @RequestHeader:- Used to bind HTTP request header values to method parameters in a controller.
 It provides way to access headers of an HTTP request and use them within your controller methods.
 * */

// ResponseEntity<Void> :- To return a response without a body