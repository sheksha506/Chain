package com.example.chain.serviceImp;

import com.example.chain.entity.User;
import com.example.chain.repo.UserRespository;
import com.example.chain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserSerivceImp implements UserService {

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public User saveUser(User user) {

        if(userRespository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        return userRespository.save(user);
    }

    @Override
    public User updateUser(String email, Double latitude, Double longitude) {
        User user = userRespository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("User Not found"));

        user.setLatitude(latitude);
        user.setLongitude(longitude);
         return userRespository.save(user);


    }

    @Override
    public List<User> getUsers() {
        return userRespository.findAll();
    }

    @Override
    public List<User> getUsersWithinRange(Double latitude, Double longitude, Double range) {
        List<User> allUsers = userRespository.findAll();
        List<User> nearbyUsers = new ArrayList<>();

        for (User user : allUsers) {
            double distance = haversine(latitude, longitude, user.getLatitude(), user.getLongitude());
            if (distance <= range) {
                nearbyUsers.add(user);
            }
        }

        return nearbyUsers;
    }



    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Earth radius in meters

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // distance in meters
    }
}
