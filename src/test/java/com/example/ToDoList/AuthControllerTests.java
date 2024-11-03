package com.example.ToDoList;


import com.fasterxml.jackson.databind.ObjectMapper;
import controller.AuthReuest;
import model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.security.config.annotation.web.AuthorizeRequestsDsl;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WebMvcTest(AuthReuest.class)
@SpringJUnitConfig
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterUser() throws Exception {
        // Create a new user for registration
        User user = new User();
        user.setEmail("testuser@example.com");
        user.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    public void testLoginUser_Success() throws Exception {
        // Create a request for login with valid credentials
        AuthReuest authRequest = new AuthReuest();
        authRequest.setEmail("testuser@example.com");
        authRequest.setPassword("right");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(".")))  // JWT typically contains periods in its structure
                .andExpect(jsonPath("$").isString());
    }

    @Test
    public void testLoginUser_InvalidCredentials() throws Exception {
        // Create a request for login with invalid credentials
        AuthReuest authRequest = new AuthReuest();
        authRequest.setEmail("wronguser@example.com");
        authRequest.setPassword("wrong");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }
}
