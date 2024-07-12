package com.finances.calculator.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.finances.calculator.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.ArrayList;


/**
 * @author Marcos Ramirez
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Value("${JWT_SECRET}")
    private String secret;

    public JWTAuthorizationFilter(AuthenticationManager authManager, JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsServiceImpl) {
        super(authManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(SecurityConstants.AUTHORIZATION_HEADER);

        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
        if (token != null) {
            // parse the token.
            token = token.replace(SecurityConstants.TOKEN_PREFIX, "");

            try {
                String username = jwtUtil.extractUsername(token);

                // Verify token using JwtUtil's verifyToken method
                if (username != null && jwtUtil.verifyToken(token, userDetailsServiceImpl.loadUserByUsername(username))) {
                    return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                }

//                String user = JWT.require(Algorithm.HMAC512(secret.getBytes()))
//                        .build()
//                        .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
//                        .getSubject();
//                jwtUtil.verifyToken()
//
//                //String user = jwtTokenUtil.getUsernameFromToken(token.replace(TOKEN_PREFIX, ""));
//
//                if (user != null) {
//                    return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
//                }
            }catch (Exception e){
                System.out.println("Error verifying JWT: ");
                System.out.println(e.getMessage());
            }
            return null;
        }
        return null;
    }

}
