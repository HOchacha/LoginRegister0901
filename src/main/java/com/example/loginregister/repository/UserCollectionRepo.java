package com.example.loginregister.repository;


import com.example.loginregister.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCollectionRepo extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(String id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
