package dev.harshit.userservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.harshit.userservice.dtos.SendEmailDto;
import dev.harshit.userservice.exceptions.*;
import dev.harshit.userservice.models.Token;
import dev.harshit.userservice.models.User;
import dev.harshit.userservice.repositories.TokenRepository;
import dev.harshit.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public UserServiceImpl(
            UserRepository userRepository,
            TokenRepository tokenRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            KafkaTemplate kafkaTemplate) {

        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }
    @Override
    public Token login(String email, String password)
            throws UserNotFoundException, InvalidPasswordException {

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            // throw an exception or redirect user to signup
            throw new UserNotFoundException("User with Email " + email + " not found");

            // return "redirect:/signup";    // Redirect to signup page
        }

        User user = userOptional.get();
        if (!bCryptPasswordEncoder.matches(password, user.getHashedPassword())) {
            // throw an exception
            throw new InvalidPasswordException("Invalid Password");
        }

        Token token = createToken(user);

        return tokenRepository.save(token);
    }

    @Override
    public User signUp(String name, String email, String password)
            throws UserAlreadyExistsException {

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            // throw an exception or redirect user to login
            throw new UserAlreadyExistsException("User with email " + email + " already exists.");

            // return new ModelAndView(new RedirectView("/login", true));
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setDeleted(false);

        userRepository.save(user);

        SendEmailDto sendEmailDto = new SendEmailDto();
        sendEmailDto.setFrom("test@gmail.com");
        sendEmailDto.setTo("abc@gmail.com");
        sendEmailDto.setSubject("Welcome");
        sendEmailDto.setBody("Hello World!");

        // Take the Java Object and convert that to a String
        String sendEmailDtoString = null;
        try {
            sendEmailDtoString = objectMapper.writeValueAsString(sendEmailDto);
        } catch (Exception ex) {
            System.out.println("Something went wrong");
        }

        kafkaTemplate.send("sendEmail", sendEmailDtoString);

        return user;
    }

    @Override
    public User validateToken(String token) throws InvalidTokenException {
        Optional<Token> tokenOptional = tokenRepository
                .findByValueAndDeletedAndExpiryAtGreaterThan(token, false, new Date());

        if (tokenOptional.isEmpty()) {
//            throw new InvalidTokenException("Token is invalid or expired");
            return null;
        }

        return tokenOptional.get().getUser();
    }

    @Override
    public void logout(String tokenValue) throws TokenNotFoundException {

        Optional<Token> tokenOpt = tokenRepository.findByValueAndDeleted(tokenValue, false);

        if (tokenOpt.isEmpty()) {
           throw new TokenNotFoundException("Token not found or already deleted");
        }

        Token token = tokenOpt.get();

        token.setDeleted(true);

        tokenRepository.save(token);
    }

    private Token createToken(User user) {
        Token token = new Token();
        token.setCreatedAt(new Date());
        token.setUpdatedAt(new Date());
        token.setUser(user);
        token.setValue(RandomStringUtils.randomAlphanumeric(128));

        // How to set a date 30 days from today :-
        Date currentDate = new Date();                    // Get the current date
        Calendar calendar = Calendar.getInstance();       // Create a Calendar object
        calendar.setTime(currentDate);                    // Set Calendar object to current date
        calendar.add(Calendar.DAY_OF_YEAR, 30);    // Add 30 days to the calendar
        Date date30DaysFromToday = calendar.getTime();    // Get the new date

        token.setExpiryAt(date30DaysFromToday);
        token.setDeleted(false);

        return token;
    }
}






// RandomStringUtils.randomAlphanumeric(count))
// TODO:- UUID
// return new ModelAndView(new RedirectView("/login", true));

/*
 * KafkaTemplate:
 * Used in Spring Boot to send messages to Kafka topics.
 * KafkaTemplate class provides methods to send messages synchronously and asynchronously
 */

/*
 * ObjectMapper:
 * Class provided by Jackson library in Java, used for converting Java objects to JSON and vice versa.
 * Serialization:- Converting Java objects to JSON
 * Deserialization:- Converting JSON to Java objects
 */