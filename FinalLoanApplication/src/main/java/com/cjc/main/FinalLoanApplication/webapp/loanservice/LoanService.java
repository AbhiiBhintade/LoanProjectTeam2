package com.cjc.main.FinalLoanApplication.webapp.loanservice;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cjc.main.FinalLoanApplication.webapp.entity.Customer;
import com.cjc.main.FinalLoanApplication.webapp.entity.EnquiryDetails;
import com.cjc.main.FinalLoanApplication.webapp.entity.MailDetails;
import com.cjc.main.FinalLoanApplication.webapp.entity.Users;

public interface LoanService {

	Users adduser(Users u, MultipartFile profilephoto);

	Users getuserdata(String userName, String passWord);

	EnquiryDetails addenquiry(EnquiryDetails e);


	Iterable<EnquiryDetails> getenquiry(String enquirystatus1,String enquirystatus2);

	List<Users> getallusers();

	void deleteusers( int userId);

	EnquiryDetails updatestatus(int eid,EnquiryDetails ed);


	Users getsingleuser(String userType);

	MailDetails sendmailwithattachment(MailDetails mailDetails, MultipartFile attachment);

	MailDetails sendadharMail(MailDetails m,String adharnumber);

	List<Customer> getappforms(String status1,String status2);

	Customer updateloanstatus(int customerId,Customer c);

	Customer addAppForm(Customer c, MultipartFile addressproof, MultipartFile panCard, MultipartFile addharCard,
			MultipartFile photo, MultipartFile signature, MultipartFile salarySlips);

	EnquiryDetails getsingleEnq(String pancardNumber);




}
