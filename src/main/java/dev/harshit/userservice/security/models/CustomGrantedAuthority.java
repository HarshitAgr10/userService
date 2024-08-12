package dev.harshit.userservice.security.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.harshit.userservice.models.Role;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@JsonDeserialize
@NoArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority {

    // Way-1
//    private Role role;
//
//    public CustomGrantedAuthority(Role role) {
//        this.role = role;
//    }

    // Way-2
    private String authority;

    public CustomGrantedAuthority(Role role) {
        this.authority = role.getName();
    }

    @Override
    public String getAuthority() {
//        return role.getName();
        return authority;
    }
}
