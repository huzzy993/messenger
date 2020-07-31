package com.banks.messagingapp.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class AppUser {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, unique = true)
	private String nickname;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated;

}
