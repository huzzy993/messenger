package com.banks.messagingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class OutboxDto {

	private String to;

	private String message;

	private Date date;
}
