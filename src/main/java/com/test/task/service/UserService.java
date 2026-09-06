package com.test.task.service;

import com.test.task.exception.EmailAlreadyExistsException;
import com.test.task.model.User;
import com.test.task.repository.UserRepository;
import com.test.task.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("El correo ya está registrado");
        }

        User newUser = new User();
        newUser.setNombre(request.getNombre());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(newUser);
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return JwtUtil.generateToken(user.getEmail());
        }

        return null;
    }
}