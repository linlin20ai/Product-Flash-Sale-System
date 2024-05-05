package org.example.productflashsalesystem;


import io.jsonwebtoken.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.UUID;


@SpringBootTest
class ProductFlashSaleSystemApplicationTests {
    private static long time=1000*60*60*24;

    private static String signature="0020linxn!/";

    public static String Jwttext(){
        JwtBuilder jwtBuilder= Jwts.builder();
        String token=jwtBuilder
                //header
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS256")
                //payload
                .claim("username","lin")
                .claim("role","admin")
                .setSubject("admin-text")
                .setExpiration(new Date(System.currentTimeMillis()+time))
                .setId(UUID.randomUUID().toString())
                //signature
                .signWith(SignatureAlgorithm.HS512,signature)
                .compact();
        return token;
    }

    public static void passJwt(String token){
        JwtParser jwtParser=Jwts.parser();
        Jws<Claims> claimsJws= jwtParser.setSigningKey(signature).parseClaimsJws(token);
        Claims claims=claimsJws.getBody();
        System.out.println(claims);
        System.out.println(claims.get("username"));
        System.out.println(claims.get("role"));
        System.out.println(claims.getId());
        System.out.println(claims.getExpiration());
        System.out.println(claims.getSubject());
    }

    public static void main(String[] args) {
        String token=Jwttext();
        passJwt(token);
    }
}
