package com.example.vietjapaneselearning.service.impl;

import com.example.vietjapaneselearning.dto.UserDTO;
import com.example.vietjapaneselearning.model.User;
import com.example.vietjapaneselearning.repository.UserRepository;
import com.example.vietjapaneselearning.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO getCurrentUser() {
        User user = currentUserService.getUserCurrent();
        return UserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .birthdate(String.valueOf(user.getBirthDay()))
                .avatar(user.getAvatar() != null ? user.getAvatar() : "Unknow")
                .build();
    }

    @Override
    public User editProfileUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByEmail(currentUserService.getUserCurrent().getEmail());
        if (existingUser.isEmpty()) {
            throw new EntityNotFoundException("Not found profile with id " + existingUser.get().getId());
        }
        if (!userDTO.getPhoneNumber().equals(existingUser.get().getPhoneNumber()) && userRepository.findByPhoneNumber(userDTO.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("Phone already exist in database!");

        }
        if (!userDTO.getEmail().equals(existingUser.get().getEmail()) && userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exist in database!");
        }
        if (userDTO.getPassword() != null &&  !bCryptPasswordEncoder.matches(userDTO.getPassword(), existingUser.get().getPassword())) {
            throw new IllegalArgumentException("Password does incorrect");
        }
        existingUser.get().setFullName(userDTO.getFullName());
        existingUser.get().setPhoneNumber(userDTO.getPhoneNumber());
        existingUser.get().setEmail(userDTO.getEmail());
        existingUser.get().setAvatar(userDTO.getAvatar());
        existingUser.get().setUpdatedAt(LocalDateTime.now());
        if (userDTO.getNewPassword() != null && !userDTO.getNewPassword().isBlank()) {
            existingUser.get().setPassword(bCryptPasswordEncoder.encode(userDTO.getNewPassword()));
        }
        return userRepository.save(existingUser.get());
    }
}
