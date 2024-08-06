package dev.harshit.userservice.dtos;

import dev.harshit.userservice.models.Token;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private Token token;
}
