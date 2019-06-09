package org.desarrolladorslp.workshops.springboot.security.jwt;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.desarrolladorslp.workshops.springboot.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private final String AUTHORITIES_KEY = "auth";
    public TokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token");
        } catch (MalformedJwtException e) {
            log.info("Malformed JWT token");
        } catch (SignatureException e) {
            log.info("Invalid JWT token");
        } catch (IllegalArgumentException e) {
            log.info("Invalid argument for JWT token");
        }
        return false;
    }

    public String generateToken(User user) {

        final String authorities = user
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        final Date issuedAt = new Date();
        final Date expiration = new Date(issuedAt.getTime() + (1000 * jwtProperties.getExpireLength()));

        return Jwts.builder()
                .setHeaderParam("alg", SIGNATURE_ALGORITHM.getValue())
                .setHeaderParam("typ", "JWT")
                .setIssuer(jwtProperties.getIssuer())
                .setSubject(user.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(SIGNATURE_ALGORITHM, jwtProperties.getSecretKey())
                .compact();

    }

    public String refreshToken(String token) {

        final Date newIssuedAt = new Date();
        final Date newExpiration = new Date(newIssuedAt.getTime() + (1000 * jwtProperties.getExpireLength()));

        final Claims claims = Jwts.parser().setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token).getBody();
        claims.setIssuedAt(newIssuedAt);
        claims.setExpiration(newExpiration);

        return Jwts.builder()
                .setHeaderParam("alg", SIGNATURE_ALGORITHM.getValue())
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .signWith(SIGNATURE_ALGORITHM, jwtProperties.getSecretKey())
                .compact();

    }

    public Collection<? extends GrantedAuthority> getAuthorities(String token) {
        final Claims claims = Jwts.parser().setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token).getBody();

        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return authorities;
    }

    public String getSubject(String token) {
        return Jwts.parser().setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token).getBody().getSubject();
    }

}
