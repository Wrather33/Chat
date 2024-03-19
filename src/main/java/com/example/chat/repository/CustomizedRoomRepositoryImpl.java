/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.chat.repository;

import com.example.chat.model.ChatMessage;
import com.example.chat.model.Room;
import com.example.chat.model.User;
import jakarta.persistence.PreRemove;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 *
 * @author dlyad
 */
public class CustomizedRoomRepositoryImpl implements CustomizedRoomRepository{
    @Autowired
    @Lazy
    RoomRepository roomRepository; 
    
    @Override
    @Transactional
    public void addToMessages(ChatMessage chatMessage, Long id) {
         Optional<Room> room = roomRepository.findByCreatorid(id);
         room.get().getMessages().add(chatMessage);
    }

    @Override
    @Transactional
       public void removeFromMessages(ChatMessage chatMessage, Long id) {
         Optional<Room> room = roomRepository.findByCreatorid(id);
         room.get().getMessages().remove(chatMessage);
    }
    @Override
    @Transactional
    public void addToUsers(User user, Long id) {
         Optional<Room> room = roomRepository.findByCreatorid(id);
         if(!room.get().getUsers().contains(user))
         {room.get().getUsers().add(user);
         }
    }

    @Override
    @Transactional
       public void removeFromUsers(User user, Long id) {
         Optional<Room> room = roomRepository.findByCreatorid(id);
         if(room.get().getUsers().contains(user)){
             room.get().getUsers().remove(user);
         }
    }
    
}
