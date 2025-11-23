package com.kauanallyson.rifa_hub_api.controllers;

import com.kauanallyson.rifa_hub_api.dtos.auth.LoginRequest;
import com.kauanallyson.rifa_hub_api.dtos.auth.LoginResponse;
import com.kauanallyson.rifa_hub_api.dtos.auth.RegisterRequest;
import com.kauanallyson.rifa_hub_api.entities.User;
import com.kauanallyson.rifa_hub_api.repositories.UserRepository;
import com.kauanallyson.rifa_hub_api.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest data){
        if(this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        User newUser = new User(data.login(), encryptedPassword);

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}