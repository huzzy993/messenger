package com.huzzy.messenger.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.huzzy.messenger.dto.QueueMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
@Slf4j
public class SqsSender implements IMessageSender {

	private static final String SQS_QUEUE_NAME = "message-queue";

	private final AmazonSQS amazonSQS;

	private final QueueMessagingTemplate queueMessagingTemplate;

	private final ObjectMapper objectMapper;

	@Override
	public void send(QueueMessageDto message) throws JsonProcessingException {
		String payload = objectMapper.writeValueAsString(message);
		log.info("Sending to sqs: {}", payload);
		queueMessagingTemplate.send(SQS_QUEUE_NAME, new GenericMessage<>(payload));
	}

	@SqsListener(SQS_QUEUE_NAME)
	public void receiveMessage(@Payload String payload) {
		log.info("Polled from sqs: {}", payload);
	}

	@PostConstruct
	public void setup() {
		log.info("Queue initial setup");
		amazonSQS.createQueue(SQS_QUEUE_NAME);
		amazonSQS.listQueues().getQueueUrls().forEach(url -> log.info("Queue url {}", url));
	}
}
