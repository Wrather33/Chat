/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.chat.repository;

import com.example.chat.model.Room;
import com.example.chat.model.User;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dlyad
 */
@Repository
@Transactional
public interface RoomRepository extends JpaRepository<Room, Long>, CustomizedRoomRepository{
    Optional<Room> findByCreatorid(Long id);
    Optional<Room> deleteByCreatorid(Long id);
}
