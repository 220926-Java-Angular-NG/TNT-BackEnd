package com.revature.services;

import com.revature.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import io.jsonwebtoken.Claims;

@Service
public class JWTService {

    private final String THAT_NULL_SECRET_KEY = "ThatNullTeam";
    private final Long HOUR = (1000L * 60 * 60);

    public Boolean validateToken(String token, User user){
        String username = extractEmail(token);
        return (!isTokenExpired(token) && username.equals(user.getEmail()));
    }

    public String extractEmail(String token){
        return extractClaim(token,Claims::getSubject);
    }

    private Date extractDate(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    private <T> T extractClaim(String token,Function<Claims,T> claimsResolver){
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(THAT_NULL_SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token){
        return extractDate(token).before(new Date());
    }

    public String generateToken(User user){
        Map<String,Object> claims = new HashMap<String,Object>();
        return generateToken(claims,user.getEmail());
    }

    private String generateToken(Map<String,Object> claims,String subject){
        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis()+ (24 * HOUR)))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256,THAT_NULL_SECRET_KEY).compact();

    }

}
