package com.cjc.main.FinalLoanApplication.webapp.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int accountId;
	private String accounType;
	private String accountHolderName;
	private long accountNumber;


}
