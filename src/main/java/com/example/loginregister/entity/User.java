package com.example.loginregister.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
public class User {
    @Id
    private String id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 120)
    private String password;

    @NotBlank
    @Size(max = 30)
    private String name;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 40)
    private String schoolName;

    @DBRef
    private Set<Role> roles = new HashSet<>();

    public User(){
    }

    public User(String username, String password, String name, String email, String schoolName) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.schoolName = schoolName;
    }

}
