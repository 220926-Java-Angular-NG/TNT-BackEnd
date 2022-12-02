package com.revature.services;

import com.revature.models.Product;
import com.revature.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final ProductService productService;

    public Optional<User> findByCredentials(String email, String password) {
        return userService.findByCredentials(email, password);
    }

    public User register(User user) {
        return userService.save(user);
    }


    public List<Product> findAllByFeaturedTrue() {
        return productService.findAllByFeaturedTrue();
    }

    // Find user by id is a helper method to update the user
    public User findUserById(int id){
        return userService.findUserById(id);
    }


    // Start of change user password logic.

    public Optional<User> updatePassword(String email,String oldPassword,String newPassword){
        User user = userService.findUserByEmail(email);

        System.out.println(oldPassword);
        System.out.println(user.getPassword());

        if(oldPassword.equals(user.getPassword())){
            user.setPassword(newPassword);
            userService.save(user);
            return userService.findByCredentials(email,newPassword);
        }else {
            return null;
        }
    }

//    public User testChangePassword(String email,String oldPassword,String newPassword){
//        return updatePassword(email,oldPassword,newPassword)
//                .orElseThrow(() -> new NullPointerException("User password could not be changed"));
//    }

    //End of change user password logic.

}
