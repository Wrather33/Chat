/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.chat;

import com.example.chat.model.ChatMessage;
import com.example.chat.model.Room;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 *
 * @author dlyad
 */
public class RoomListener {
     @Autowired
private SimpMessagingTemplate simpMessagingTemplate;
  
    @PostRemove
    private void afterRoomDeleted(Room room) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.REMOVE);
        simpMessagingTemplate.convertAndSend("/success", "delete");
        simpMessagingTemplate.convertAndSend(String.format("/room/%d", room.getCreatorid())
                ,chatMessage);
    }
    @PostPersist
    private void afterRoomCreated(Room room){
                simpMessagingTemplate.convertAndSend("/success", "create");
    }
    @PostUpdate
    private void afterRoomUpdated(Room room){
                simpMessagingTemplate.convertAndSend("/success", "update");
    }
    
}
