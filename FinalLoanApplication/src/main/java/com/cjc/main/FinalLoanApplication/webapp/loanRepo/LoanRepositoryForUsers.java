package com.cjc.main.FinalLoanApplication.webapp.loanRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cjc.main.FinalLoanApplication.webapp.entity.Users;

public interface LoanRepositoryForUsers extends JpaRepository<Users, Integer>{
	
	Users findAllByUserNameAndPassWord(String userName,String passWord);
	Users findAllByUserType(String userType);

}
