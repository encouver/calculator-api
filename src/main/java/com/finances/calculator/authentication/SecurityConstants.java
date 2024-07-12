package com.finances.calculator.authentication;

import org.springframework.stereotype.Component;

/**
 * @author Marcos Ramirez
 */
@Component
public class SecurityConstants {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String SIGN_UP_URL = "/api/v1/users/register";

}
