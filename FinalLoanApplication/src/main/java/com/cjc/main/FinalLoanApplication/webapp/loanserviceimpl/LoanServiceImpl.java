package com.cjc.main.FinalLoanApplication.webapp.loanserviceimpl;

import java.io.IOException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cjc.main.FinalLoanApplication.webapp.entity.EnquiryDetails;
import com.cjc.main.FinalLoanApplication.webapp.entity.MailDetails;
import com.cjc.main.FinalLoanApplication.webapp.entity.Users;
import com.cjc.main.FinalLoanApplication.webapp.loanRepo.LoanRepositoryForUsers;
import com.cjc.main.FinalLoanApplication.webapp.loanRepo.RepoForEnquiry;
import com.cjc.main.FinalLoanApplication.webapp.loanservice.LoanService;

@Service
public class LoanServiceImpl implements LoanService {

	@Autowired
	LoanRepositoryForUsers lr;
	
	@Autowired
	RepoForEnquiry re;
	
	@Value("${spring.mail.username}")
	private String fromMail;
	
	
	@Autowired
	private JavaMailSender sender;
	
	
	  @Autowired
	  private Random rd;
	
	
	@Override
	public Users adduser(Users u, MultipartFile profilephoto) {
		
		try {
			
			SimpleMailMessage sm=new SimpleMailMessage();
			u.setProfilephoto(profilephoto.getBytes());
			u.setUserName(u.getName()+""+u.getUserType()+"@"+rd.nextInt(7777));
			u.setPassWord(u.getUserType()+"@"+rd.nextInt(7777));
			
			sm.setFrom(fromMail);
			sm.setTo(u.getEmail());
			sm.setSubject("YOUR USERNAME AND PASSWORD");
			sm.setText("YOUR USERNAME IS:-"+u.getUserName()+
					"YOUR PASSWORD IS:-"+u.getPassWord());
			
			
			
			sender.send(sm);
			
			return lr.save(u);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}


	@Override
	public Users getuserdata(String userName, String passWord) {
		
		Users users = lr.findAllByUserNameAndPassWord(userName, passWord);
		
		return users;
	}


	@Override
	public EnquiryDetails addenquiry(EnquiryDetails e) {
		
		return 	re.save(e);
	}


//	@Override
//	public void sendMail(MailDetails md) {
//		
//		
//		sm.setFrom(fromMail);
//		sm.setTo(md.getToMail());
//		sm.setText(md.getText());
//		sm.setSubject(md.getSubject());
//		
//		sender.send(sm);
//		
//		
	

}
