package com.banks.messagingapp.repository;

import com.banks.messagingapp.dto.InboxDto;
import com.banks.messagingapp.dto.OutboxDto;
import com.banks.messagingapp.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

	@Query(value = "select new com.banks.messagingapp.dto.InboxDto(m.sender.nickname, m.text, m.created) " +
					" from Message m JOIN AppUser a on m.sender.id=a.id where m.recipient.id=?1")
	List<InboxDto> findAllByRecipient(Long recipientId);

	@Query(value = "select new com.banks.messagingapp.dto.InboxDto(m.sender.nickname, m.text, m.created) " +
					" from Message m JOIN AppUser a on m.sender.id=a.id where m.sender.id=?1 and m.recipient.id=?2")
	List<InboxDto> findAllBySenderAndReipient(Long senderId, Long recipientId);

	@Query(value = "select new com.banks.messagingapp.dto.OutboxDto(m.recipient.nickname, m.text, m.created) " +
					" from Message m JOIN AppUser a on m.sender.id=a.id where m.sender.id=?1")
	List<OutboxDto> findAllBySender(Long senderId);
}
