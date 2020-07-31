package com.banks.messagingapp.service;

import com.banks.messagingapp.dto.QueueMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface IMessageSender {

	void send(QueueMessageDto message) throws JsonProcessingException;
}
