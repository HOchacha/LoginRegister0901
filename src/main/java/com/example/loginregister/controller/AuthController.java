package com.example.loginregister.controller;

import com.example.loginregister.entity.User;
import com.example.loginregister.entity.RefreshToken;
import com.example.loginregister.exception.TokenRefreshException;
import com.example.loginregister.exception.UserEmailDuplicationException;
import com.example.loginregister.exception.UsernameDuplicatedException;
import com.example.loginregister.payload.request.LoginRequest;
import com.example.loginregister.payload.request.SignupRequest;
import com.example.loginregister.payload.request.TokenRefreshRequest;
import com.example.loginregister.payload.response.MessageResponse;
import com.example.loginregister.payload.response.JwtResponse;
import com.example.loginregister.payload.response.TokenRefreshResponse;
import com.example.loginregister.security.jwt.JwtUtils;
import com.example.loginregister.security.service.RefreshTokenService;
import com.example.loginregister.service.AuthenticationService;
import com.example.loginregister.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Log4j2
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
@OpenAPIDefinition(info = @Info(title = "Login Authorization", description = "Sign I/O & up", version = "v1"))
public class AuthController {
    RefreshTokenService refreshTokenService;
    JwtUtils jwtUtils;
    AuthenticationService authenticationService;
    UserService userService;

    @Autowired
    public AuthController(UserService userService, RefreshTokenService refreshTokenService, AuthenticationService authenticationService, JwtUtils jwtUtils) {
        this.refreshTokenService = refreshTokenService;
        this.jwtUtils = jwtUtils;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(
                authenticationService.setAuthentication(loginRequest)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        log.info(requestRefreshToken);

        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByToken(requestRefreshToken);
        if (!optionalRefreshToken.isPresent()) {
            throw new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!");
        }
        RefreshToken validRefreshToken = refreshTokenService.verifyExpiration(optionalRefreshToken.get());
        log.info(validRefreshToken);
        User user = validRefreshToken.getUser();
        log.info(user);
        String token = jwtUtils.generateTokenFromUsername(user.getUsername());
        log.info(token);

        return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
        //jwtUtils를 어떻게 빼야할지 고민, refreshTokenService 쪽으로 빼고 싶지만, map 함수의 구조가 이해되지 않음
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        log.info(signUpRequest);
        try {
            User user = userService.enrollUser(signUpRequest);
            log.info(user);
            JwtResponse jwtResponse = authenticationService.setAuthentication(new LoginRequest(signUpRequest.getUsername(), signUpRequest.getPassword()));

            return ResponseEntity.ok(jwtResponse);
        }
        catch(UsernameDuplicatedException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Username is already in use."));
        }
        catch(UserEmailDuplicationException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : Email is already using."));
        }
    }
}
