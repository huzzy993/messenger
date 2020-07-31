package com.huzzy.messenger.config;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableSqs
public class SqsConfig {

	@Bean
	@Primary
	public AmazonSQSAsync sqs(@Value("${sqs.url}") String sqsUrl) {
		return AmazonSQSAsyncClientBuilder.standard()
						.withEndpointConfiguration(new EndpointConfiguration(sqsUrl, sqsUrl))
						.build();
	}

	@Bean
	public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
		return new QueueMessagingTemplate(amazonSQSAsync);
	}
}
