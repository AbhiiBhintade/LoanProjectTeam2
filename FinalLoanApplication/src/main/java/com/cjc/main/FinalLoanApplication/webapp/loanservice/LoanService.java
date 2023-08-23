package com.cjc.main.FinalLoanApplication.webapp.loanservice;

import org.springframework.web.multipart.MultipartFile;

import com.cjc.main.FinalLoanApplication.webapp.entity.EnquiryDetails;
import com.cjc.main.FinalLoanApplication.webapp.entity.MailDetails;
import com.cjc.main.FinalLoanApplication.webapp.entity.Users;

public interface LoanService {

	Users adduser(Users u, MultipartFile profilephoto);

	Users getuserdata(String userName, String passWord);

	EnquiryDetails addenquiry(EnquiryDetails e);

}
