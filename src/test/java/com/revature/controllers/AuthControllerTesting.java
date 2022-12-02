package com.revature.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dtos.LoginRequest;
import com.revature.dtos.RegisterRequest;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.services.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class AuthControllerTesting {
    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthService authService;
    @Mock
    private ObjectMapper objectMapper;
//    @Mock
//    private HttpSession session;

    private LoginRequest loginRequest;
    private LoginRequest badLoginRequest;
    private HttpSession session;
    private User newUser;
    private User dbUser;
    private Optional<User> optDbUser;
    private RegisterRequest registerRequest;
    private String email = "bigboy@sally.forth";
    private String password = "password";
    private String wrongPassword = "passedword";
    private String newPassword = "pastwyrdt";
    private String firstName = "Grimbo";
    private String lastName = "McFulstrom";
    private ArrayList<Product> wishlist;
    private ArrayList<Product> wishlistUpd;
    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    public void populateObjects(){
        loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);
        badLoginRequest = new LoginRequest();
        badLoginRequest.setEmail(email);
        badLoginRequest.setPassword(wrongPassword);

        newUser = new User();
        newUser.setId(0);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setWishList(wishlist);

        dbUser = new User();
        dbUser.setId(1);
        dbUser.setEmail(email);
        dbUser.setPassword(password);
        dbUser.setFirstName(firstName);
        dbUser.setLastName(lastName);
        dbUser.setWishList(wishlist);
        optDbUser = Optional.ofNullable(dbUser);

        //populate products
        product1 = new Product();
        product1.setId(1);
        product1.setQuantity(10);
        product1.setPrice(10.00);
        product1.setDescription("A good ol' time Jones");
        product1.setImage("imagelocation.jpg");
        product1.setName("Grek");
        product1.setFeatured(true);
        product2 = new Product();
        product2.setId(2);
        product2.setQuantity(8);
        product2.setPrice(19.89);
        product2.setDescription("Flattery whereabouts, tha?");
        product2.setImage("imagelocation2.jpg");
        product2.setName("Skronk");
        product2.setFeatured(false);
        product3 = new Product();
        product3.setId(3);
        product3.setQuantity(8);
        product3.setPrice(19.89);
        product3.setDescription("Being'st frontward, never not shan't.");
        product3.setImage("imagelocation3.jpg");
        product3.setName("Plingo");
        product3.setFeatured(true);

        wishlist = new ArrayList<Product>();
        wishlist.add(product1);

        wishlistUpd = wishlist;
        wishlistUpd.add(product2);

        session = new MockHttpSession();

        registerRequest = new RegisterRequest();
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);
        registerRequest.setFirstName(firstName);
        registerRequest.setLastName(lastName);

    }




    @Test
    public void givenLoginRequestAndSession_login_ReturnsResponseEntityUser(){
        Mockito.when(authService.findByCredentials(loginRequest.getEmail(), loginRequest.getPassword()))
                .thenReturn(Optional.ofNullable(dbUser));

        ResponseEntity<User> responseEntity = authController.login(loginRequest,session);

        Assertions.assertEquals(ResponseEntity.ok(optDbUser.get()), responseEntity);
    }

    @Test
    public void givenBadLoginRequestAndSession_login_returnsBadRequest(){
        Mockito.when(authService.findByCredentials(badLoginRequest.getEmail(), badLoginRequest.getPassword()))
                .thenReturn(Optional.empty());

        ResponseEntity<User> responseEntity = authController.login(badLoginRequest,session);

        Assertions.assertEquals(ResponseEntity.badRequest().build(), responseEntity);

    }


    @Test
    public void givenSession_logout_returnsResponseEntityTrue(){
        ResponseEntity<Boolean> response = authController.logout(session);

        Assertions.assertEquals(ResponseEntity.ok(true), response);
    }

    @Test
    public void givenRegisterRequest_register_returnsResponseEntityUser(){
        Mockito.when(authService.register(newUser)).thenReturn(dbUser);

        ResponseEntity<User> responseEntity = authController.register(registerRequest);

        Assertions.assertEquals(ResponseEntity.status(HttpStatus.CREATED).body(dbUser), responseEntity);
    }

    @Test
    public void givenUser_updateUser_returnsResponseEntityUser(){
        User updUser = dbUser;
        updUser.setWishList(wishlistUpd);
        Mockito.when(authService.findUserById(1)).thenReturn(dbUser);
        Mockito.when(authService.register(updUser)).thenReturn(updUser);

        ResponseEntity<User> responseEntity = authController.updateUser(updUser);

        Assertions.assertEquals(ResponseEntity.status(HttpStatus.CREATED).body(updUser),responseEntity);
    }

    @Test
    public void givenNull_getAllFeaturedProducts_returnsResponseEntityListOfProducts(){
        List<Product> featuredProducts = new ArrayList<>();
        featuredProducts.add(product1);
        featuredProducts.add(product3);

        Mockito.when(authService.findAllByFeaturedTrue()).thenReturn(featuredProducts);

        ResponseEntity<List<Product>> responseEntity = authController.getAllFeaturedProducts();

        List<Product> returnedList = responseEntity.getBody();

        for(int i=0; i<featuredProducts.size(); i++) {
            Assertions.assertEquals(featuredProducts.get(i), returnedList.get(i));
        }
    }

    @Test
    public void givenEmailPasswordNewPasswordSession_updatePassword_returnsResponseEntityUser(){
        User updUser = dbUser;
        updUser.setPassword(newPassword);

        Mockito.when(authService.updatePassword(email, password, newPassword)).thenReturn(Optional.of(updUser));

        ResponseEntity<User> responseEntity = authController.updatePassword(email, password, newPassword,session);

        Assertions.assertEquals(ResponseEntity.ok().build(), responseEntity);

    }


    @Test
    public void givenEmailWrongPasswordNewPasswordSession_updatePassword_returnsResponseEntityUser(){

        Mockito.when(authService.updatePassword(email, wrongPassword, newPassword)).thenReturn(Optional.empty());

        ResponseEntity<User> responseEntity = authController.updatePassword(email, password, newPassword,session);

        Assertions.assertEquals(ResponseEntity.badRequest().build(), responseEntity);

    }


}
