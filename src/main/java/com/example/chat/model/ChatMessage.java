/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.chat.model;

import java.io.File;
import java.nio.ByteBuffer;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import lombok.Data;
import org.hibernate.type.descriptor.java.BlobJavaType;
@Data
public class ChatMessage {
    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        EDIT,
        FILE
    }

    private MessageType type;
    private String content;
    private String sender;
    private String date;
    private String file;

}