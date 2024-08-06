package dev.harshit.userservice.repositories;

import dev.harshit.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token save(Token token);

    Optional<Token> findByValueAndDeletedAndExpiryAtGreaterThan(
            String value,
            Boolean deleted,
            Date date
    );

    Optional<Token> findByValueandDeleted(String token, Boolean deleted);

    Token save(String token);
}
