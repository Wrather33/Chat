/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.chat.controllers;

import com.example.chat.model.ChatMessage;
import com.example.chat.model.User;
import com.example.chat.repository.RoomRepository;
import com.example.chat.security.UserDetailsServiceImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 *
 * @author dlyad
 */
@Controller
public class ChatController {
    @Autowired
private SimpMessagingTemplate simpMessagingTemplate;
      private final RoomRepository roomRepository;
    private final UserDetailsServiceImpl UserDetailsServiceImpl;
    @Autowired
    public ChatController(UserDetailsServiceImpl UserDetailsServiceImpl
    , RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
        this.UserDetailsServiceImpl = UserDetailsServiceImpl;
    }
    
    @MessageMapping("/{id}/sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage
    ,   Authentication authentication, @DestinationVariable("id") Long id) {
       UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = UserDetailsServiceImpl.getUserRepository().findByEmail(userDetails.getUsername());
        User result = user.get();
        chatMessage.setId(String.valueOf(result.getId()));
        chatMessage.setDate(new Date().toString());
        chatMessage.setSender(String.format("%s %s", result.getFirstName(),
                result.getLastName()));
        roomRepository.addToMessages(chatMessage, id);
        simpMessagingTemplate.convertAndSend(String.format("/room/%d", id)
                ,chatMessage);
    }
     @MessageMapping("/{id}/joinRoom")
    public void addUser(
             Authentication authentication, @DestinationVariable("id") Long id
    , SimpMessageHeaderAccessor headerAccessor) 
    throws IOException{
         UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = UserDetailsServiceImpl.getUserRepository().findByEmail(userDetails.getUsername());
        User result = user.get();
        headerAccessor.getSessionAttributes().put("room", id);
        if(!roomRepository.findByCreatorid(id).get().getUsers().contains(result)){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(String.format("%s %s", result.getFirstName(),
                result.getLastName()));
        chatMessage.setPhoto(result.generateImage());
        chatMessage.setId(String.valueOf(result.getId()));
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        roomRepository.addToUsers(result, id);
        simpMessagingTemplate.convertAndSend(String.format("/room/%d", id)
                ,chatMessage);
    }
    }
    @MessageMapping("/{id}/sendFile")
    public void sendFile(@Payload ChatMessage chatMessage
    ,   Authentication authentication, @DestinationVariable("id") Long id){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = UserDetailsServiceImpl.getUserRepository().findByEmail(userDetails.getUsername());
        User result = user.get();
        chatMessage.setId(String.valueOf(result.getId()));
        chatMessage.setDate(new Date().toString());
        chatMessage.setSender(String.format("%s %s", result.getFirstName(),
                result.getLastName()));
        simpMessagingTemplate.convertAndSend(String.format("/room/%d", id)
                ,chatMessage);
        
    }
}