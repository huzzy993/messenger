package com.huzzy.messenger.controller;

import com.huzzy.messenger.IntegrationTest;
import com.huzzy.messenger.dto.CreateUserRequestDto;
import com.huzzy.messenger.dto.CreateUserResponseDto;
import com.huzzy.messenger.repository.AppUserRepository;
import com.huzzy.messenger.service.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends IntegrationTest {

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private AppUserService appUserService;

	@BeforeEach
	void cleanup() {
		appUserRepository.deleteAll();
	}

	@Test
	void shouldCreateUser() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/users")
						.contentType(APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(CreateUserRequestDto.builder().nickname("nick-1").build())))
						.andReturn();

		CreateUserResponseDto createUserResponseDto =
						objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreateUserResponseDto.class);

		assertThat(appUserRepository.findById(createUserResponseDto.getId())).hasValueSatisfying(appUser -> {
			assertThat(appUser.getNickname()).isEqualTo("nick-1");
		});
	}

	@Test
	void shouldFailToCreateUserWithDuplicateNickname() throws Exception {
		appUserService.createUser(CreateUserRequestDto.builder().nickname("nick-1").build());

		mockMvc.perform(post("/users")
						.contentType(APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(CreateUserRequestDto.builder().nickname("nick-1").build())))
						.andExpect(status().isConflict());
	}
}
