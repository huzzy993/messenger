package com.huzzy.messenger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.stream.Stream;

import static org.testcontainers.containers.PostgreSQLContainer.POSTGRESQL_PORT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

@SpringBootTest
@ExtendWith(value = {SpringExtension.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IntegrationTest {

	static LocalStackContainer localStackContainer;

	static PostgreSQLContainer postgreSQLContainer;

	static {
		localStackContainer = new LocalStackContainer("0.10.8")
						.withServices(SQS)
						.waitingFor(Wait.forLogMessage(".*Ready\\.\n", 1));

		postgreSQLContainer = new PostgreSQLContainer("postgres:10.13")
						.withDatabaseName("messenger-test")
						.withPassword("messenger-test")
						.withUsername("messenger-test");

		Stream.of(localStackContainer, postgreSQLContainer).parallel().forEach(GenericContainer::start);

		System.setProperty("sqs.port", String.valueOf(localStackContainer.getMappedPort(SQS.getPort())));
		System.setProperty("postgres.port", String.valueOf(postgreSQLContainer.getMappedPort(POSTGRESQL_PORT)));
	}

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;
}
