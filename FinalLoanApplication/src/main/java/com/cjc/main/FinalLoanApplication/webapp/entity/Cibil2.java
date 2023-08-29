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
public class Cibil2 {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cibilId;
	private int	cibilScore;
	private String cibilScoreDateTime;
	private String cibilStatus;
	private String cibilRemark;


}
