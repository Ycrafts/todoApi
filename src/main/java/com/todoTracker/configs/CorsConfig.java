// package com.todoTracker.configs;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class CorsConfig implements WebMvcConfigurer {
//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/api/**") 
//                 .allowedOrigins(
                        
//                         "http://192.168.1.97:8080" 
//                 )
//                 .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                 .allowedHeaders("Content-Type", "Authorization")
//                 .allowCredentials(true)
//                 .maxAge(3600);
//     }
// }