package in.hotel.config;

import in.hotel.entity.User;
import in.hotel.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            // check if admin already exists
            if (userRepository.existsByEmail("admin@hotel.com")) {
                return;
            }

            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@hotel.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            admin.setEnabled(true);

            userRepository.save(admin);

            System.out.println("✅ ADMIN user created: admin@hotel.com / admin123");
        };
    }
}
