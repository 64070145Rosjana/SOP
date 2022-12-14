package com.example.authservice.services;


import com.example.authservice.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expirationTime;

    private Key key;

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());

    }

    public Claims getAllClaimFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Date getExpirationDateFromToken(String token){
        return getAllClaimFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generate(User userV0, String type){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userV0.getId());
        claims.put("role", userV0.getRole());
        return doGenerateToken(claims, userV0.getEmail(), type);
    }

    private String doGenerateToken(Map<String, Object> claims, String username, String type){
        long exppirationTimeLong;
        if("ACCESS".equals(type)){
            exppirationTimeLong = Long.parseLong(expirationTime) * 100;

        }else{
            exppirationTimeLong = Long.parseLong(expirationTime) * 100 * 5;
        }
        final Date createDate = new Date();
        final Date expirationDate = new Date(createDate.getTime() + exppirationTimeLong);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }
}
