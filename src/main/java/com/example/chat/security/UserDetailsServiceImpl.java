/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.chat.security;

import com.example.chat.model.Role;
import com.example.chat.model.Status;
import com.example.chat.model.User;
import com.example.chat.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author dlyad
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    private final UserRepository userRepository;
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(()->
                new UsernameNotFoundException("user not found")
        );
        return SecurityUser.fromUser(user);
        
    }
    public void save(User user){
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        
    }
    public void edit(User user){
        User updatedUser = userRepository.findById(user.getId()).orElseThrow(()->
                new UsernameNotFoundException("user not found")
        );
        updatedUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setEmail(user.getEmail());
        userRepository.save(updatedUser);
    }
    public void delete(Long id){
        userRepository.deleteById(id);
    }
    public UserRepository getUserRepository() {
        return userRepository;
    }
    
    
}
