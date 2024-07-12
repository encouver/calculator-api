package com.finances.calculator.controllers;

import com.finances.calculator.dto.UserDTO;
import com.finances.calculator.services.RecordsService;
import com.finances.calculator.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author Marcos Ramirez
 */
@RestController
@RequestMapping("/api/v1/users")
public class UsersControllerV1 {

    @Autowired
    private UsersService usersService;

    @Autowired
    private RecordsService recordsService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {

        String token = null;
        try {
            token = usersService.loginUser(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        if(token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid username or password");
        }

        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(@RequestBody UserDTO userDTO) {

        Boolean result = usersService.logoutUser(userDTO);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {

        usersService.registerUser(userDTO);

        return ResponseEntity.ok("Register V1");
    }

    @GetMapping("/balance")
    public ResponseEntity<Double> getBalance() {
        return ResponseEntity.ok(recordsService.getBalance());
    }

}


