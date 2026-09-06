package com.test.task.controller;

import com.test.task.exception.EmailAlreadyExistsException;
import com.test.task.model.LoginRequest;
import com.test.task.model.LoginResponse;
import com.test.task.model.User;
import com.test.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Registro de usuario
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User request) {
        try {
            User user = userService.save(request);
            return ResponseEntity.ok(user);
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }

    // Login de usuario
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = userService.login(request.getEmail(), request.getPassword());

        if (token != null) {
            return ResponseEntity.ok(new LoginResponse(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas");
        }
    }
}
