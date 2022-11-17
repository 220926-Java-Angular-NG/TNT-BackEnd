package com.revature.controllers;


import com.revature.dtos.ChangePasswordRequest;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dtos.LoginRequest;
import com.revature.dtos.RegisterRequest;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"}, allowCredentials = "true")
public class AuthController {

    private final AuthService authService;
    private final ObjectMapper objectMapper;


    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        Optional<User> optional = authService.findByCredentials(loginRequest.getEmail(), loginRequest.getPassword());
        if(!optional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        session.setAttribute("user", optional.get());

        // get current user, from users session cookie, and convert it to a User object
        User currUser = (User)session.getAttribute("user");
        // now have access to all of users information
        System.out.println(currUser.getId() + currUser.getEmail());


        return ResponseEntity.ok(optional.get());
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpSession session) {
//        System.out.println("HELLOP THERERER" + session.getAttribute("user"));
        session.removeAttribute("user");

        return ResponseEntity.ok(true);
    }



    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest registerRequest) {
        User created = new User(0,
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getFirstName(),
                registerRequest.getLastName());

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(created));
    }


    @GetMapping("/featured")
    public ResponseEntity<List<Product>> getAllFeaturedProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(authService.findAllByFeaturedTrue());

    }



    @PostMapping("/change-password-proto")
    public ResponseEntity<User> updatePassword(String email, String oldPassword, String newPassword,HttpSession session){


        Optional<User> optional = authService.updatePassword(email,oldPassword,newPassword);

        if(!optional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        session.removeAttribute("user");

        return ResponseEntity.ok().build();

    }

    @PostMapping("/change-password")
    public User changePassword(@RequestBody ChangePasswordRequest change){
        System.out.println(change.getEmail());
        System.out.println(change.getOldPassword());
        System.out.println(change.getNewPassword());
        System.out.println();

        return authService.testChangePassword(change.getEmail(),change.getOldPassword(),change.getNewPassword());
    }
}
