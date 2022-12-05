package com.revature.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dtos.login.LoginRequest;
import com.revature.dtos.login.LoginResponse;
import com.revature.dtos.registration.RegisterRequest;
import com.revature.models.Product;
import com.revature.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService  implements UserDetailsService {

    private final UserService userService;
    private final ProductService productService;
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByCredentials(String email, String password) {
        return userService.findByCredentials(email, password);
    }

    //Start of registration methods.
    public Boolean register(RegisterRequest registerRequest) {
        String password = registerRequest.getPassword();
        password = passwordEncoder.encode(password);
        registerRequest.setPassword(password);
        User user = objectMapper.convertValue(registerRequest, User.class);
        user = userService.save(user);
        return (user.getId() != 0);
    }

    //End of registration methods.


    //Start of login methods.
    public Optional<LoginResponse> userLogin(LoginRequest loginRequest){
        String password = loginRequest.getPassword();
        //password = passwordEncoder.encode(password);
        //loginRequest.setPassword(password);



        Optional<LoginResponse> logIn;
        User user = userService.findUserByEmail(loginRequest.getEmail());
        System.out.println();


        if(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            logIn = Optional.of(generateLoginResponse(user));
        }else{
            logIn = Optional.empty();
        }
        return logIn;
    }

    private LoginResponse generateLoginResponse(User user){
        String token = jwtService.generateToken(user);
        LoginResponse logIn = new LoginResponse();
        logIn.setId(user.getId());
        logIn.setEmail(user.getEmail());
        logIn.setFirstName(user.getFirstName());
        logIn.setLastName(user.getLastName());
        logIn.setWishList(user.getWishList());
        logIn.setToken(token);
        return logIn;
    }
    //End of login methods.





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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findUserByEmail(username);
    }
}
