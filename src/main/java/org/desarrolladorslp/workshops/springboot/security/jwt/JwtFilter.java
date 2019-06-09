package org.desarrolladorslp.workshops.springboot.security.jwt;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.desarrolladorslp.workshops.springboot.security.RequestUtils;
import org.desarrolladorslp.workshops.springboot.security.TokenBasedAuthentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final RequestUtils requestUtils;

    public JwtFilter(TokenProvider tokenProvider,
                     RequestUtils requestUtils) {
        this.tokenProvider = tokenProvider;
        this.requestUtils = requestUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestToken = requestUtils.getToken(request);
        if (StringUtils.hasText(requestToken) && tokenProvider.isValidToken(requestToken)) {

            final Collection<? extends GrantedAuthority> authorities = tokenProvider.getAuthorities(requestToken);
            final User principal = new User(tokenProvider.getSubject(requestToken), "", authorities);

            final TokenBasedAuthentication tba = new TokenBasedAuthentication(principal, requestToken);
            tba.setAuthenticated(true);
            // Use SecurityContextHolder as we did not define an AuthenticationProvider for an
            // AuthenticationManager to use.
            // Here we manually set the authentication.
            SecurityContextHolder.getContext().setAuthentication(tba);

        }

        filterChain.doFilter(request, response);
    }
}
