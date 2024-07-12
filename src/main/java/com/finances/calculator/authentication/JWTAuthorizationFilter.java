package com.finances.calculator.authentication;


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

    @Value("${JWT_SECRET_L}")
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

                if (username != null && jwtUtil.verifyToken(token, userDetailsServiceImpl.loadUserByUsername(username))) {
                    return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                }

            }catch (Exception e){
                System.out.println("Error verifying JWT: ");
                System.out.println(e.getMessage());
            }
            return null;
        }
        return null;
    }

}
