package com.cjc.main.FinalLoanApplication.webapp.loanservice;

import org.springframework.web.multipart.MultipartFile;

import com.cjc.main.FinalLoanApplication.webapp.entity.Users;

public interface LoanService {

	Users adduser(Users u, MultipartFile profilephoto);

}
