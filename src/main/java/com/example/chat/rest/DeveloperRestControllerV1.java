/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.chat.rest;

import com.example.chat.model.Developer;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dlyad
 */
@RestController
@RequestMapping("/api/v1/developers")
public class DeveloperRestControllerV1 {
    private List<Developer> developers = Stream.of(
    new Developer(1L, "Ivan", "Ivanov"),
            new Developer(2L, "Sergey", "Sergeev"),
            new Developer(3L, "Petr", "Petrov")
).collect(Collectors.toList());
    
    @GetMapping
    @PreAuthorize("hasAuthority('developers:read')")
    public List<Developer> getAll(){
        return developers;
                }
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('developers:read')")
    public Developer getById(@PathVariable Long id)
    {
   return developers.stream().filter(developer->developer.getId().equals(id)).findFirst().orElse(null);
}
    @PostMapping
    @PreAuthorize("hasAuthority('developers:write')")
    public Developer create(@RequestBody Developer developer){
        developers.add(developer);
        return developer;
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('developers:write')")
    public void delete(@PathVariable Long id){
        developers.removeIf(developer-> developer.getId().equals(id));
    }
        
}
