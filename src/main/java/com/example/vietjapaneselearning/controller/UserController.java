package com.example.vietjapaneselearning.controller;

import com.example.vietjapaneselearning.dto.UserDTO;
import com.example.vietjapaneselearning.model.User;
import com.example.vietjapaneselearning.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PutMapping("/edit-profile")
    public ResponseEntity<User> editProfile(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.editProfileUser(userDTO));
    }
}
