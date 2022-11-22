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


}
