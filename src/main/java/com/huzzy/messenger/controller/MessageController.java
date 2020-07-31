package com.huzzy.messenger.controller;

import com.huzzy.messenger.dto.InboxDto;
import com.huzzy.messenger.dto.OutboxDto;
import com.huzzy.messenger.dto.SendMessageRequestDto;
import com.huzzy.messenger.dto.SendMessageResponseDto;
import com.huzzy.messenger.model.CurrentUserDetails;
import com.huzzy.messenger.service.MessageService;
import com.huzzy.messenger.util.Constants;
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
import javax.validation.Valid;

import static com.huzzy.messenger.controller.MessageController.MESSAGES_BASE_PATH;

@RestController
@RequestMapping(MESSAGES_BASE_PATH)
@AllArgsConstructor
@Slf4j
public class MessageController {

	public static final String MESSAGES_BASE_PATH = "/messages";

	private final MessageService messageService;

	private final CurrentUserDetails currentUserDetails;

	@PostMapping
	@ApiImplicitParam(name = Constants.USER_ID_HEADER, value = "Current User Id", required = true, paramType = "header",
					dataTypeClass = Long.class, example = "1234")
	public SendMessageResponseDto sendMessage(@RequestBody @Valid SendMessageRequestDto messageRequestDto) throws JsonProcessingException {
		Long messageId = messageService.sendMessage(currentUserDetails.getAppUser(), messageRequestDto);
		return SendMessageResponseDto.builder().messageId(messageId).build();
	}

	@GetMapping("inbox")
	@ApiImplicitParam(name = Constants.USER_ID_HEADER, value = "Current User Id", required = true, paramType = "header",
					dataTypeClass = Long.class, example = "1234")
	public List<InboxDto> loadInbox() {
		log.info("Load inbox for userId {}", currentUserDetails.getAppUser().getId());

		return messageService.getInbox(currentUserDetails.getAppUser());
	}

	@GetMapping("inbox/filter")
	@ApiImplicitParam(name = Constants.USER_ID_HEADER, value = "Current User Id", required = true, paramType = "header",
					dataTypeClass = Long.class, example = "1234")
	public List<InboxDto> filterInbox(@RequestParam("senderId") Long senderId) {
		log.info("Filter inbox by senderId {} for userId {}", senderId, currentUserDetails.getAppUser().getId());

		return messageService.filterInboxBySender(currentUserDetails.getAppUser(), senderId);
	}

	@GetMapping("outbox")
	@ApiImplicitParam(name = Constants.USER_ID_HEADER, value = "Current User Id", required = true, paramType = "header",
					dataTypeClass = Long.class, example = "1234")
	public List<OutboxDto> loadOutbox() {
		log.info("Load outbox for userId {}", currentUserDetails.getAppUser().getId());

		return messageService.getOutbox(currentUserDetails.getAppUser());
	}
}
