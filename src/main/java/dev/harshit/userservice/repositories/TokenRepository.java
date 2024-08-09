package dev.harshit.userservice.repositories;

import dev.harshit.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token save(Token token);

    Optional<Token> findByValueAndDeletedAndExpiryAtGreaterThan(
            String value,
            Boolean deleted,
            Date date
    );

    Optional<Token> findByValueAndDeleted(String token, Boolean deleted);

    Token save(String token);
}
