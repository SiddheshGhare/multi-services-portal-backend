package com.msp.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msp.dto.AuthResponse;
import com.msp.dto.AuthUserSummaryResponse;
import com.msp.dto.LoginRequest;
import com.msp.dto.LoginResult;
import com.msp.dto.RegisterRequest;
import com.msp.entity.RefreshToken;
import com.msp.entity.UserEntity;
import com.msp.exception.InvalidCredentialsException;
import com.msp.exception.UserAlreadyExistsException;
import com.msp.exception.UserNotFoundException;
import com.msp.repository.RefreshTokenRepository;
import com.msp.repository.UserRepository;
import com.msp.security.JwtUtil;

@Service
@Transactional
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // ================= REGISTER =================

    public String register(RegisterRequest request) {

        if (request == null) {
            throw new InvalidCredentialsException("Invalid Request");
        }

        if (request.getEmail() == null ||
                request.getEmail().isBlank()) {

            throw new InvalidCredentialsException(
                    "Email is required");
        }

        if (request.getPassword() == null ||
                request.getPassword().isBlank()) {

            throw new InvalidCredentialsException(
                    "Password is required");
        }

        if (request.getRole() == null) {

            throw new InvalidCredentialsException(
                    "Role is required");
        }

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new UserAlreadyExistsException(
                    "Email already registered");
        }

        UserEntity user = new UserEntity();

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()));

        user.setRole(request.getRole());

        userRepository.save(user);

        return "User Registered Successfully";
    }



    // ================= LOGIN =================

    public LoginResult login(LoginRequest request) {

        if (request == null) {

            throw new InvalidCredentialsException(
                    "Invalid Request");
        }

        if (request.getEmail() == null ||
                request.getEmail().isBlank()) {

            throw new InvalidCredentialsException(
                    "Email is required");
        }

        if (request.getPassword() == null ||
                request.getPassword().isBlank()) {

            throw new InvalidCredentialsException(
                    "Password is required");
        }


        UserEntity user =

                userRepository

                        .findByEmail(request.getEmail())

                        .orElseThrow(() ->

                                new InvalidCredentialsException(

                                        "Invalid email or password"));



        if (!passwordEncoder.matches(

                request.getPassword(),

                user.getPassword())) {


            throw new InvalidCredentialsException(

                    "Invalid email or password");
        }



        // Delete old refresh token

        refreshTokenRepository.deleteByUser(user);



        // Generate access token

        String accessToken =

                jwtUtil.generateToken(

                        user.getId(),

                        user.getEmail(),

                        user.getRole().name());



        // Generate refresh token

        String refreshTokenValue =

                jwtUtil.generateRefreshToken();



        RefreshToken refreshToken =

                new RefreshToken();


        refreshToken.setToken(refreshTokenValue);

        refreshToken.setUser(user);

        refreshToken.setExpiryDate(

                LocalDateTime.now().plusDays(7));

        System.out.println("Refresh Token : " + refreshTokenValue);

        refreshTokenRepository.save(refreshToken);
        System.out.println("Saving refresh token...");



        AuthResponse authResponse =

                new AuthResponse(

                        user.getId(),

                        user.getRole(),

                        accessToken);



        return new LoginResult(

                authResponse,

                refreshTokenValue);
    }



    // ================= REFRESH TOKEN =================

    public String refreshToken(String token) {


        if (token == null ||

                token.isBlank()) {

            throw new InvalidCredentialsException(

                    "Refresh token missing");
        }



        RefreshToken refreshToken =

                refreshTokenRepository

                        .findByToken(token)

                        .orElseThrow(() ->

                                new InvalidCredentialsException(

                                        "Invalid refresh token"));




        if (refreshToken.getExpiryDate()

                .isBefore(LocalDateTime.now())) {


            refreshTokenRepository.delete(refreshToken);


            throw new InvalidCredentialsException(

                    "Refresh token expired");
        }



        UserEntity user =

                refreshToken.getUser();



        return jwtUtil.generateToken(

                user.getId(),

                user.getEmail(),

                user.getRole().name());
    }



    // ================= LOGOUT =================

    public void logout(String refreshToken) {


        if (refreshToken == null ||

                refreshToken.isBlank()) {


            throw new InvalidCredentialsException(

                    "Refresh token missing");
        }



        RefreshToken token =

                refreshTokenRepository

                        .findByToken(refreshToken)

                        .orElseThrow(() ->

                                new InvalidCredentialsException(

                                        "Invalid refresh token"));



        refreshTokenRepository.delete(token);

    }

    public AuthUserSummaryResponse getUserSummaryById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return new AuthUserSummaryResponse(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }

}
