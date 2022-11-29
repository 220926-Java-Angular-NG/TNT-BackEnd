package com.revature.services;

import com.revature.models.User;
import com.revature.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class UserServiceTesting {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User newUser;
    private User dbUser;
    private Optional<User> optDbUser;

    private String email = "bigboy@sally.forth";
    private String password = "password";
    private String firstName = "Grimbo";
    private String lastName = "McFulstrom";


    @BeforeEach
    public void populateUserObjects(){
        newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);


        dbUser = new User();
        dbUser.setId(1);
        dbUser.setEmail(email);
        dbUser.setPassword(password);
        dbUser.setFirstName(firstName);
        dbUser.setLastName(lastName);
        optDbUser = Optional.ofNullable(dbUser);
    }


    @Test
    public void givenEmailAndPassword_findByCredentials_returnsOptionalUser(){
        Mockito.when(userRepository.findByEmailAndPassword(email, password)).thenReturn(optDbUser);

        Optional<User> returnedUser = userService.findByCredentials(email, password);

        Assertions.assertEquals(optDbUser.get().getId(), returnedUser.get().getId());
        Assertions.assertEquals(optDbUser.get().getEmail(), returnedUser.get().getEmail());
        Assertions.assertEquals(optDbUser.get().getPassword(), returnedUser.get().getPassword());
        Assertions.assertEquals(optDbUser.get().getFirstName(), returnedUser.get().getFirstName());
        Assertions.assertEquals(optDbUser.get().getLastName(), returnedUser.get().getLastName());

    }


    @Test
    public void givenNewUser_save_returnsCreatedEntity(){
        Mockito.when(userRepository.save(newUser)).thenReturn(dbUser);

        User createdUser = userService.save(newUser);

        Assertions.assertEquals(dbUser.getId(), createdUser.getId());
        Assertions.assertEquals(dbUser.getEmail(), createdUser.getEmail());
        Assertions.assertEquals(dbUser.getPassword(), createdUser.getPassword());
        Assertions.assertEquals(dbUser.getFirstName(), createdUser.getFirstName());
        Assertions.assertEquals(dbUser.getLastName(), createdUser.getLastName());

    }

    @Test
    public void givenEmail_findUserByEmail_returnsMatchingUser(){
        Mockito.when(userRepository.findByEmail(email)).thenReturn(dbUser);

        User foundUser = userService.findUserByEmail(email);

        Assertions.assertEquals(dbUser.getId(), foundUser.getId());
        Assertions.assertEquals(dbUser.getEmail(), foundUser.getEmail());
        Assertions.assertEquals(dbUser.getPassword(), foundUser.getPassword());
        Assertions.assertEquals(dbUser.getFirstName(), foundUser.getFirstName());
        Assertions.assertEquals(dbUser.getLastName(), foundUser.getLastName());

    }




}
