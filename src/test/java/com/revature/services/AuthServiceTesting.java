package com.revature.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dtos.ChangePasswordRequest;
import com.revature.dtos.login.LoginRequest;
import com.revature.dtos.login.LoginResponse;
import com.revature.dtos.registration.RegisterRequest;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class AuthServiceTesting {

    @InjectMocks
    private AuthService authService;
    @Mock
    private UserService userService;
    @Mock
    private ProductService productService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private JWTService jwtService;

    private User newUser;
    private User dbUser;
    private Optional<User> optDbUser;
    private User upDbUser;
    private Optional<User> optUpDbUser;
    private RegisterRequest registerRequest;
    private ChangePasswordRequest changePasswordRequest;
    private ChangePasswordRequest badChangePasswordRequest;

    private int id = 1;
    private String email = "bigboy@sally.forth";
    private String password = "password";
    private String newPassword = "newpassword";
    private String wrongPassword = "wrongpassword";
    private String firstName = "Grimbo";
    private String lastName = "McFulstrom";
    private List<Product> products;
    private Boolean aBoolean = true;
    private Boolean nayBoolen = false;
    private PasswordEncoder newPassEnc = new BCryptPasswordEncoder();
    private LoginRequest loginRequest;
    private LoginResponse loginResponse;
    private String token;

    @BeforeEach
    public void populateUserObjects(){
        newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);


        dbUser = new User();
        dbUser.setId(id);
        dbUser.setEmail(email);
        dbUser.setPassword(newPassEnc.encode(password));
        dbUser.setFirstName(firstName);
        dbUser.setLastName(lastName);
        optDbUser = Optional.ofNullable(dbUser);

        upDbUser = new User();
        upDbUser.setId(1);
        upDbUser.setEmail(email);
        upDbUser.setPassword(newPassword);
        upDbUser.setFirstName(firstName);
        upDbUser.setLastName(lastName);
        optUpDbUser = Optional.ofNullable(upDbUser);

        registerRequest = new RegisterRequest();
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);
        registerRequest.setFirstName(firstName);
        registerRequest.setLastName(lastName);

        changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setEmail(email);
        changePasswordRequest.setOldPassword(password);
        changePasswordRequest.setNewPassword(newPassword);
        badChangePasswordRequest = changePasswordRequest;
        badChangePasswordRequest.setOldPassword(wrongPassword);

        loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        token = "token";

        loginResponse = new LoginResponse();
        loginResponse.setId(1);
        loginResponse.setEmail(email);
        loginResponse.setToken(token);
        loginResponse.setFirstName(firstName);
        loginResponse.setLastName(lastName);
        loginResponse.setWishList(new ArrayList());
    }

    @Test
    public void givenEmailAndPassword_findByCredentials_returnsOptUser(){
        Mockito.when(userService.findByCredentials(email,password)).thenReturn(optDbUser);

        Optional<User> returnedUser = authService.findByCredentials(email,password);

        Assertions.assertEquals(optDbUser.get().getId(), returnedUser.get().getId());
        Assertions.assertEquals(optDbUser.get().getEmail(), returnedUser.get().getEmail());
        Assertions.assertEquals(optDbUser.get().getPassword(), returnedUser.get().getPassword());
        Assertions.assertEquals(optDbUser.get().getFirstName(), returnedUser.get().getFirstName());
        Assertions.assertEquals(optDbUser.get().getLastName(), returnedUser.get().getLastName());
    }

    @Test
    public void givenNewUser_register_returnsCreatedEntity(){
        Mockito.when(passwordEncoder.encode(password)).thenReturn(password);
        Mockito.when(objectMapper.convertValue(registerRequest, User.class)).thenReturn(newUser);
        Mockito.when(userService.save(newUser)).thenReturn(dbUser);

        Boolean returnedValue = authService.register(registerRequest);

        Assertions.assertEquals(aBoolean, returnedValue);

    }



    @Test
    public void givenNull_findAllByFeaturedTrue_returnsListOfProducts(){
        Mockito.when(productService.findAllByFeaturedTrue()).thenReturn(products);

        List<Product> newProducts = authService.findAllByFeaturedTrue();

        Assertions.assertEquals(products, newProducts);
    }

    @Test
    public void givenId_findUserById_returnsUser(){
        Mockito.when(userService.findUserById(id)).thenReturn(dbUser);

        User foundUser = authService.findUserById(id);

        Assertions.assertEquals(dbUser.getId(), foundUser.getId());
        Assertions.assertEquals(dbUser.getEmail(), foundUser.getEmail());
        Assertions.assertEquals(dbUser.getPassword(), foundUser.getPassword());
        Assertions.assertEquals(dbUser.getFirstName(), foundUser.getFirstName());
        Assertions.assertEquals(dbUser.getLastName(), foundUser.getLastName());

    }

    @Test
    public void givenEmailAndPasswordAndNewPassword_updatePassword_returnsOptTrue(){
        Mockito.when(userService.findUserByEmail(email)).thenReturn(dbUser);
        Mockito.when(passwordEncoder.matches(changePasswordRequest.getOldPassword(), dbUser.getPassword())).thenReturn(true);
        Mockito.when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);
        Mockito.when(userService.save(upDbUser)).thenReturn(upDbUser);
        Mockito.when(userService.findByCredentials(email,newPassword)).thenReturn(optUpDbUser);

        Optional<Boolean> returnedValue = authService.updatePassword(changePasswordRequest);

        Assertions.assertEquals(Optional.of(aBoolean), returnedValue);

    }





    @Test
    public void givenEmailAndWrongPasswordAndNewPassword_updatePassword_returnsOptFalse() {

        Mockito.when(userService.findUserByEmail(email)).thenReturn(dbUser);
        Mockito.when(passwordEncoder.matches(wrongPassword,password)).thenReturn(false);
        Mockito.when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);
//        Mockito.when(userService.save(dbUser)).thenReturn(upDbUser);
//        Mockito.when(userService.findByCredentials(email, newPassword)).thenReturn(optUpDbUser);

        Optional<Boolean> returnedValue = authService.updatePassword(badChangePasswordRequest);

        Assertions.assertEquals(Optional.of(nayBoolen), returnedValue);
    }

    @Test
    public void givenValidLoginRequest_login_returnOptUser(){
        Mockito.when(userService.findUserByEmail(loginRequest.getEmail())).thenReturn(dbUser);
        Mockito.when(passwordEncoder.matches(loginRequest.getPassword(), dbUser.getPassword())).thenReturn(true);
        Mockito.when(jwtService.generateToken(dbUser)).thenReturn(token);

        Optional<LoginResponse> returnedResponse = authService.userLogin(loginRequest);

        Assertions.assertEquals(returnedResponse.get().getId(), dbUser.getId());
        Assertions.assertEquals(returnedResponse.get().getToken(), token);
    }

    @Test
    public void givenInvalidLoginRequest_login_returnEmptyOpt(){
        Mockito.when(userService.findUserByEmail(loginRequest.getEmail())).thenReturn(dbUser);
        Mockito.when(passwordEncoder.matches(wrongPassword, dbUser.getPassword())).thenReturn(false);

        Optional<LoginResponse> returnedResponse = authService.userLogin(loginRequest);

        Assertions.assertEquals(returnedResponse.isPresent(), false);
    }

    }
