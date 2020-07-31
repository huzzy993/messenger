package com.huzzy.messenger.service;

import com.huzzy.messenger.dto.QueueMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface IMessageSender {

	void send(QueueMessageDto message) throws JsonProcessingException;
}
