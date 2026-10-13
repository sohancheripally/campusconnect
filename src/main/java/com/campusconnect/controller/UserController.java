
package com.campusconnect.controller;

import com.campusconnect.model.Role;
import com.campusconnect.model.User;
import com.campusconnect.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/students")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public List<User> getStudents() {
        return userRepository.findByRole(Role.STUDENT)
                .stream()
                .map(user -> {
                    User safeUser = new User();
                    safeUser.setId(user.getId());
                    safeUser.setName(user.getName());
                    safeUser.setEmail(user.getEmail());
                    safeUser.setRole(user.getRole());
                    safeUser.setDepartment(user.getDepartment());
                    safeUser.setRollNumber(user.getRollNumber());
                    return safeUser;
                })
                .toList();
    }
    @GetMapping
@PreAuthorize("hasRole('ADMIN')")
public List<User> getAllUsers() {
    return userRepository.findAll().stream()
        .map(user -> {
            User safeUser = new User();
            safeUser.setId(user.getId());
            safeUser.setName(user.getName());
            safeUser.setEmail(user.getEmail());
            safeUser.setRole(user.getRole());
            safeUser.setDepartment(user.getDepartment());
            safeUser.setRollNumber(user.getRollNumber());
            return safeUser;
        })
        .toList();
}
}
