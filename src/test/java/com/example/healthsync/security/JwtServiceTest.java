package com.example.healthsync.security;

import com.example.healthsync.entity.Role;
import com.example.healthsync.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(
                jwtService,
                "secretKey",
                "test-secret-key-that-is-long-enough-for-hmac-signing"
        );

        ReflectionTestUtils.setField(
                jwtService,
                "jwtExpiration",
                86400000L
        );
    }

    @Test
    void generateTokenCreatesTokenWithUserEmail() throws Exception {
        User user = new User(
                1L,
                "firstName",
                "lastName",
                "email@email.com",
                "password",
                Role.USER
        );

        String token = jwtService.generateToken(user);

        assertThat(jwtService.extractUsername(token)).isEqualTo(user.getEmail());
    }

    @Test
    void isTokenValidReturnsTrueForValidToken() throws Exception {
        User user = new User(
                1L,
                "firstName",
                "lastName",
                "email@email.com",
                "password",
                Role.USER
        );

        String token = jwtService.generateToken(user);

        assertThat(jwtService.isTokenValid(token, user)).isTrue();
    }

    @Test
    void isTokenValidReturnsFalseWhenUserEmailDoesNotMatch() throws Exception {
        User userOne = new User(
                1L,
                "firstName",
                "lastName",
                "emailOne@email.com",
                "password",
                Role.USER
        );

        String token = jwtService.generateToken(userOne);

        User userTwo = new User(
                1L,
                "firstName",
                "lastName",
                "emailTwo@email.com",
                "password",
                Role.USER
        );

        assertThat(jwtService.isTokenValid(token, userTwo)).isFalse();
    }
}
