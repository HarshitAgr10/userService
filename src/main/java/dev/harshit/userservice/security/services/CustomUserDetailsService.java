package dev.harshit.userservice.security.services;

import dev.harshit.userservice.models.User;
import dev.harshit.userservice.repositories.UserRepository;
import dev.harshit.userservice.security.models.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
    * Here, simply returning user details by fetching it from the database
    * It is spring's responsibility to match the password with password entered from the browser
    */

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User does not exist");
        }

        User user = userOptional.get();

        return new CustomUserDetails(user);
    }
}
