package com.revature.services;

import com.revature.models.User;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public Optional<User> findByCredentials(String email, String password) {
        return userService.findByCredentials(email, password);
    }

    public User register(User user) {
        return userService.save(user);
    }

    public User changePassword(String email,String password,String newPassword){
        User user = userService.findUserByEmail(email);

        if(password.equals(user.getPassword())){
            user.setPassword(newPassword);
            return userService.save(user);
        }

        return null;
    }
}
