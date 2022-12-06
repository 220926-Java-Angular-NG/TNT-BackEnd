package com.revature.controllers;


import com.revature.dtos.ChangePasswordRequest;
import com.revature.dtos.login.LoginRequest;
import com.revature.dtos.login.LoginResponse;
import com.revature.dtos.registration.RegisterRequest;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.UserService;
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
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://tntfrontendrev.s3-website-us-east-1.amazonaws.com"}, allowCredentials = "true")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;



    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        Optional<LoginResponse> optional = authService.userLogin(loginRequest);
        if(!optional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        session.setAttribute("user", optional.get());

        // get current user, from users session cookie, and convert it to a User object
//<<<<<<< HEAD
//=======
//        User currUser = (User)session.getAttribute("user");
//        // now have access to all of user's information
//        System.out.println(currUser.getId() + currUser.getEmail());
//>>>>>>> origin/Dev


        return ResponseEntity.ok(optional.get());
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout() {
//        System.out.println("HELLO THERE" + session.getAttribute("user"));
//        session.removeAttribute("user");

        return ResponseEntity.ok(true);
    }



    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestBody RegisterRequest registerRequest) {
        System.out.println(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerRequest));
    }

    @PostMapping("/wishlist")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        int userId = user.getId();
        User dbUser = authService.findUserById(userId);
        dbUser.setWishList(user.getWishList());
        userService.save(dbUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(dbUser);
    }


    @GetMapping("/featured")
    public ResponseEntity<List<Product>> getAllFeaturedProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(authService.findAllByFeaturedTrue());

    }



    @PostMapping("/change-password")
    public ResponseEntity<Boolean> updatePassword(@RequestBody ChangePasswordRequest updatePassword){


        Optional<Boolean> optional = authService.updatePassword(updatePassword);

        if(!optional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

//        session.removeAttribute("user");

        return ResponseEntity.ok().build();

    }


    /*
    @PostMapping("/change-password")
    public User changePassword(@RequestBody ChangePasswordRequest change){
        System.out.println(change.getEmail());
        System.out.println(change.getOldPassword());
        System.out.println(change.getNewPassword());
        System.out.println();

        return authService.testChangePassword(change.getEmail(),change.getOldPassword(),change.getNewPassword());
    }
    */

}
