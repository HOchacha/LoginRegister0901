package com.example.loginregister.security.service;

import com.example.loginregister.entity.User;
import com.example.loginregister.repository.UserCollectionRepo;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {

    UserCollectionRepo userCollectionRepo;
    @Autowired
    public UserDetailsServiceImpl(UserCollectionRepo userCollectionRepo) {
        this.userCollectionRepo = userCollectionRepo;
    }



    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        log.info(username);
        User user = userCollectionRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        log.info(user);
        return UserDetailsImpl.build(user);
    }
}
