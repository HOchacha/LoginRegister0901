package com.example.loginregister.repository;


import com.example.loginregister.entity.RefreshToken;
import com.example.loginregister.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RefreshTokenRepo extends MongoRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);

    long deleteByUser(User user);
}
