package org.desarrolladorslp.workshops.springboot.security;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public final class RequestUtils {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        return (StringUtils.hasText(token) && token.startsWith("Bearer ")) ?
                request.getHeader(AUTHORIZATION_HEADER).substring(7) :
                null;

    }

}