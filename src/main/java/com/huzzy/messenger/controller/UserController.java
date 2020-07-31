package com.huzzy.messenger.controller;

import com.huzzy.messenger.dto.CreateUserRequestDto;
import com.huzzy.messenger.dto.CreateUserResponseDto;
import com.huzzy.messenger.service.AppUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("users")
@AllArgsConstructor
@Slf4j
public class UserController {

	private final AppUserService appUserService;

	@PostMapping
	public CreateUserResponseDto createUser(@RequestBody @Valid CreateUserRequestDto createUserRequestDto) {
		log.info("Create user {}", createUserRequestDto.getNickname());

		Long user = appUserService.createUser(createUserRequestDto);
		return CreateUserResponseDto.builder().id(user).build();
	}
}
