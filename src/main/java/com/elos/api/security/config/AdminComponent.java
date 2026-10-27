package com.elos.api.security.config;
import com.elos.api.dto.usuario.RegisterRequest;
import com.elos.api.entity.usuario.role.Role;
import com.elos.api.service.usuario.AuthenticationService;
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
