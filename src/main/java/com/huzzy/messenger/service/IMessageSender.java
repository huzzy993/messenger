package com.huzzy.messenger.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.huzzy.messenger.dto.QueueMessageDto;

public interface IMessageSender {

	void send(QueueMessageDto message) throws JsonProcessingException;
}
