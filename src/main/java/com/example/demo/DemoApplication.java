package com.example.demo;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        return args -> {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);

            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin"));
            admin.setEmail("admin@example.com");
            admin.getRoles().add(adminRole);
            userRepository.save(admin);

            User user = new User();
            user.setUsername("user");
            user.setPassword(encoder.encode("user"));
            user.setEmail("user@example.com");
            user.getRoles().add(userRole);
            userRepository.save(user);
        };
    }
}
