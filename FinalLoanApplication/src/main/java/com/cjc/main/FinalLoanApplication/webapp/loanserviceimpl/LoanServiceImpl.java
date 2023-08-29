package com.cjc.main.FinalLoanApplication.webapp.loanserviceimpl;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
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

import com.cjc.main.FinalLoanApplication.webapp.entity.Cibil;
import com.cjc.main.FinalLoanApplication.webapp.entity.Customer;
import com.cjc.main.FinalLoanApplication.webapp.entity.EnquiryDetails;
import com.cjc.main.FinalLoanApplication.webapp.entity.MailDetails;
import com.cjc.main.FinalLoanApplication.webapp.entity.Users;
import com.cjc.main.FinalLoanApplication.webapp.enums.Cibil_Status;
import com.cjc.main.FinalLoanApplication.webapp.enums.Currentloanstatus;
import com.cjc.main.FinalLoanApplication.webapp.enums.Enquiry_Status;
import com.cjc.main.FinalLoanApplication.webapp.enums.PaymentStatus;
import com.cjc.main.FinalLoanApplication.webapp.loanRepo.LoanRepositoryForUsers;
import com.cjc.main.FinalLoanApplication.webapp.loanRepo.RepoForCustomer;
import com.cjc.main.FinalLoanApplication.webapp.loanRepo.RepoForEnquiry;
import com.cjc.main.FinalLoanApplication.webapp.loanservice.LoanService;

@Service
public class LoanServiceImpl implements LoanService {

	@Autowired
	LoanRepositoryForUsers lr;
	
	@Autowired
	RepoForEnquiry re;
	
	@Autowired
	RepoForCustomer rc;
	
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
		e.setCaseid("CD"+rd.nextInt(8888)+rd.nextInt(9999));
		
		sm.setFrom(fromMail);
		sm.setTo(e.getEmail());
		sm.setSubject("RESPONSE TO YOUR LOAN ENQUIRY");
		sm.setText("YOUR ENQUIRY HAS BEEN RECEIVED. "
				+ "WE WILL UPDATE YOU SOON."+"YOUR CASEID IS:-"+e.getCaseid());
		
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
		else {
Iterable<EnquiryDetails> all = re.findAllByEnquiryStatusOrEnquiryStatus(enquirystatus1,enquirystatus2);
			
			return all;
		}
		
		
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
	public EnquiryDetails updatestatus(int eid,EnquiryDetails ed) {
			
		System.out.println("in service update");
			EnquiryDetails e = re.findByEid(eid);
			if(e.getCibil()==null)
			{
				e.setCibil(new Cibil());
			}
			String enquiryStatus = e.getEnquiryStatus();
			if(enquiryStatus.equals("CREATED"))
			{
				
				e.setEnquiryStatus(Enquiry_Status.CIBIL_REQUIRED.toString());
			
				re.save(e);
				return e;
			}else if(enquiryStatus.equals("CIBIL_REQUIRED"))
			{
				
				e.setEnquiryStatus(Enquiry_Status.CIBIL_CHECKED.toString());
				e.getCibil().setCibilScore(ed.getCibil().getCibilScore());;
				if(e.getCibil().getCibilScore()<650)
				{	
					e.getCibil().setCibilRemark("cibil score is low");
					e.getCibil().setCibilStatus(Cibil_Status.LOW_CIBIL.toString());
					e.getCibil().setCibilScoreDateTime(new Date().toString());
					return re.save(e);
				}
				else if(e.getCibil().getCibilScore()<750 && e.getCibil().getCibilScore()>650)
				{
					e.getCibil().setCibilRemark("cibil score is good");
					e.getCibil().setCibilStatus(Cibil_Status.AVRAGE_CIBIL.toString());
					e.getCibil().setCibilScoreDateTime(new Date().toString());
					return re.save(e);
				}
				else if(e.getCibil().getCibilScore()>750 && e.getCibil().getCibilScore()<900)
				{
					e.getCibil().setCibilRemark("cibil score is high");
					e.getCibil().setCibilStatus(Cibil_Status.HIGH_CIBIL.toString());
					e.getCibil().setCibilScoreDateTime(new Date().toString());
					return re.save(e);
				}else{
					//throw CibilCoreNotApplicabelException
					
				}
			
		
			}
			if(enquiryStatus.equals("CIBIL_CHECKED"))
			{
				e.setEnquiryStatus(Enquiry_Status.CREDIT_STATE.toString());
				return re.save(e);
			}
			else
			{
				//exxception
			}
			
			 if(enquiryStatus.equals("CREDIT_STATE"))
			{
				 if(e.getCibil().getCibilScore()>650)
				 {
					 
					 e.setEnquiryStatus(Enquiry_Status.APPROVED.toString());
					 return re.save(e);
				 }
				 else {
					 e.setEnquiryStatus(Enquiry_Status.REJECTED.toString());
					return re.save(e);
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
	public MailDetails sendmailwithattachment(MailDetails mailDetails, MultipartFile attachment) {
		MimeMessage mm = sender.createMimeMessage();
		
		try {
			MimeMessageHelper mmh=new MimeMessageHelper(mm, true);
			mmh.setFrom(fromMail);
			mmh.setTo(mailDetails.getToMail());
			mmh.setSubject(mailDetails.getSubject());
			mmh.setText(mailDetails.getText());
			
			
			sender.send(mm);
			
			return mailDetails;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return mailDetails;
		}
	}


	@Override
	public MailDetails sendadharMail(MailDetails m,String adharnumber) {
		
		if(adharnumber.length()==12)
		{
			m.setSubject("ADHAR VERIFICATION OTP");
			m.setText(String.valueOf(rd.nextInt(9999)));
			SimpleMailMessage sm=new SimpleMailMessage();
			sm.setFrom(fromMail);
			sm.setTo(m.getToMail());
			sm.setSubject(m.getSubject());
			sm.setText("Your Adhar OTP is:-"+" "+m.getText());
			sender.send(sm);
			return m;
			
		}
		else {
			//adhar not found exception
		}
		
		
		return null;
	}
	
	@Override
	public Customer addAppForm(Customer c, MultipartFile addressproof, MultipartFile panCard, MultipartFile addharCard,
			MultipartFile photo, MultipartFile signature, MultipartFile salarySlips) {

		try {
			c.getAllPersonalDoc().setAddressProof(addressproof.getBytes());
			c.getAllPersonalDoc().setPanCard(panCard.getBytes());
			c.getAllPersonalDoc().setAddharCard(addharCard.getBytes());
			c.getAllPersonalDoc().setPhoto(photo.getBytes());
			c.getAllPersonalDoc().setSignature(signature.getBytes());
			c.getAllPersonalDoc().setSalarySlips(salarySlips.getBytes());
			
			double customerMobileNumber = c.getCustomerMobileNumber();
			EnquiryDetails e = re.findByMobileNumber(customerMobileNumber);
			c.getCibilScore().setCibilScore(e.getCibil().getCibilScore());
			c.getCibilScore().setCibilScoreDateTime(String.valueOf(new Date()));
			c.getCibilScore().setCibilStatus(e.getCibil().getCibilStatus());
			c.getCibilScore().setCibilRemark(e.getCibil().getCibilRemark());
			c.getCurrentLoanDetails().setCurrentLoanNumber(e.getCaseid());
			
			 c.getCurrentLoanDetails().setSanctionDate(String.valueOf(new Date()));
			
			 Date d=new Date();
			 
			   Calendar cal = Calendar.getInstance();
		        cal.setTime(d);
		        cal.add(Calendar.DATE, 30);
		          Date d2 = cal.getTime();
			
			c.getCurrentLoanDetails().getEmiDetails().setNextEmiDueDate(String.valueOf(d2));
			c.setCustomerstatus(Currentloanstatus.INPROCESS.toString());
			c.getCurrentLoanDetails().setRemark("OK");
			
			c.getCurrentLoanDetails().setStatus(Currentloanstatus.INPROCESS.toString());
			
			return rc.save(c);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return null;
	}
	
	

	




	@Override
	public List<Customer> getappforms(String status1,String status2) {
		
		if(status2.startsWith("no"))
		{
			List<Customer> list = rc.findAllByCustomerstatusOrCustomerstatus(status1, status2);
			
			return list;
		}
		else
		{
			List<Customer> list = rc.findAllByCustomerstatusOrCustomerstatus(status1, status2);
			return list;
		}
	}


	@Override
	public Customer updateloanstatus(int customerId,Customer c) {

		Customer c2 = rc.findByCustomerId(customerId);
		System.out.println(c2);
		String s = c2.getCustomerstatus();
		System.out.println(s);
		
		if(c2.getCustomerstatus().equals("INPROCESS"))
		{
			System.out.println(c2);
			c2.getCurrentLoanDetails().setStatus(Currentloanstatus.VERIFICATION_STATE.toString());
			c2.setCustomerstatus(Currentloanstatus.VERIFICATION_STATE.toString());
			rc.save(c2);
			return c2;
		}
		else if(c2.getCustomerstatus()=="VERIFICATION_STATE")
		{
			c2.getCurrentLoanDetails().setStatus(Currentloanstatus.VERIFICATION_DONE.toString());
			c2.getCustomerverification().setVerificationDate(String.valueOf(new Date()));
			c2.getCustomerverification().setRemarks("Verified");
			c2.setCustomerstatus(Currentloanstatus.VERIFICATION_DONE.toString());
			c2.getCustomerverification().setStatus(Currentloanstatus.VERIFICATION_DONE.toString());
			rc.save(c2);
			return c2;
		}
		else if(c2.getCustomerstatus()=="VERIFICATION_DONE")
		{
			c2.setCustomerstatus(Currentloanstatus.SANCTIONED.toString());
			c2.getCurrentLoanDetails().setStatus(Currentloanstatus.SANCTIONED.toString());
			c2.getSanctionLetter().setSanctionDate(String.valueOf(new Date()));
			c2.getSanctionLetter().setApplicantName(c2.getCustomerName());
			c2.getSanctionLetter().setContactDetails(c2.getCustomerMobileNumber());
			c2.getSanctionLetter().setLoanAmtSanctioned(c2.getCurrentLoanDetails().getLoanAmount());
			c2.getSanctionLetter().setRateOfInterest(c2.getCurrentLoanDetails().getRateOfInterest());
			c2.getSanctionLetter().setLoanTenure(c2.getCurrentLoanDetails().getTenure());
			c2.getSanctionLetter().setMonthlyEmiAmount(c2.getCurrentLoanDetails().getEmiDetails().getEmiAmountMonthly());
			c2.getSanctionLetter().setSanctionStatus(Currentloanstatus.SANCTIONED.toString());
			c2.getCurrentLoanDetails().setStatus(Currentloanstatus.SANCTIONED.toString());
			
			rc.save(c2);
			return c2;
		}
		else if(c2.getCustomerstatus()=="SANCTIONED")
		{
			c2.setCustomerstatus(Currentloanstatus.DISBURSED.toString());

			c2.getCurrentLoanDetails().setStatus(Currentloanstatus.DISBURSED.toString());
			String cn = c2.getCurrentLoanDetails().getCurrentLoanNumber();
			c2.getLoanDisbursement().setLoanNo(cn);
			c2.getLoanDisbursement().setAgreementDate(String.valueOf(new Date()));
			c2.getLoanDisbursement().setTotalAmount(c2.getSanctionLetter().getLoanAmtSanctioned());
			c2.getLoanDisbursement().setBankName(c2.getAccountDetails().getBankname());
			c2.getLoanDisbursement().setAccountNumber(c2.getAccountDetails().getAccountNumber());
			c2.getLoanDisbursement().setTransferAmount(c2.getSanctionLetter().getLoanAmtSanctioned());
			c2.getLoanDisbursement().setPaymentStatus(PaymentStatus.TRANSFERED.toString());
			c2.getLoanDisbursement().setAmountPaidDate(String.valueOf(new Date()));
			
			//also send mail
			
			rc.save(c2);
			
			return c2;
		}
		else if(c2.getCustomerstatus()=="DISBURSED")
		{
			

			c2.getLedger().setLedgerCreatedDate(String.valueOf(new Date()));
			c2.getLedger().setTotalLoanAmount(c2.getSanctionLetter().getLoanAmtSanctioned());
			c2.getLedger().setPayableAmountwithInterest(c2.getCurrentLoanDetails().getTotalAmountToBePaidDouble());
			c2.getLedger().setTenure(c2.getCurrentLoanDetails().getTenure());
			c2.getLedger().setMonthlyEMI(c2.getCurrentLoanDetails().getEmiDetails().getEmiAmountMonthly());
			
			
	    }
		
		return null;
		
	}


	@Override
	public EnquiryDetails getsingleEnq(String pancardNumber) {

		EnquiryDetails ed=re.findByPancardNumber(pancardNumber);
		System.out.println(ed.getPancardNumber());
		
		return ed;
	}


	



	
	


	

	
	


}
