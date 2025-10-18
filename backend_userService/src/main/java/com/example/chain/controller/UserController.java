package com.example.chain.controller;


import com.example.chain.dto.CoordinatesDto;
import com.example.chain.dto.JwtAuth;
import com.example.chain.dto.LoginDto;
import com.example.chain.dto.SignupDTO;
import com.example.chain.entity.User;
import com.example.chain.jwt.JwtTokenProvider;
import com.example.chain.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping(value = "/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;



    @PostMapping("/signup")
    public ResponseEntity<String> register(@Valid @RequestBody SignupDTO signupDTO) {
        try {
            User user = new User();
            user.setPassword(signupDTO.getPassword());
            user.setEmail(signupDTO.getEmail());
            user.setUsername(signupDTO.getUsername());
            user.setLatitude(signupDTO.getLatitude());
            user.setLongitude(signupDTO.getLongitude());

            userService.saveUser(user);
            return new ResponseEntity<>("User successfully registered", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new JwtAuth(token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }


    @PatchMapping("/update")
    public ResponseEntity<Void> updateCoordinates(@RequestBody CoordinatesDto coordinatesDto){
        userService.updateUser(coordinatesDto.getEmail(), coordinatesDto.getLatitude(), coordinatesDto.getLongitude());
        return ResponseEntity.noContent().build();


    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }


    @GetMapping("/nearby")
    public ResponseEntity<List<User>> getNearbyUsers(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "100") double range
    ) {
        List<User> nearbyUsers = userService.getUsersWithinRange(latitude, longitude, range);
        return ResponseEntity.ok(nearbyUsers);
    }




}
