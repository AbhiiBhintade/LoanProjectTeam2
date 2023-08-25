package com.cjc.main.FinalLoanApplication.webapp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.context.support.StaticApplicationContext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class EnquiryDetails {

	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int eid;
	private String firstName;
	private String lastName;
	private int age;
	private String email;
	private Double mobileNumber;
	private String pancardNumber;
	private String enquiryStatus;
	private String caseid;
	@OneToOne(cascade = CascadeType.ALL)
	private Cibil cibil=new Cibil();


}
