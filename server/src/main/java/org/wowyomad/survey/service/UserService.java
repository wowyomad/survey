package org.wowyomad.survey.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wowyomad.survey.dto.UserEditDto;
import org.wowyomad.survey.dto.UserLoginDto;
import org.wowyomad.survey.dto.UserRegistrationDto;
import org.wowyomad.survey.entity.ConfirmationType;
import org.wowyomad.survey.entity.User;
import org.wowyomad.survey.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ConfirmationCodeService confirmationCodeService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, ConfirmationCodeService confirmationCodeService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.confirmationCodeService = confirmationCodeService;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public User registerUser(UserRegistrationDto userRegistrationDto) {
        if (!userRegistrationDto.getPassword().equals(userRegistrationDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }


        User user = new User();
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setEmailConfirmed(false);
        user.setRequireEmailConfirmationOnLogin(true);
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setPhoneNumber(userRegistrationDto.getPhoneNumber());
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        try {
            confirmationCodeService.createConfirmationCode(user.getEmail(), ConfirmationType.ACCOUNT_CREATION);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Transactional
    public Optional<User> loginUser(UserLoginDto userLoginDTO) throws MessagingException {
        Optional<User> userOpt = userRepository.findByEmail(userLoginDTO.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
                if (user.isRequireEmailConfirmationOnLogin() && !user.isEmailConfirmed()) {
                    confirmationCodeService.createConfirmationCode(user.getEmail(), ConfirmationType.EMAIL_VERIFICATION);
                    return Optional.empty();
                } else {
                    return Optional.of(user);
                }
            }
        }
        return Optional.empty();
    }

    @Transactional
    public void confirmEmail(String email, String code, ConfirmationType type) {
        if (confirmationCodeService.confirmCode(email, code, type)) {
            Optional<User> userOpt = userRepository.findByEmail(email);
            userOpt.ifPresent(user -> {
                user.setEmailConfirmed(true);
                userRepository.save(user);
            });
        } else {
            throw new IllegalArgumentException("Invalid or expired confirmation code.");
        }
    }

    @Transactional
    public User editUser(User user, @Valid UserEditDto userEditDto) {
        user.setEmail(userEditDto.getEmail());
        user.setFirstName(userEditDto.getFirstName());
        user.setLastName(userEditDto.getLastName());
        user.setPhoneNumber(userEditDto.getPhoneNumber());
        return userRepository.save(user);
    }

    void removeExpiredUnconfirmedUsers() {
        LocalDateTime time = LocalDateTime.now().minusMinutes(1);
        List<User> unconfirmedUsers = userRepository.findByEmailConfirmedFalseAndCreatedAtBefore(time);
        userRepository.deleteAll(unconfirmedUsers);
    }

    public Optional<User> getById(Long userId) {
        return userRepository.findById(userId);
    }
}
