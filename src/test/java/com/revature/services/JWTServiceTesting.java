package com.revature.services;

import com.revature.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class JWTServiceTesting {

    @InjectMocks
    private JWTService jwtService;
    @Mock
    private JWTService jwtSwervice;
    @Mock
    private PasswordEncoder passwordEncoder;


    private String THAT_NULL_SECRET_KEY = "ThatNullTeam";
    private Long HOUR = (1000L * 60 * 60);

    private User user;
    private int id = 1;
    private String email = "mhanson@gmail.com";
    private String password = "password";
    private String newPassword = "newpassword";
    private String wrongPassword = "wrongpassword";
    private String firstName = "matt";
    private String lastName = "hanson";
    private String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtaGFuc29uQGdtYWlsLmNvbSIsImV4cCI6MTY3MDM1NDc5NCwiaWF0IjoxNjcwMjY4Mzk0fQ.wyrV56WhYsA04WWVtndIRpnoqsEIIvp8ypuaccv29y8";

    Map<String,Object> claims = new HashMap<String,Object>();

    @BeforeEach
    public void populateUserObjects(){

        user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);

    }

   @Test
   public void givenTokenAndUser_validateToken_returnsBooleanTrue(){
       Mockito.when(jwtSwervice.extractEmail(token)).thenReturn(email);
       Mockito.when(jwtSwervice.isTokenExpired(token)).thenReturn(false);

       Boolean result = jwtService.validateToken(token, user);

       Assertions.assertEquals(true,result);

   }

   @Test
    public void givenUser_generateToken_returnsTokenString(){

       String tokent = Jwts.builder().setClaims(claims)
               .setSubject(email)
               .setExpiration(new Date(System.currentTimeMillis()+ (24 * HOUR)))
               .setIssuedAt(new Date(System.currentTimeMillis()))
               .signWith(SignatureAlgorithm.HS256,THAT_NULL_SECRET_KEY).compact();
       
        Mockito.when(jwtSwervice.generateToken(claims, user.getEmail())).thenReturn(tokent);

        String tanket = jwtService.generateToken(user);

        Assertions.assertEquals(tokent, tanket);

   }





}
