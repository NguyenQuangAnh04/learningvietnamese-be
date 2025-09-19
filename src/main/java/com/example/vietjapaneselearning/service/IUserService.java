package com.example.vietjapaneselearning.service;

import com.example.vietjapaneselearning.dto.UserDTO;
import com.example.vietjapaneselearning.model.User;

public interface IUserService {
    UserDTO getCurrentUser();
    User editProfileUser(UserDTO userDTO);
}
