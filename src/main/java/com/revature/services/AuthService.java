package com.revature.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dtos.login.LoginRequest;
import com.revature.dtos.login.LoginResponse;
import com.revature.dtos.registration.RegisterRequest;
import com.revature.dtos.registration.RegistrationResponse;
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
    public RegistrationResponse register(RegisterRequest registerRequest) {
        String password = registerRequest.getPassword();
        password = passwordEncoder.encode(password);
        registerRequest.setPassword(password);
        User user = objectMapper.convertValue(registerRequest, User.class);
        return generateRegisterResponse(userService.save(user));
    }

    private RegistrationResponse generateRegisterResponse(User user){
        return objectMapper.convertValue(user,RegistrationResponse.class);
    }
    //End of registration methods.


    //Start of login methods.
    public LoginResponse userLogin(LoginRequest loginRequest){
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),loginRequest.getPassword()
        ));

        return null;
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

    public User testChangePassword(String email,String oldPassword,String newPassword){
        return updatePassword(email,oldPassword,newPassword)
                .orElseThrow(() -> new NullPointerException("User password could not be changed"));
    }

    //End of change user password logic.

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findUserByEmail(username);
    }
}
