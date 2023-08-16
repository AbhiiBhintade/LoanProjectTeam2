package com.cjc.main.FinalLoanApplication.webapp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CurrentLoanDetails {
	
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int currentloanId;
	private int currentLoanNumber;
	
	@OneToOne(cascade = CascadeType.ALL)
	private EMIDetails emiDetails;
	private double loanAmount;
	private int rateOfInterest;
	private int tenure;
	private double totalAmountToBePaidDouble;
	private int processingFees;
	private double totalInterest;
	private String sanctionDate;
	private String remark;
	private String status;



}
