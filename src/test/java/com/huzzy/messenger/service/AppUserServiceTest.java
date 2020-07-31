package com.huzzy.messenger.service;

import com.huzzy.messenger.dto.CreateUserRequestDto;
import com.huzzy.messenger.entity.AppUser;
import com.huzzy.messenger.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest {

	@InjectMocks
	AppUserService underTest;

	@Mock
	private AppUserRepository appUserRepository;

	@Test
	public void createUser() {
		underTest.createUser(CreateUserRequestDto.builder().nickname("nick-1").build());

		verify(appUserRepository).save(argThat(argument -> argument.getNickname().equals("nick-1")));
	}

	@Test
	public void findUser() {
		Optional<AppUser> mock = Optional.of(mock(AppUser.class));
		when(appUserRepository.findById(1L)).thenReturn(mock);

		Optional<AppUser> actual = underTest.findUser(1L);

		assertThat(actual).hasValue(mock.get());
	}
}
