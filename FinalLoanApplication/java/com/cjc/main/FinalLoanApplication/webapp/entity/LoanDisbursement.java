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
public class LoanDisbursement {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int agreementId;
	private int loanNo;
	private String agreementDate;
	private double totalAmount;
	private String bankName;
	private long accountNumber;
	private double transferAmount;
	private String paymentStatus;
	private String amountPaidDate;


}
