/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.chat;

import com.example.chat.model.ChatMessage;
import com.example.chat.model.Room;
import jakarta.persistence.PostRemove;
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
    private void afterRoomDelete(Room room) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.REMOVE);
        simpMessagingTemplate.convertAndSend(String.format("/room/%d", room.getCreatorid())
                ,chatMessage);
    }
    
}
