package dev.harshit.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity(name = "user")
public class User extends BaseModel {

    private String name;

    private String email;

    private String hashedPassword;

    @ManyToMany
    private List<Role> roles;
}
