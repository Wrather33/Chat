/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.chat.model;

/**
 *
 * @author dlyad
 */
public enum Permission {
    DEVELOPERS_READ("developers:read"),
    DEVELOPERS_WRITE("developers:write");
    
    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
    
    
}
