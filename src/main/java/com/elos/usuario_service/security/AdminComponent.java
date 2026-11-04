package com.elos.usuario_service.security;

import com.elos.usuario_service.AuthenticationService;
import com.elos.usuario_service.dto.RegisterRequest;
import com.elos.usuario_service.usuario.role.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminComponent {
    @Value("${spring.security.user.name}")
    private String adminEmail;
    @Value("${spring.security.user.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner initAdmin(AuthenticationService authentication) {
       return args -> {
           try {
               authentication.register(
                       RegisterRequest.noRole("admin", adminEmail, adminPassword, "", "", ""),
                       Role.ADMIN
               );
               System.out.println("Admin user initialized successfully.");
           } catch (Exception e) {
               System.out.println("Admin initialization skipped or failed: " + e.getMessage());
           }
       };

    }
}
