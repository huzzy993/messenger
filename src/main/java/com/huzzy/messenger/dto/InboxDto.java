package com.huzzy.messenger.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class InboxDto {

	private String from;

	private String message;

	private Date date;
}
