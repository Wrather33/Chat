/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.chat.config;

import com.example.chat.model.Permission;
import com.example.chat.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 *
 * @author dlyad
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig{
    private final UserDetailsService UserDetailsService;
    @Autowired
    public SecurityConfig(UserDetailsService UserDetailsService) {
        this.UserDetailsService = UserDetailsService;
    }
    
  @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	 http.authorizeHttpRequests(auth-> auth.requestMatchers("/"
         ).
                 permitAll().
                 requestMatchers("/register").permitAll()/*.requestMatchers(HttpMethod.GET, "/api/**").
                 hasAuthority(Permission.DEVELOPERS_READ.getPermission()).
                 requestMatchers(HttpMethod.POST, "/api/**").hasAuthority(
                 Permission.DEVELOPERS_WRITE.getPermission()).
                 requestMatchers(HttpMethod.DELETE, "/api/**").
                 hasAuthority(
                 Permission.DEVELOPERS_WRITE.getPermission())*/
                 .anyRequest().authenticated()).formLogin(formLogin ->
    formLogin
        .loginPage("/login")
        .permitAll().defaultSuccessUrl("/success", true)
).logout(l->{
     l.invalidateHttpSession(true)
          .clearAuthentication(true)
          .logoutRequestMatcher(new AntPathRequestMatcher("/logout",
          "POST"))
          .logoutSuccessUrl("/login")
          .permitAll();})
                 ;
         
        return http.build();
}
/*@Bean
public UserDetailsService userDetailsService(){
    return new InMemoryUserDetailsManager(
    User.builder().username("admin").password(
    passwordEncoder().encode("admin"))
            .authorities(Role.ADMIN.getAuthoritys()).build(),
            User.builder().username("user").password(
            passwordEncoder().encode("user")).authorities(Role.USER.getAuthoritys()).build()
    );
}*/
public void configure(AuthenticationManagerBuilder authenticationManagerBuilder){
    authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
}
@Bean
public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
    
}

@Bean
public DaoAuthenticationProvider daoAuthenticationProvider(){
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    daoAuthenticationProvider.setUserDetailsService(UserDetailsService);
    return daoAuthenticationProvider;
}
}
