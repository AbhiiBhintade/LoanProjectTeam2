package com.cjc.main.FinalLoanApplication.webapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDetails {
	
	private String toMail;
	private String text;
	private String subject;
	

}
