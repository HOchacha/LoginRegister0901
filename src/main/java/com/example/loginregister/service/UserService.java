package com.example.loginregister.service;

import com.example.loginregister.entity.ERole;
import com.example.loginregister.entity.Role;
import com.example.loginregister.entity.User;
import com.example.loginregister.exception.UserEmailDuplicationException;
import com.example.loginregister.exception.UsernameDuplicatedException;
import com.example.loginregister.payload.request.SignupRequest;
import com.example.loginregister.repository.RoleRepository;
import com.example.loginregister.repository.UserCollectionRepo;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Log4j2
public class UserService {
    UserCollectionRepo userCollectionRepo;
    RoleRepository roleRepository;
    @Autowired
    public UserService(UserCollectionRepo userCollectionRepo, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userCollectionRepo = userCollectionRepo;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    PasswordEncoder passwordEncoder;


    public User enrollUser(SignupRequest signUpRequest){
        log.info(signUpRequest);

        if (userCollectionRepo.existsByUsername(signUpRequest.getUsername())) {
            log.info(signUpRequest.getUsername());
            throw new UsernameDuplicatedException("Error: Username is already taken!");
        }
        if (userCollectionRepo.existsByEmail(signUpRequest.getEmail())) {
            log.info(signUpRequest.getEmail());
            throw new UserEmailDuplicationException("Error: Email is already used");
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getName(),
                signUpRequest.getEmail(),
                signUpRequest.getSchoolName());

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userCollectionRepo.save(user);
        return user;
    }
}
