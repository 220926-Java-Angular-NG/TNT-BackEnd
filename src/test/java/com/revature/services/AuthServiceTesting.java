package com.revature.services;

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

    private User newUser;
    private User dbUser;
    private Optional<User> optDbUser;
    private User upDbUser;
    private Optional<User> optUpDbUser;

    private int id = 1;
    private String email = "bigboy@sally.forth";
    private String password = "password";
    private String newPassword = "newpassword";
    private String wrongPassword = "wrongpassword";
    private String firstName = "Grimbo";
    private String lastName = "McFulstrom";
    private List<Product> products;


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
        dbUser.setPassword(password);
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
        Mockito.when(userService.save(newUser)).thenReturn(dbUser);

        User createdUser = authService.register(newUser);

        Assertions.assertEquals(dbUser.getId(), createdUser.getId());
        Assertions.assertEquals(dbUser.getEmail(), createdUser.getEmail());
        Assertions.assertEquals(dbUser.getPassword(), createdUser.getPassword());
        Assertions.assertEquals(dbUser.getFirstName(), createdUser.getFirstName());
        Assertions.assertEquals(dbUser.getLastName(), createdUser.getLastName());

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
    public void givenEmailAndPasswordAndNewPassword_updatePassword_returnsOptUser(){
        Mockito.when(userService.findUserByEmail(email)).thenReturn(dbUser);
        Mockito.when(userService.save(dbUser)).thenReturn(upDbUser);
        Mockito.when(userService.findByCredentials(email,newPassword)).thenReturn(optUpDbUser);

        Optional<User> returnedUser = authService.updatePassword(email,password,newPassword);

        Assertions.assertEquals(optUpDbUser.get().getId(), returnedUser.get().getId());
        Assertions.assertEquals(optUpDbUser.get().getEmail(), returnedUser.get().getEmail());
        Assertions.assertEquals(optUpDbUser.get().getPassword(), returnedUser.get().getPassword());
        Assertions.assertEquals(optUpDbUser.get().getFirstName(), returnedUser.get().getFirstName());
        Assertions.assertEquals(optUpDbUser.get().getLastName(), returnedUser.get().getLastName());

    }

    @Test
    public void givenEmailAndWrongPasswordAndNewPassword_updatePassword_returnsNull() {
        Mockito.when(userService.findUserByEmail(email)).thenReturn(dbUser);
        Mockito.when(userService.save(dbUser)).thenReturn(upDbUser);
        Mockito.when(userService.findByCredentials(email, newPassword)).thenReturn(optUpDbUser);

        Optional<User> returnedUser = authService.updatePassword(email, wrongPassword, newPassword);

        Assertions.assertNull(returnedUser);
    }

    }
