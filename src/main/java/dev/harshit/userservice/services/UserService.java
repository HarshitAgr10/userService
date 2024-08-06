package dev.harshit.userservice.services;

import dev.harshit.userservice.dtos.LoginRequestDto;
import dev.harshit.userservice.exceptions.*;
import dev.harshit.userservice.models.Token;
import dev.harshit.userservice.models.User;
import org.springframework.web.servlet.ModelAndView;

public interface UserService {
    public Token login(String email, String password)
            throws UserNotFoundException, InvalidPasswordException;

    public User signUp(String name, String email, String password)
            throws UserAlreadyExistsException;

    public User validateToken(String token)
            throws InvalidTokenException;

    public void logout(String token)
            throws TokenNotFoundException;
}
