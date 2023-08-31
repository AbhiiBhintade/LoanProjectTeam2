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
	private String customerstatus;
//	private Byte[] applicationpdf;
	@OneToOne(cascade = CascadeType.ALL)
	private AllPersonalDocs allPersonalDoc=new AllPersonalDocs();
	@OneToOne(cascade = CascadeType.ALL)
	private CustomerAddress customerAddress=new CustomerAddress();
	@OneToOne(cascade = CascadeType.ALL)
	private Profession profession=new Profession();
	@OneToOne(cascade = CascadeType.ALL)
	private Cibil2 cibilScore=new Cibil2();
	@OneToOne(cascade = CascadeType.ALL)
	private CurrentLoanDetails currentLoanDetails=new CurrentLoanDetails();
	@OneToOne(cascade = CascadeType.ALL)
	private AccountDetails accountDetails=new AccountDetails();
	@OneToOne(cascade = CascadeType.ALL)
	private GuarantorDetails gurantorDetails=new GuarantorDetails();
	@OneToOne(cascade = CascadeType.ALL)
	private LoanDisbursement loanDisbursement=new LoanDisbursement();
	@OneToOne(cascade = CascadeType.ALL)
	private Ledger ledger=new Ledger();
	@OneToOne(cascade = CascadeType.ALL)
	private SanctionLetter sanctionLetter=new SanctionLetter();
	@OneToOne(cascade = CascadeType.ALL)
	private CustomerVerification customerverification=new CustomerVerification();



}





































		

