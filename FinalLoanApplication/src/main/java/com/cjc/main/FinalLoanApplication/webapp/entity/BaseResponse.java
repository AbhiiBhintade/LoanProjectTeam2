package com.cjc.main.FinalLoanApplication.webapp.entity;

import java.util.Date;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {

	private int responsestatus;
	private String responsemsg;
	private Date date;
	private T responsedata;
}
