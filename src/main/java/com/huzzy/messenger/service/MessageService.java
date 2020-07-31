package com.huzzy.messenger.service;

import com.huzzy.messenger.dto.InboxDto;
import com.huzzy.messenger.dto.OutboxDto;
import com.huzzy.messenger.dto.QueueMessageDto;
import com.huzzy.messenger.dto.SendMessageRequestDto;
import com.huzzy.messenger.entity.AppUser;
import com.huzzy.messenger.entity.Message;
import com.huzzy.messenger.exception.SameSenderAndRecipientException;
import com.huzzy.messenger.exception.UserNotFoundException;
import com.huzzy.messenger.repository.MessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class MessageService {

	private final IMessageSender messageSender;

	private final AppUserService appUserService;

	private final MessageRepository messageRepository;

	@Transactional
	public Long sendMessage(AppUser sender, SendMessageRequestDto messageRequestDto) throws JsonProcessingException {
		Long senderId = sender.getId();
		Long recipientId = messageRequestDto.getRecipientId();

		if (senderId.equals(recipientId)) {
			throw new SameSenderAndRecipientException();
		}

		Optional<AppUser> recipientOpt = appUserService.findUser(recipientId);
		if (!recipientOpt.isPresent()) {
			throw new UserNotFoundException("Recipient does not exist");
		}

		AppUser recipient = recipientOpt.get();
		Message message = Message.builder()
						.text(messageRequestDto.getMessage())
						.sender(sender)
						.recipient(recipient)
						.build();

		messageRepository.save(message);
		messageSender.send(buildQueueMessageDto(messageRequestDto, senderId, recipientId));

		return message.getId();
	}

	private QueueMessageDto buildQueueMessageDto(SendMessageRequestDto messageRequestDto, Long senderId,
																							 Long recipientId) {
		return QueueMessageDto.builder()
						.message(messageRequestDto.getMessage())
						.recipientId(recipientId)
						.senderId(senderId)
						.build();
	}

	public List<InboxDto> getInbox(AppUser appUser) {
		return messageRepository.findAllByRecipient(appUser.getId());
	}

	public List<InboxDto> filterInboxBySender(AppUser appUser, Long senderId) {
		Optional<AppUser> sender = appUserService.findUser(senderId);
		if (!sender.isPresent()) {
			throw new UserNotFoundException("Sender " + senderId + " not found");
		}
		return messageRepository.findAllBySenderAndReipient(sender.get().getId(), appUser.getId());
	}

	public List<OutboxDto> getOutbox(AppUser appUser) {
		return messageRepository.findAllBySender(appUser.getId());
	}
}
