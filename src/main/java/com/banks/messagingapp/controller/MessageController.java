package com.banks.messagingapp.controller;

import com.banks.messagingapp.dto.InboxDto;
import com.banks.messagingapp.dto.OutboxDto;
import com.banks.messagingapp.dto.SendMessageRequestDto;
import com.banks.messagingapp.dto.SendMessageResponseDto;
import com.banks.messagingapp.model.CurrentUserDetails;
import com.banks.messagingapp.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiImplicitParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.banks.messagingapp.controller.MessageController.MESSAGES_BASE_PATH;
import static com.banks.messagingapp.util.Constants.USER_ID_HEADER;

@RestController
@RequestMapping(MESSAGES_BASE_PATH)
@AllArgsConstructor
@Slf4j
public class MessageController {

	public static final String MESSAGES_BASE_PATH = "/messages";

	private final MessageService messageService;

	private final CurrentUserDetails currentUserDetails;

	@PostMapping
	@ApiImplicitParam(name = USER_ID_HEADER, value = "Current User Id", required = true, paramType = "header",
					dataTypeClass = Long.class, example = "1234")
	public SendMessageResponseDto sendMessage(@RequestBody SendMessageRequestDto messageRequestDto) throws JsonProcessingException {
		Long messageId = messageService.sendMessage(currentUserDetails.getAppUser(), messageRequestDto);
		return SendMessageResponseDto.builder().messageId(messageId).build();
	}

	@GetMapping("inbox")
	@ApiImplicitParam(name = USER_ID_HEADER, value = "Current User Id", required = true, paramType = "header",
					dataTypeClass = Long.class, example = "1234")
	public List<InboxDto> loadInbox() {
		log.info("Load inbox for {}", currentUserDetails.getAppUser().getId());

		return messageService.getInbox(currentUserDetails.getAppUser());
	}

	@GetMapping("inbox/filter")
	@ApiImplicitParam(name = USER_ID_HEADER, value = "Current User Id", required = true, paramType = "header",
					dataTypeClass = Long.class, example = "1234")
	public List<InboxDto> filterInbox(@RequestParam("senderId") Long senderId) {
		log.info("Filter inbox by sender {} for {}", senderId, currentUserDetails.getAppUser().getId());

		return messageService.filterInboxBySender(currentUserDetails.getAppUser(), senderId);
	}

	@GetMapping("outbox")
	@ApiImplicitParam(name = USER_ID_HEADER, value = "Current User Id", required = true, paramType = "header",
					dataTypeClass = Long.class, example = "1234")
	public List<OutboxDto> loadOutbox() {
		log.info("Load outbox for {}", currentUserDetails.getAppUser().getId());

		return messageService.getOutbox(currentUserDetails.getAppUser());
	}
}
