package com.abdullayevtural.silent_signals.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthApiIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("POST /api/auth/register yeni istifadəçi yaradır (201)")
	void registerReturnsCreated() throws Exception {
		String body = """
				{
				  "username": "integration_user_1",
				  "password": "Secret123!",
				  "email": "int1@example.com",
				  "phoneNumber": "+994501112233"
				}
				""";
		mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.message").exists());
	}

	@Test
	@DisplayName("POST /api/auth/register eyni username ilə 409 qaytarır")
	void registerDuplicateReturnsConflict() throws Exception {
		String body = """
				{
				  "username": "dup_user",
				  "password": "Secret123!",
				  "email": "dup@example.com",
				  "phoneNumber": "+994501112233"
				}
				""";
		mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isCreated());
		mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.message").exists());
	}

	@Test
	@DisplayName("POST /api/auth/login səhv şifrə ilə 401")
	void loginBadPasswordReturns401() throws Exception {
		String reg = """
				{
				  "username": "login_test_user",
				  "password": "CorrectPass1!",
				  "email": "login@example.com",
				  "phoneNumber": "+994501112233"
				}
				""";
		mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(reg))
				.andExpect(status().isCreated());

		String login = """
				{
				  "username": "login_test_user",
				  "password": "WrongPass!"
				}
				""";
		mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(login))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").exists());
	}
}
