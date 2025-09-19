package com.example.vietjapaneselearning.service;

import com.example.vietjapaneselearning.model.User;
import com.example.vietjapaneselearning.dto.request.AuthRequest;
import com.example.vietjapaneselearning.dto.request.RegisterRequest;
import com.example.vietjapaneselearning.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthService {
    AuthResponse login(AuthRequest request, HttpServletResponse response);
    User register(RegisterRequest request);

}
