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

    public Optional<User> updatePassword(String email,String oldPassword,String newPassword){
        User user = userService.findUserByEmail(email);

        if(oldPassword.equals(user.getPassword())){
            user.setPassword(newPassword);
            userService.save(user);
            return userService.findByCredentials(email,newPassword);
        }else {
            return null;
        }
    }

    public User testChangePassword(String email,String oldPassword,String newPassword) throws Exception {
        return updatePassword(email,oldPassword,newPassword)
                .orElseThrow(() -> new Exception("Password not changed"));
    }
}
