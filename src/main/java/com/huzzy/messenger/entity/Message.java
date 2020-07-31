package com.huzzy.messenger.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class Message {

	@Id
	@GeneratedValue
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String text;

	@OneToOne
	private AppUser sender;

	@OneToOne
	private AppUser recipient;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated;
}
