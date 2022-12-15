package com.github.sbooster.templates.backend.util;

import com.github.sbooster.templates.backend.rsocket.security.model.StoredUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class JwtUtils {
    public static final String SECRET = "BE420D1C47A49F458D6E76FC3ACA3C96";

    public static String create(StoredUserDetails userDetails, Duration expireAfter) {
        Instant now = Instant.now();
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userDetails.getId());
        claims.put("ema", userDetails.getUsername());
        claims.put("pwd", userDetails.getPassword());
        claims.put("typ", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", ")));
        claims.put("iat", now.toEpochMilli());
        claims.put("eat", ((Instant) expireAfter.addTo(now)).toEpochMilli());
        return Jwts.builder().setHeaderParam("typ", "JWT").setClaims(claims).signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    public static boolean isValid(String token, StoredUserDetails userDetails) {
        Claims claims = parseClaimsJwsBody(token);
        long now = Instant.now().toEpochMilli();
        long expireAt = Long.parseLong(String.valueOf(claims.get("eat")));
        long realSubject = userDetails.getId();
        long subject = Long.parseLong(String.valueOf(claims.get("sub")));
        String realUsername = userDetails.getUsername();
        String username = String.valueOf(claims.get("ema"));
        String realPassword = userDetails.getPassword();
        String password = String.valueOf(claims.get("pwd"));
        String realAuthorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", "));
        String authorities = String.valueOf(claims.get("typ"));
        return expireAt > now &&
                Objects.equals(subject, realSubject) &&
                Objects.equals(username, realUsername) &&
                Objects.equals(password, realPassword) &&
                Objects.equals(authorities, realAuthorities);
    }

    public static Claims parseClaimsJwsBody(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    public static Long parseSubject(String token) {
        return Long.parseLong(String.valueOf(parseClaimsJwsBody(token).get("sub")));
    }
}
