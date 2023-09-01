package com.example.loginregister.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*", maxAge = 3600)
@OpenAPIDefinition(info = @Info(title = "Authentication Test", description = "", version = "v1"))
public class TestController {
    @GetMapping("/all")
    public ResponseEntity allAccess() {
        return new ResponseEntity("User Contents",HttpStatus.OK);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity userAccess() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity moderatorAccess() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity adminAccess() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
