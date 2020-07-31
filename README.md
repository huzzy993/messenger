#### Description
Task: Create a backend for a messaging application

Requirements 1 to 5 from the task pdf have been implemented.

An Amazon SQS queue was used to simulate sending. 

##### Improvemnt Ideas:
1. Make inbox, outbox and inboxFilter queries paginated to prevent endpoint from abuse and server from overload.
2. Database secrets should not be in source code but in an external secrets manager such as Vault.
3. More unit tests need to be added for the service layer. However, integration tests that cover all the functional requirements have been added.

#### Running

#### Prerequisites
1. Docker (Postgres and Localstack containers will be started)
2. Java 8

#### Steps
1. Make run script executable by executing `chmod +x ./run.sh` in a terminal
2. Execute `./run.sh`
3. Use CURL to make requests or use Swagger Dashboard located at `localhost:8080/swagger-ui.html`
