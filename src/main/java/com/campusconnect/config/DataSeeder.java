
package com.campusconnect.config;

import com.campusconnect.model.Role;
import com.campusconnect.model.User;
import com.campusconnect.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createUserIfMissing(
                "Demo Student",
                "student@campusconnect.com",
                "student123",
                Role.STUDENT);

        createUserIfMissing(
                "Demo Faculty",
                "faculty@campusconnect.com",
                "faculty123",
                Role.FACULTY);

        createUserIfMissing(
                "Demo Admin",
                "admin@campusconnect.com",
                "admin123",
                Role.ADMIN);
    }

    private void createUserIfMissing(
            String name,
            String email,
            String password,
            Role role) {

        if (userRepository.existsByEmail(email)) {
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        userRepository.save(user);
    }
}
