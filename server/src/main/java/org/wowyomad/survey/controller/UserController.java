package org.wowyomad.survey.controller;

import org.wowyomad.survey.dto.UserRegistrationDto;
import org.wowyomad.survey.dto.UserLoginDto;
import org.wowyomad.survey.dto.UserEditDto;
import org.wowyomad.survey.entity.ConfirmationType;
import org.wowyomad.survey.entity.User;
import org.wowyomad.survey.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto) throws MessagingException {
        userService.registerUser(userRegistrationDto);
        return ResponseEntity.ok("User registered successfully. Please check your email to confirm your account.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody UserLoginDto userLoginDto) throws MessagingException {
        Optional<User> userOpt = userService.loginUser(userLoginDto);

        if (userOpt.isPresent()) {
            return ResponseEntity.ok("Login successful.");
        } else {
            return ResponseEntity.status(401).body("Email confirmation required. Please check your email.");
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editUser(@RequestParam Long userId, @Valid @RequestBody UserEditDto userEditDto) {
        Optional<User> userOpt = userService.getById(userId);
        if(!userOpt.isPresent()) {
            return ResponseEntity.status(404).body("User not found.");
        }
        User user = userOpt.get();
        userService.editUser(user, userEditDto);
        return ResponseEntity.ok("User profile updated successfully.");
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmEmail(@RequestParam String email, @RequestParam String code, @RequestParam ConfirmationType type) {
        try {
            userService.confirmEmail(email, code, type);
            return ResponseEntity.ok("Email confirmed successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
