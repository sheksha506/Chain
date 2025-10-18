package com.example.message.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConnectedUserService {

    private final Set<String> connectedUsers = ConcurrentHashMap.newKeySet();

    public void addUser(String email) {
        connectedUsers.add(email);
        System.out.println("Connected users: " + connectedUsers);
    }

    public void removeUser(String email) {
        connectedUsers.remove(email);
        System.out.println("Connected users: " + connectedUsers);
    }

    public boolean isUserConnected(String email) {
        return connectedUsers.contains(email);
    }
}
