/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.chat;

import com.example.chat.model.ChatMessage;
import com.example.chat.model.User;
import com.example.chat.repository.RoomRepository;
import com.example.chat.security.UserDetailsServiceImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 *
 * @author dlyad
 */
@Component
public class WebSocketEventListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    private final RoomRepository roomRepository;
    private final UserDetailsServiceImpl UserDetailsServiceImpl;
    @Autowired
    public WebSocketEventListener(RoomRepository roomRepository, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.roomRepository = roomRepository;
        this.UserDetailsServiceImpl = userDetailsServiceImpl;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event)
            throws IOException
   {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());  
        if(headerAccessor.getSessionAttributes().get("room") != null){
        Long room = Long.valueOf(headerAccessor.getSessionAttributes().get("room").toString());
        Optional<User> user = UserDetailsServiceImpl.getUserRepository().findByEmail(event.getUser().getName());
        User result = user.get();
        if(roomRepository.findByCreatorid(room).isPresent()){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.LEAVE);
        chatMessage.setSender(String.format("%s %s", result.getFirstName(),
                result.getLastName()));
        chatMessage.setId(String.valueOf(result.getId()));
        chatMessage.setPhoto(result.generateImage());
        roomRepository.removeFromUsers(result,room);
        messagingTemplate.convertAndSend(String.format("/room/%d", room), chatMessage);
        }
    }
   }
}