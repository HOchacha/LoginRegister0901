package com.example.loginregister.service;

import com.example.loginregister.entity.RefreshToken;
import com.example.loginregister.entity.User;
import com.example.loginregister.payload.request.LoginRequest;
import com.example.loginregister.payload.response.JwtResponse;
import com.example.loginregister.repository.RoleRepository;
import com.example.loginregister.repository.UserCollectionRepo;
import com.example.loginregister.security.jwt.JwtUtils;
import com.example.loginregister.security.service.RefreshTokenService;
import com.example.loginregister.security.service.UserDetailsImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AuthenticationService {
    RefreshTokenService refreshTokenService;
    AuthenticationManager authenticationManager;
    UserCollectionRepo userCollectionRepo;
    JwtUtils jwtUtils;
    @Autowired
    public AuthenticationService(RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager, UserCollectionRepo userCollectionRepo, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
        this.userCollectionRepo = userCollectionRepo;
        this.jwtUtils = jwtUtils;
    }

    public JwtResponse setAuthentication(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String Jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        User user = userCollectionRepo.findByUsername(((UserDetailsImpl) authentication.getPrincipal()).getUsername()).get();
        return new JwtResponse(Jwt, refreshToken.getToken(),user.getId(), user.getUsername(), user.getEmail(),user.getSchoolName());
    }

    public User findUserByUsername(String username){
        return userCollectionRepo.findByUsername(username).get();
    }


}
