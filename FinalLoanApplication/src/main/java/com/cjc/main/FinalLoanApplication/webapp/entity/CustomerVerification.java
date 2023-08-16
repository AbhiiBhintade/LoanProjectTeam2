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
public class CustomerVerification {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int verificationID;
	private String verificationDate;
	private String status;
	private String remarks;

}
