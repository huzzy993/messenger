package com.banks.messagingapp.service;

import com.banks.messagingapp.dto.CreateUserRequestDto;
import com.banks.messagingapp.entity.AppUser;
import com.banks.messagingapp.exception.AppException;
import com.banks.messagingapp.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AppUserService {

	private final AppUserRepository appUserRepository;

	public Long createUser(CreateUserRequestDto createUserRequestDto) {
		String nickname = createUserRequestDto.getNickname();
		if (appUserRepository.findByNickname(nickname).isPresent()) {
			throw new AppException.DuplicateNicknameException("User with nickname " + nickname + " already exists");
		}

		AppUser appUser = new AppUser();
		appUser.setNickname(nickname);
		appUserRepository.save(appUser);
		return appUser.getId();
	}

	public Optional<AppUser> findUser(Long userId) {
		return appUserRepository.findById(userId);
	}
}
