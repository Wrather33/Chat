/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.chat.repository;

import com.example.chat.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author dlyad
 */
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    
    
}
