package com.cjc.main.FinalLoanApplication.webapp.loanserviceimpl;

import java.io.IOException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cjc.main.FinalLoanApplication.webapp.entity.Users;
import com.cjc.main.FinalLoanApplication.webapp.loanRepo.LoanRepositoryForUsers;
import com.cjc.main.FinalLoanApplication.webapp.loanservice.LoanService;

@Service
public class LoanServiceImpl implements LoanService {

	@Autowired
	LoanRepositoryForUsers lr;
	
	
	Random rd=new Random(7777);
	
	
	@Override
	public Users adduser(Users u, MultipartFile profilephoto) {
		
		try {
			u.setProfilephoto(profilephoto.getBytes());
			u.setUserName(u.getName()+""+u.getUserType()+"@"+rd.nextInt(7777));
			u.setPassWord(u.getUserType()+"@"+rd.nextInt(7777));
			
			return lr.save(u);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
