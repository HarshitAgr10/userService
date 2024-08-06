package dev.harshit.userservice.dtos;

import dev.harshit.userservice.models.Role;
import dev.harshit.userservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private String name;
    private String email;
    private List<Role> roles;

    public static UserDto fromUser(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(userDto.getEmail());
        userDto.setRoles(userDto.getRoles());

        return userDto;
    }
}
