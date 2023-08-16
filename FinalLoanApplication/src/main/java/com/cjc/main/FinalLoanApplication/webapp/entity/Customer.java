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
public class Customer {
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int customerId;
	private String customerName;
	private String customerDateOfBirth;
	private int customerAge;
	private String customerGender;
	private String customerEmail;
	private double customerMobileNumber;
	private double customerTotalLoanRequired;
	@OneToOne(cascade = CascadeType.ALL)
	private AllPersonalDocs allPersonalDoc;
	@OneToOne(cascade = CascadeType.ALL)
	private CustomerAddress customerAddress;
	@OneToOne(cascade = CascadeType.ALL)
	private Profession profession;
	@OneToOne(cascade = CascadeType.ALL)
	private Cibil cibilScore;
	@OneToOne(cascade = CascadeType.ALL)
	private CurrentLoanDetails currentLoanDetails;
	@OneToOne(cascade = CascadeType.ALL)
	private AccountDetails accountDetails;
	@OneToOne(cascade = CascadeType.ALL)
	private GuarantorDetails gurantorDetails;
	@OneToOne(cascade = CascadeType.ALL)
	private LoanDisbursement loanDisbursement;
	@OneToOne(cascade = CascadeType.ALL)
	private Ledger ledger;
	@OneToOne(cascade = CascadeType.ALL)
	private SanctionLetter sanctionLetter;
	@OneToOne(cascade = CascadeType.ALL)
	private CustomerVerification customerverification;



}
