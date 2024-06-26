/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.chat;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.MultipartConfigElement;
import java.util.Arrays;
import javax.sql.DataSource;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.unit.DataSize;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
@Configuration
public class WebConfig implements WebMvcConfigurer {
 @Override
 public void addViewControllers(ViewControllerRegistry registry) {
 registry.addRedirectViewController("/", "/");
 }
 @Bean
public FilterRegistrationBean hiddenHttpMethodFilter() {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new HiddenHttpMethodFilter());
    filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
    return filterRegistrationBean;
}
@Bean
public MultipartConfigElement multipartConfigElement() {
  MultipartConfigFactory factory = new MultipartConfigFactory();
  factory.setMaxFileSize(DataSize.ofBytes(Integer.MAX_VALUE));
  return factory.createMultipartConfig();
}
@Bean 
public RoomListener RoomListener(){
    return new RoomListener();
}
@Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/first_db");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        return dataSource;
    }
    @Bean
    public JdbcTemplate JdbcTemplate(){
        return  new JdbcTemplate(dataSource());
    }
    @Bean
    public ObjectMapper ObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        StreamReadConstraints streamReadConstraints = StreamReadConstraints
    .builder()
    .maxStringLength(Integer.MAX_VALUE)
    .build();
     mapper.getFactory().setStreamReadConstraints(streamReadConstraints);
     mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }
}
