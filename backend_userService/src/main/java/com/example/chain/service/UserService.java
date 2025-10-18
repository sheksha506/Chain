package com.example.chain.service;

import com.example.chain.dto.CoordinatesDto;
import com.example.chain.entity.User;

import java.util.List;

public interface UserService {

    public User saveUser(User user);
    public User updateUser(String email, Double latitude, Double longitude);
    List<User> getUsers();
    List<User> getUsersWithinRange(Double latitude, Double longitude, Double range);
}
