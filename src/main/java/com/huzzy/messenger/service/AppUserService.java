package com.huzzy.messenger.service;

import com.huzzy.messenger.dto.CreateUserRequestDto;
import com.huzzy.messenger.entity.AppUser;
import com.huzzy.messenger.exception.DuplicateNicknameException;
import com.huzzy.messenger.repository.AppUserRepository;
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
			throw new DuplicateNicknameException("User with nickname " + nickname + " already exists");
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
