/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.chat.controllers;

import com.example.chat.model.ChatMessage;
import com.example.chat.model.Room;
import com.example.chat.model.User;
import com.example.chat.repository.CustomizedRoomRepositoryImpl;
import com.example.chat.repository.RoomRepository;
import com.example.chat.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author dlyad
 */
@Controller
@RequestMapping
public class AuthController {
     @Autowired
private SimpMessagingTemplate simpMessagingTemplate;
    private final RoomRepository roomRepository;
    private final UserDetailsServiceImpl UserDetailsServiceImpl;
    @Autowired
    public AuthController(UserDetailsServiceImpl UserDetailsServiceImpl
    , RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
        this.UserDetailsServiceImpl = UserDetailsServiceImpl;
    }
    @InitBinder
public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
}
    @GetMapping("/editRoom/{id}")
    public String getEditRoom(@PathVariable("id") Long id
    ,  Authentication authentication, Model model){
           UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = UserDetailsServiceImpl.getUserRepository().findByEmail(userDetails.getUsername());
        User result = user.get();
        if(id.equals(result.getId())){
            model.addAttribute("room", roomRepository.findByCreatorid(id).get());
            return "editRoom";
        }
        else{
            return String.format("redirect:/room/%d", id);
        }
}
    @DeleteMapping("/avatar/{id}")
    public String deleteAvatar(@PathVariable("id") Long id, Authentication authentication
    , Model model){
           UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = UserDetailsServiceImpl.getUserRepository().findByEmail(userDetails.getUsername());
        User result = user.get();
        if(id.equals(result.getId())){
        result.setPhoto(null);
        UserDetailsServiceImpl.getUserRepository().save(result);
        }
        return "redirect:/success";
    }
    @PostMapping("/avatar/{id}")
    public String setAvatar(@RequestParam(name = "photo", required = false) MultipartFile photo, @PathVariable("id") Long id,
     Authentication authentication, Model model) throws IOException{
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = UserDetailsServiceImpl.getUserRepository().findByEmail(userDetails.getUsername());
        User result = user.get();
        if(id.equals(result.getId())){
        result.setPhoto(photo.getBytes());
       UserDetailsServiceImpl.getUserRepository().save(result);
        }
       return "redirect:/success";
        
    }
    @PostMapping("/editImage/{id}")
    public String editImage(@RequestParam(name = "photo", required = false) MultipartFile photo, @PathVariable("id") Long id,
     Authentication authentication, Model model) throws IOException{
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User first = UserDetailsServiceImpl.getUserRepository().findByEmail(userDetails.getUsername()).get();
       User second = UserDetailsServiceImpl.getUserRepository().findById(id).get();
       if(first.getId().equals(second.getId())){
        Room roomUpdate = roomRepository.findByCreatorid(id).get();
        roomUpdate.setPhoto(photo.getBytes());
        roomRepository.save(roomUpdate);
        model.addAttribute("room", roomUpdate);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.PHOTO_ROOM);
        chatMessage.setContent(roomUpdate.getName());
        chatMessage.setPhoto(roomUpdate.generateImage());
        simpMessagingTemplate.convertAndSend(String.format("/room/%d", id)
                ,chatMessage);
       }
       return String.format("redirect:/editRoom/%d", id);
        
    }
    @DeleteMapping("/editImage/{id}")
    public String deleteImage(@PathVariable("id") Long id,
     Authentication authentication, Model model) throws IOException{
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User first = UserDetailsServiceImpl.getUserRepository().findByEmail(userDetails.getUsername()).get();
       User second = UserDetailsServiceImpl.getUserRepository().findById(id).get();
       if(first.getId().equals(second.getId())){
        Room roomUpdate = roomRepository.findByCreatorid(id).get();
        roomUpdate.setPhoto(null);
        roomRepository.save(roomUpdate);
        model.addAttribute("room", roomUpdate);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.PHOTO_ROOM);
        chatMessage.setContent(roomUpdate.getName());
        chatMessage.setPhoto(roomUpdate.generateImage());
        simpMessagingTemplate.convertAndSend(String.format("/room/%d", id)
                ,chatMessage);
       }
      return String.format("redirect:/editRoom/%d", id);
            
    }
    @PatchMapping("/editRoom/{id}")
    public String editRoom(@ModelAttribute("room") Room room
    , @PathVariable("id") Long id){
        try{
        Room roomUpdate = roomRepository.findByCreatorid(id).get();
        roomUpdate.setName(room.getName());
        roomRepository.save(roomUpdate);
        }
        catch(Exception e){
            return String.format("redirect:/editRoom/%d", id);
        }
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.EDIT);
        chatMessage.setContent(room.getName());
        simpMessagingTemplate.convertAndSend(String.format("/room/%d", id)
                ,chatMessage);
        return String.format("redirect:/room/%d", id);
}
    @GetMapping("/room/{id}")
    public String getRoom(Model model, @PathVariable("id") Long id
    , Authentication authentication){
        if(roomRepository.findByCreatorid(id).isEmpty()){
            return String.format("redirect:/success/%d", id);
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = UserDetailsServiceImpl.getUserRepository().findByEmail(userDetails.getUsername());
        User result = user.get();
        //roomRepository.addToUsers(result, id);
        model.addAttribute("room", 
                roomRepository.findByCreatorid(id).get());
        Collection<User> users = roomRepository.findByCreatorid(id).get().getUsers();
        model.addAttribute("users", users);
        return "room";
}
    @GetMapping("/room/{id}/leave")
    public String leaveRoom(@PathVariable("id") Long id, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = UserDetailsServiceImpl.getUserRepository().findByEmail(userDetails.getUsername());
        User result = user.get();
       roomRepository.removeFromUsers(result, id);
       return "redirect:/success";
    }
    @DeleteMapping("/room/{id}")
    public String deleteRoom(@PathVariable("id") Long id, Authentication authentication
    , Model model){
           UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = UserDetailsServiceImpl.getUserRepository().findByEmail(userDetails.getUsername());
        User result = user.get();
        if(id.equals(result.getId())){
            roomRepository.deleteByCreatorid(id);
            return String.format("redirect:/success/%d", id);
        }
        else{
            model.addAttribute("users", roomRepository.findByCreatorid(id).get()
            .getUsers());
            model.addAttribute("room", roomRepository.findByCreatorid(id).get());
            return "room";
        }
    }
    @GetMapping("/")
    public String getIndexPage(){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof AnonymousAuthenticationToken) return "index";
    return "redirect:/success";
    }
    @GetMapping("/createRoom/{id}")
    public String getRoomForm(@ModelAttribute("room") Room room
    , @PathVariable("id") Long id){
        room.setCreatorid(id);
        return "createRoom";
    }
     @PostMapping("/createRoom/{id}")
    public String createRoom(@ModelAttribute("room") Room room
    , @PathVariable("id") Long id, @RequestParam(name = "file", required = false) MultipartFile file){
        try{
            room.setPhoto(file.getBytes());
            room.setCreatorid(id);
            roomRepository.save(room);
        }
        catch(Exception e){
            return "createRoom";
        }
            return String.format("redirect:/room/%d", id);
    }
    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }
    @GetMapping("/success")
    public String getSuccessPage(Model model, 
            Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = UserDetailsServiceImpl.getUserRepository().findByEmail(userDetails.getUsername());
        User result = user.get();
        model.addAttribute("user", result);
        return String.format("redirect:/success/%d", result.getId());
    }
    @GetMapping("/success/{id}")
    public String getPage(@PathVariable("id") Long id,
        Authentication authentication, Model model){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User first = UserDetailsServiceImpl.getUserRepository().findByEmail(userDetails.getUsername()).get();
        try{
            User second = UserDetailsServiceImpl.getUserRepository().findById(id).get();
            if(first.getId().equals(second.getId())){
            model.addAttribute("user", first);
            List<Room> rooms = roomRepository.findAll();
            if(!rooms.isEmpty()){
            model.addAttribute("rooms", rooms);
            }
            return "success";
        }
        else{
                throw new Exception();
        }
        }
        catch(Exception e){
            return String.format("redirect:/success/%d", first.getId());
        }
               
    }
        
    @GetMapping("/register")
    public String getRegisterPage(@ModelAttribute("user") User user){
        return "register";
    }
    @GetMapping("/edit/{id}")
    public String getEditpage(@PathVariable("id") Long id,
        Authentication authentication, Model model){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User first = UserDetailsServiceImpl.getUserRepository().findByEmail(userDetails.getUsername()).get();
        try{
            User second = UserDetailsServiceImpl.getUserRepository().findById(id).get();
            if(first.getId().equals(second.getId())){
            model.addAttribute("user", first);
            return "edit";
        }
        else{
                throw new Exception();
        }
        }
        catch(Exception e){
            return String.format("redirect:/edit/%d", first.getId());
        }
    }
    @PatchMapping("/edit/{id}")
    public String editUser(@ModelAttribute("user") User user,
             @PathVariable("id") Long id){
        try{
            user.setId(id);
            UserDetailsServiceImpl.edit(user);
        }
        catch(Exception e){
            return "edit";
        }
         SecurityContext context = SecurityContextHolder.getContext();
         SecurityContextHolder.clearContext();
         context.setAuthentication(null);
        return "redirect:/login";
    }
     @PostMapping("/register")
    public String addUser(@ModelAttribute("user") User user,
            @RequestParam(name = "file", required = false) MultipartFile file) throws IOException{
        try{
            user.setPhoto(file.getBytes());
            UserDetailsServiceImpl.save(user);
        }
        catch(Exception e){
            return "register";
        }
        return "redirect:/login";
        
    }
    @DeleteMapping("/delete/{id}")
    public String deleteUser(
    @PathVariable("id") Long id,
        Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User first = UserDetailsServiceImpl.getUserRepository().findByEmail(userDetails.getUsername()).get();
        try{
            User second = UserDetailsServiceImpl.getUserRepository().findById(id).get();
            if(first.getId().equals(second.getId())){
            UserDetailsServiceImpl.delete(id);
            SecurityContext context = SecurityContextHolder.getContext();
            SecurityContextHolder.clearContext();
            context.setAuthentication(null);
            return "redirect:/login";
        }
        else{
                throw new Exception();
        }
        }
        catch(Exception e){
            return String.format("redirect:/success/%d", first.getId());
        }
    }
    
}
