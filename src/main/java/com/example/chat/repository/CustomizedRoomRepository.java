/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.chat.repository;

import com.example.chat.model.ChatMessage;
import com.example.chat.model.Room;
import com.example.chat.model.User;
import java.util.Optional;

/**
 *
 * @author dlyad
 */
public interface CustomizedRoomRepository{
    void addToUsers(User user, Long id);
    void removeFromUsers(User user, Long id);
    void addToMessages(ChatMessage chatMessage, Long id);
    void removeFromMessages(ChatMessage chatMessage, Long id);
}
