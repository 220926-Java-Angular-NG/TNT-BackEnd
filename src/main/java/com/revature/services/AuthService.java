package com.revature.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dtos.ChangePasswordRequest;
import com.revature.dtos.login.LoginRequest;
import com.revature.dtos.login.LoginResponse;
import com.revature.dtos.registration.RegisterRequest;
import com.revature.models.Product;
import com.revature.models.User;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.dynamic.DynamicType;
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

    public Optional<Boolean> updatePassword(ChangePasswordRequest updatePassword){
        String oldPassword = updatePassword.getOldPassword();
        User user = userService.findUserByEmail(updatePassword.getEmail());


        if(passwordEncoder.matches(oldPassword,user.getPassword())){
            String newPassword = passwordEncoder.encode(updatePassword.getNewPassword());
            user.setPassword(newPassword);
            userService.save(user);
            return Optional.of(new Boolean(true));
        }else {
            return Optional.of(new Boolean(false));
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
