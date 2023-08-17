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
public class AllPersonalDocs

 {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int documentId;
	private byte[] addressProof;
	private byte[] panCard;
	private byte[] addharCard;
	private byte[] photo;
	private byte[] signature;
	private byte[] salarySlips;


}
