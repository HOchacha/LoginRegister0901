package com.example.loginregister.payload.response;

import com.example.loginregister.entity.RefreshToken;
import lombok.Data;

import java.util.List;


@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private String id;
    private String username;
    private String email;
    private String schoolName;

    public JwtResponse(String accessToken, String refreshToken, String id, String username, String email, String schoolName) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.schoolName = schoolName;
    }
}
