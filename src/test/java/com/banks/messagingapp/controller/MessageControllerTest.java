package com.banks.messagingapp.controller;

import com.banks.messagingapp.IntegrationTest;
import com.banks.messagingapp.dto.CreateUserRequestDto;
import com.banks.messagingapp.dto.InboxDto;
import com.banks.messagingapp.dto.OutboxDto;
import com.banks.messagingapp.dto.SendMessageRequestDto;
import com.banks.messagingapp.dto.SendMessageResponseDto;
import com.banks.messagingapp.repository.AppUserRepository;
import com.banks.messagingapp.repository.MessageRepository;
import com.banks.messagingapp.service.AppUserService;
import com.banks.messagingapp.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static com.banks.messagingapp.util.Constants.USER_ID_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MessageControllerTest extends IntegrationTest {

	@Autowired
	private AppUserService appUserService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private MessageRepository messageRepository;

	@BeforeEach
	public void setup() {
		messageRepository.deleteAll();
		appUserRepository.deleteAll();
	}

	@Test
	public void shouldSendMessageWithCorrectUser() throws Exception {
		Long userId1 = appUserService.createUser(CreateUserRequestDto.builder().nickname("nick-1").build());
		Long userId2 = appUserService.createUser(CreateUserRequestDto.builder().nickname("nick-2").build());

		MvcResult mvcResult = mockMvc.perform(post("/messages")
						.contentType(APPLICATION_JSON)
						.header(USER_ID_HEADER, userId1)
						.content(objectMapper.writeValueAsString(SendMessageRequestDto.builder().message("test").recipientId(userId2).build())))
						.andReturn();

		SendMessageResponseDto sendMessageRequestDto =
						objectMapper.readValue(mvcResult.getResponse().getContentAsString(), SendMessageResponseDto.class);

		assertThat(messageRepository.findById(sendMessageRequestDto.getMessageId())).hasValueSatisfying(message -> {
			assertThat(message.getText()).isEqualTo("test");
			assertThat(message.getSender().getId()).isEqualTo(userId1);
			assertThat(message.getRecipient().getId()).isEqualTo(userId2);
		});
	}

	@Test
	public void shouldFailToSendWithUserThatDoesNotExist() throws Exception {
		Long userId1 = appUserService.createUser(CreateUserRequestDto.builder().nickname("nick-2").build());
		Long userId2 = userId1 + 1;

		ResultActions mvcResult = mockMvc.perform(post("/messages")
						.contentType(APPLICATION_JSON)
						.header(USER_ID_HEADER, userId2)
						.content(objectMapper.writeValueAsString(SendMessageRequestDto.builder().message("test").recipientId(userId1).build())));

		mvcResult.andExpect(status().isNotFound());
	}

	@Test
	public void shouldFailToSendMessageToSelf() throws Exception {
		Long userId2 = appUserService.createUser(CreateUserRequestDto.builder().nickname("nick-2").build());

		MvcResult mvcResult = mockMvc.perform(post("/messages")
						.contentType(APPLICATION_JSON)
						.header(USER_ID_HEADER, userId2)
						.content(objectMapper.writeValueAsString(SendMessageRequestDto.builder().message("test").recipientId(userId2).build())))
						.andReturn();

		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
	}

	@Test
	public void shouldFilterCurrentUserInbox() throws Exception {
		Long userId1 = appUserService.createUser(CreateUserRequestDto.builder().nickname("nick-1").build());
		Long userId2 = appUserService.createUser(CreateUserRequestDto.builder().nickname("nick-2").build());
		Long userId3 = appUserService.createUser(CreateUserRequestDto.builder().nickname("nick-3").build());

		messageService.sendMessage(appUserRepository.getOne(userId1), SendMessageRequestDto.builder().message("1 to " +
						"2").recipientId(userId2).build());
		messageService.sendMessage(appUserRepository.getOne(userId3), SendMessageRequestDto.builder().message("3 to " +
						"2").recipientId(userId2).build());

		MvcResult mvcResult = mockMvc.perform(get("/messages/inbox").header(USER_ID_HEADER, userId2)).andReturn();

		InboxDto[] list = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), InboxDto[].class);
		assertThat(list).extracting(InboxDto::getFrom, InboxDto::getMessage).containsExactlyInAnyOrder(
						tuple("nick-1", "1 to 2"),
						tuple("nick-3", "3 to 2"));
	}

	@Test
	void shouldFilterCurrentUserInboxBySender() throws Exception {
		Long userId1 = appUserService.createUser(CreateUserRequestDto.builder().nickname("nick-1").build());
		Long userId2 = appUserService.createUser(CreateUserRequestDto.builder().nickname("nick-2").build());
		Long userId3 = appUserService.createUser(CreateUserRequestDto.builder().nickname("nick-3").build());
		Long userId4 = appUserService.createUser(CreateUserRequestDto.builder().nickname("nick-4").build());

		messageService.sendMessage(appUserRepository.getOne(userId2),
						SendMessageRequestDto.builder().message("2 to 1").recipientId(userId1).build());
		messageService.sendMessage(appUserRepository.getOne(userId3),
						SendMessageRequestDto.builder().message("3 to 1").recipientId(userId1).build());
		messageService.sendMessage(appUserRepository.getOne(userId4),
						SendMessageRequestDto.builder().message("4 to 1").recipientId(userId1).build());
		messageService.sendMessage(appUserRepository.getOne(userId4),
						SendMessageRequestDto.builder().message("4 to 1 again").recipientId(userId1).build());

		MvcResult mvcResult = mockMvc.perform(get("/messages/inbox/filter")
						.param("senderId", String.valueOf(userId4))
						.header(USER_ID_HEADER, userId1)).andReturn();

		InboxDto[] list = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), InboxDto[].class);
		assertThat(list).extracting(InboxDto::getFrom, InboxDto::getMessage).containsExactlyInAnyOrder(
						tuple("nick-4", "4 to 1"),
						tuple("nick-4", "4 to 1 again"));
	}

	@Test
	void shouldGetOutbox() throws Exception {
		Long userId1 = appUserService.createUser(CreateUserRequestDto.builder().nickname("nick-1").build());
		Long userId2 = appUserService.createUser(CreateUserRequestDto.builder().nickname("nick-2").build());
		Long userId3 = appUserService.createUser(CreateUserRequestDto.builder().nickname("nick-3").build());
		Long userId4 = appUserService.createUser(CreateUserRequestDto.builder().nickname("nick-4").build());

		messageService.sendMessage(appUserRepository.getOne(userId3),
						SendMessageRequestDto.builder().message("3 to 1").recipientId(userId1).build());
		messageService.sendMessage(appUserRepository.getOne(userId4),
						SendMessageRequestDto.builder().message("4 to 1").recipientId(userId1).build());
		messageService.sendMessage(appUserRepository.getOne(userId4),
						SendMessageRequestDto.builder().message("4 to 1 again").recipientId(userId1).build());
		messageService.sendMessage(appUserRepository.getOne(userId4),
						SendMessageRequestDto.builder().message("4 to 2").recipientId(userId2).build());

		MvcResult mvcResult = mockMvc.perform(get("/messages/outbox")
						.header(USER_ID_HEADER, userId4)).andReturn();

		OutboxDto[] list = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OutboxDto[].class);
		assertThat(list).extracting(OutboxDto::getTo, OutboxDto::getMessage).containsExactlyInAnyOrder(
						tuple("nick-1", "4 to 1"),
						tuple("nick-1", "4 to 1 again"),
						tuple("nick-2", "4 to 2"));
	}

}
