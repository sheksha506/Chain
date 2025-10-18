package com.example.message.service;



import com.example.message.service.ConnectedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    @Autowired
    private ConnectedUserService connectedUserService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        if (sha.getUser() != null) {
            String user = sha.getUser().getName();
            connectedUserService.addUser(user);
            System.out.println("WS Connected: " + user);
        } else {
            System.out.println("WS Connected: anonymous");
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        if (sha.getUser() != null) {
            String user = sha.getUser().getName();
            connectedUserService.removeUser(user);
            System.out.println("WS Disconnected: " + user);
        }
    }
}

