/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.chat.model;

import com.example.chat.RoomListener;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLConnection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 *
 * @author dlyad
 */
@Data
@EntityListeners(RoomListener.class)
@Entity
@Table(name ="rooms")
public class Room{
    
    @Id
    private Long creatorid;
    @Column(name = "name")
    private String name;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "users")
    private Collection<User> users = new ArrayList<User>();
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "messages")
    private Collection<ChatMessage> messages = new ArrayList<>();
    @Column(name = "photo")
    private byte[] photo;
    public String generateImage() throws IOException {
        if(getPhoto() != null){
        InputStream is = new ByteArrayInputStream(photo);
        String mimeType = URLConnection.guessContentTypeFromStream(is);
        is.close();
        return String.format("data:%s;base64,%s", mimeType, Base64.getEncoder().encodeToString(photo));
        }
        else {
            return null;
        }
}
    
}
