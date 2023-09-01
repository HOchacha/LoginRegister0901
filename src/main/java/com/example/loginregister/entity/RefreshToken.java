package com.example.loginregister.entity;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "refreshtokens")
@Data
public class RefreshToken {

    @Id
    private String id;  // MongoDB에서는 주로 String 타입의 ID를 사용합니다.

    @DBRef
    private User user;

    private String token;

    private Instant expiryDate;

    //getters and setters

}
