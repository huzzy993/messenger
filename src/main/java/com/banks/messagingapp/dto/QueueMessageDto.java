package com.banks.messagingapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueueMessageDto {

	private Long messageId;

	private String message;

	private Long senderId;

	private Long recipientId;

}
