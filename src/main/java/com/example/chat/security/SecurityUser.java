/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.chat.security;

import com.example.chat.model.Status;
import com.example.chat.model.User;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author dlyad
 */
@Data
public class SecurityUser implements UserDetails{
    private final String username;
    private final String password;
    private final List<SimpleGrantedAuthority> authoritys;
    private final boolean isActive;

    public SecurityUser(String username, String password, List<SimpleGrantedAuthority> authoritys, boolean isActive) {
        this.username = username;
        this.password = password;
        this.authoritys = authoritys;
        this.isActive = isActive;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authoritys;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
     return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
     return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
    public static UserDetails fromUser(User user){
        return new org.springframework.security.core.userdetails.User(user.getEmail(), 
                user.getPassword(), 
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getRole().getAuthoritys()
                
                
        );
    }     
    
}
