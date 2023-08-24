package com.cjc.main.FinalLoanApplication.webapp.loanserviceimpl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.activation.FileTypeMap;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cjc.main.FinalLoanApplication.webapp.entity.EnquiryDetails;
import com.cjc.main.FinalLoanApplication.webapp.entity.MailDetails;
import com.cjc.main.FinalLoanApplication.webapp.entity.Users;
import com.cjc.main.FinalLoanApplication.webapp.enums.Enquiry_Status;
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
		
		SimpleMailMessage sm=new SimpleMailMessage();
		
		e.setEnquiryStatus(Enquiry_Status.CREATED.toString());
		
		
		sm.setFrom(fromMail);
		sm.setTo(e.getEmail());
		sm.setSubject("RESPONSE TO YOUR LOAN ENQUIRY");
		sm.setText("YOUR ENQUIRY HAS BEEN RECEIVED. "
				+ "WE WILL UPDATE YOU SOON.");
		
		sender.send(sm);
		
		return 	re.save(e);
	}



	@Override
	public Iterable<EnquiryDetails> getenquiry(String enquirystatus1,String enquirystatus2) {
		
		if(enquirystatus2.startsWith("no"))
		{
			
			Iterable<EnquiryDetails> all = re.findAllByEnquiryStatusOrEnquiryStatus(enquirystatus1,enquirystatus2);
			
			return all;
		}
		return null;
		
	}


	@Override
	public List<Users> getallusers() {
		
		List<Users>findAll=lr.findAll();
		
		return findAll;
	}


	@Override
	public void deleteusers( int userId) {
		
		lr.deleteById(userId);
		
	}


	@Override
	public EnquiryDetails updatestatus(int eid) {
			
		System.out.println("in service update");
			EnquiryDetails e = re.findByEid(eid);
			
			String enquiryStatus = e.getEnquiryStatus();
			if(enquiryStatus.equals("CREATED"))
			{
				
				e.setEnquiryStatus(Enquiry_Status.CIBIL_REQUIRED.toString());
				re.save(e);
				return e;
			}else if(enquiryStatus.equals("CIBIL_REQUIRED"))
			{
				
				e.setEnquiryStatus(Enquiry_Status.CIBIL_CHECKED.toString());
				re.save(e);
				return e;
			}
			else if (enquiryStatus.equals("CIBIL_CHECKED")) {
				if(e.getCibil().getCibilScore()>650)
				{
					e.setEnquiryStatus(Enquiry_Status.APPROVED.toString());
					re.save(e);
					return e;
				}else {
					e.setEnquiryStatus(Enquiry_Status.REJECTED.toString());
					re.save(e);
					return e;
				}
				
			}
			return null;
	}


	@Override
	public Users getsingleuser(String userType) {

			Users users = lr.findAllByUserType(userType);
		
		return users;
	}


	@Override
	public String sendmailwithattachment(MailDetails mailDetails, MultipartFile attachment) {
		MimeMessage mm = sender.createMimeMessage();
		
		try {
			MimeMessageHelper mmh=new MimeMessageHelper(mm, true);
			mmh.setFrom(fromMail);
			mmh.setTo(mailDetails.getToMail());
			mmh.setSubject(mailDetails.getSubject());
			mmh.setText(mailDetails.getText());
			
			
			sender.send(mm);
			
			return "mail send";
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "mail not send";
		}
	}



	
	


	

	
	


}
