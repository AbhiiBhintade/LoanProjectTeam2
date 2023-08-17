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
public class SanctionLetter {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int sanctionId;
	private String sanctionDate;
	private String applicantName;
	private double contactDetails;
	private double loanAmtSanctioned;
	private int rateOfInterest;
	private int loanTenure;
	private double monthlyEmiAmount;
	private String sanctionStatus;


}
