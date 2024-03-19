/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.chat.model;

import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *
 * @author dlyad
 */
public enum Role {
    USER(Set.of(Permission.DEVELOPERS_READ)),
    ADMIN(Set.of(Permission.DEVELOPERS_READ, Permission.DEVELOPERS_WRITE));
    
    private Set<Permission> permissions;
    
    private Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }
    public Set<SimpleGrantedAuthority> getAuthoritys(){
        return permissions.stream().map(permission ->
        new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toSet());
    }
    
}
