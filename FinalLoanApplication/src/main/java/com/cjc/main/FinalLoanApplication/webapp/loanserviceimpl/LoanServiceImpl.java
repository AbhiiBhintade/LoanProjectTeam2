package com.cjc.main.FinalLoanApplication.webapp.loanserviceimpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.activation.FileTypeMap;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.security.auth.message.callback.PrivateKeyCallback.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.cjc.main.FinalLoanApplication.webapp.entity.Cibil;
import com.cjc.main.FinalLoanApplication.webapp.entity.Customer;

import com.cjc.main.FinalLoanApplication.webapp.entity.EnquiryDetails;

import com.cjc.main.FinalLoanApplication.webapp.entity.InstallmentsDetails;
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
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

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
					 SimpleMailMessage sm=new SimpleMailMessage();
					    sm.setFrom(fromMail);
						sm.setTo(e.getEmail());
						sm.setSubject("Approved Application");
						sm.setText("YOUR APPLICATION"+e.getCaseid()+"IS APPROVED FILL APPLICATION FORM"+"   "+"http://localhost:4200/fillapp");
						
						sender.send(sm);
					 return re.save(e);
				 }
				 else {
					 e.setEnquiryStatus(Enquiry_Status.REJECTED.toString());
					 SimpleMailMessage sm=new SimpleMailMessage();
					    sm.setFrom(fromMail);
						sm.setTo(e.getEmail());
						sm.setSubject("Rejected Application");
						sm.setText("YOUR APPLICATION "+e.getCaseid()+"IS REJECTED BECAUSE"+e.getEnquiryStatus());
						sender.send(sm);
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
			
			double mb = c.getCustomerMobileNumber();
			
			EnquiryDetails e = re.findByMobileNumber(mb);
			
			
			
			c.getCibilScore().setCibilScore(e.getCibil().getCibilScore());
			c.getCibilScore().setCibilScoreDateTime(String.valueOf(new Date()));
			c.getCibilScore().setCibilStatus(e.getCibil().getCibilStatus());
			c.getCibilScore().setCibilRemark(e.getCibil().getCibilRemark());
			c.getCurrentLoanDetails().setCurrentLoanNumber(e.getCaseid());
			c.getCurrentLoanDetails().getEmiDetails().setEmiAmountMonthly(c.getCurrentLoanDetails().getEmiAmountMonthly() );
			
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


			String heading="Application Form";
			
			Date d3=new Date();
			
			DateFormat formt=new SimpleDateFormat("dd-MM-YYYY");
			       
			String date= "Date:- "+formt.format(d3);
		
	          Document document=new Document();
	          ByteArrayOutputStream out=new ByteArrayOutputStream();
	          
	          PdfWriter.getInstance(document, out);
	              
	          document.open();
	          
	    
	          
	          Font titleFont= FontFactory.getFont(FontFactory.COURIER_BOLD,25);
	           titleFont.setColor(CMYKColor.RED);
	          Paragraph titlePara=new Paragraph(heading ,titleFont);
	         
	          titlePara.setAlignment(Element.ALIGN_CENTER);
	          
	          document.add(titlePara);
	          
	          
	          
	          Font font =FontFactory.getFont(FontFactory.TIMES_BOLD,13);
	          Paragraph pdfdate=new Paragraph(date,font);
	          pdfdate.setSpacingBefore(25);
	          document.add(pdfdate);
	                                     // no of colums 
	          PdfPTable table=new PdfPTable(2);
	          table.setSpacingBefore(25);
	          table.setWidthPercentage(95f);
	          table.setWidths(new int[] {40,40});
	          
	          PdfPCell loannumber=new PdfPCell();
	          loannumber.setPadding(5);
	          loannumber.setPaddingLeft(15);
	          
	          
	          
	          loannumber.setPhrase(new Phrase("Loan Number" ,font));
	              table.addCell(loannumber);
	              loannumber.setPhrase(new Phrase(c.getCurrentLoanDetails().getCurrentLoanNumber()));
	              table.addCell(loannumber);
	              	
	              
	              PdfPCell customer_name=new PdfPCell();
	              customer_name.setPadding(5);
	              customer_name.setPaddingLeft(15);
	              
	              customer_name.setPhrase(new Phrase("Customer Name",font));
	                  table.addCell(customer_name);
	                  customer_name.setPhrase(new Phrase(c.getCustomerName()));
	                  table.addCell(customer_name);
	                  
	             
	                  PdfPCell gender=new PdfPCell();
	                  gender.setPadding(5);
	                  gender.setPaddingLeft(15);
	                  
	                  gender.setPhrase(new Phrase("Gender",font));
	                      table.addCell(gender);
	                      gender.setPhrase(new Phrase(c.getCustomerGender()));
	                      table.addCell(gender);
	          
	          
	                      PdfPCell email=new PdfPCell();
	                      email.setPadding(5);
	                      email.setPaddingLeft(15);
	                      
	                      email.setPhrase(new Phrase("Email",font));
	                          table.addCell(email);
	                          email.setPhrase(new Phrase(c.getCustomerEmail()));
	                          table.addCell(email);
	                      
	                          
	                          PdfPCell mobile_number=new PdfPCell();
	                          mobile_number.setPadding(5);
	                          mobile_number.setPaddingLeft(15);
	                          
	                          mobile_number.setPhrase(new Phrase("Mobile Number",font));
	                              table.addCell(mobile_number);
	                              mobile_number.setPhrase(new Phrase(""+(c.getCustomerMobileNumber())));
	                              table.addCell(mobile_number);
	                              
	                              PdfPCell cyststatus=new PdfPCell();
	                              cyststatus.setPadding(5);
	                              cyststatus.setPaddingLeft(15);
	                              
	                              cyststatus.setPhrase(new Phrase("Customer Status",font));
	                                  table.addCell(cyststatus);
	                                  cyststatus.setPhrase(new Phrase(c.getCustomerstatus()));
	                                  table.addCell(cyststatus);
	                          
	                                  PdfPCell custcity=new PdfPCell();
	                                  custcity.setPadding(5);
	                                  custcity.setPaddingLeft(15);
	                                  
	                                  custcity.setPhrase(new Phrase("Customer City",font));
	                                      table.addCell(custcity);
	                                      custcity.setPhrase(new Phrase(c.getCustomerAddress().getCustomerCityname()));
	                                      table.addCell(custcity);
	                                 
	                                      PdfPCell cust_district=new PdfPCell();
	                                      cust_district.setPadding(5);
	                                      cust_district.setPaddingLeft(15);
	                                      
	                                      cust_district.setPhrase(new Phrase("Customer District",font));
	                                          table.addCell(cust_district);
	                                          cust_district.setPhrase(new Phrase(c.getCustomerAddress().getCustomerDistrict()));
	                                          table.addCell(cust_district);
	                                          
	                                          PdfPCell cust_state=new PdfPCell();
	                                          cust_state.setPadding(5);
	                                          cust_state.setPaddingLeft(15);
	                                          
	                                          cust_state.setPhrase(new Phrase("Customer State",font));
	                                              table.addCell(cust_state);
	                                              cust_state.setPhrase(new Phrase(c.getCustomerAddress().getCustomerState()));
	                                              table.addCell(cust_state);
	                                              
	                 PdfPCell profession_type=new PdfPCell();
	                          profession_type.setPadding(5);
	                          profession_type.setPaddingLeft(15);
	                                              
	                          profession_type.setPhrase(new Phrase("Profession Type",font));
	                          table.addCell(profession_type);
	                          profession_type.setPhrase(new Phrase(c.getProfession().getProfessionType()));
	                          table.addCell(profession_type);
	                          
	                 PdfPCell profession_name=new PdfPCell();
	                          profession_name.setPadding(5);
	                          profession_name.setPaddingLeft(15);
	                                              
	                          profession_name.setPhrase(new Phrase("Profession Name",font));
	                          table.addCell(profession_name);
	                          profession_name.setPhrase(new Phrase(c.getProfession().getCompanyname()));
	                          table.addCell(profession_name); 
	                          
	                 PdfPCell cust_cibilscore=new PdfPCell();
	                 cust_cibilscore.setPadding(5);
	                 cust_cibilscore.setPaddingLeft(15);
	                                              
	                 cust_cibilscore.setPhrase(new Phrase("Customer Cibil Score",font));
	                          table.addCell(cust_cibilscore);
	                          cust_cibilscore.setPhrase(new Phrase(""+c.getCibilScore().getCibilScore()));
	                          table.addCell(cust_cibilscore); 
	                          
	                PdfPCell loan_amount=new PdfPCell();
	                loan_amount.setPadding(5);
	                loan_amount.setPaddingLeft(15);
	                                                       
	                loan_amount.setPhrase(new Phrase("Loan Amount",font));
	                                   table.addCell(loan_amount);
	                                   loan_amount.setPhrase(new Phrase(String.valueOf(c.getCustomerTotalLoanRequired())));
	                                   table.addCell(loan_amount); 
	                                                
	                PdfPCell roi=new PdfPCell();
	                         roi.setPadding(5);
	                         roi.setPaddingLeft(15);
	                                                                          
	                       roi.setPhrase(new Phrase("Rate Of Interest",font));
	                       table.addCell(roi);
	                       roi.setPhrase(new Phrase(c.getCurrentLoanDetails().getRateOfInterest()));
	                       table.addCell(roi);
	                       
	              PdfPCell emi=new PdfPCell();
	              emi.setPadding(5);
	              emi.setPaddingLeft(15);
	                                                                        
	              emi.setPhrase(new Phrase("EMI",font));
	                     table.addCell(emi);
	                     emi.setPhrase(new Phrase(String.valueOf(c.getCurrentLoanDetails().getEmiAmountMonthly())));
	                     table.addCell(emi);   
	                     
	      PdfPCell tenure=new PdfPCell();
	      tenure.setPadding(5);
	      tenure.setPaddingLeft(15);
	                                                                               
	      tenure.setPhrase(new Phrase("Tenure",font));
	             table.addCell(tenure);
	             tenure.setPhrase(new Phrase(c.getCurrentLoanDetails().getTenure()));
	             table.addCell(tenure);
	             
	             PdfPCell account_details=new PdfPCell();
	             account_details.setPadding(5);
	             account_details.setPaddingLeft(15);
	                                                                                      
	             account_details.setPhrase(new Phrase("Account Type",font));
	                    table.addCell(account_details);
	                    account_details.setPhrase(new Phrase(c.getAccountDetails().getAccounType()));
	                    table.addCell(account_details);
	             
	            PdfPCell account_number=new PdfPCell();
	            account_number.setPadding(5);
	            account_number.setPaddingLeft(15);
	                                                                                             
	            account_number.setPhrase(new Phrase("Account Number",font));
	                           table.addCell(account_number);
	                           account_number.setPhrase(new Phrase(c.getAccountDetails().getAccountHolderName()));
	                           table.addCell(account_number);
	                           
	           PdfPCell bank_name=new PdfPCell();
	           bank_name.setPadding(5);
	           bank_name.setPaddingLeft(15);
	                                                                                                            
	           bank_name.setPhrase(new Phrase("Bank Name",font));
	                     table.addCell(bank_name);
	                     bank_name.setPhrase(new Phrase(c.getAccountDetails().getBankname()));
	                     table.addCell(bank_name);                   
	                    
	          document.add(table);
	          
	          document.close();
	          
	          
	          
	          c.setApplicationpdf(out.toByteArray());
	          
	          
	          
	          
			
			
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
		else if(c2.getCustomerstatus().equals("VERIFICATION_STATE"))
		{
			c2.getCurrentLoanDetails().setStatus(Currentloanstatus.VERIFICATION_DONE.toString());
			c2.getCustomerverification().setVerificationDate(String.valueOf(new Date()));
			c2.getCustomerverification().setRemarks("Verified");
			c2.setCustomerstatus(Currentloanstatus.VERIFICATION_DONE.toString());
			c2.getCustomerverification().setStatus(Currentloanstatus.VERIFICATION_DONE.toString());
			rc.save(c2);
			return c2;
		}
		else if(c2.getCustomerstatus().equals("VERIFICATION_DONE"))
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
		else if(c2.getCustomerstatus().equals("SANCTIONED"))
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
		else if(c2.getCustomerstatus().equals("DISBURSED"))
		{
			

			c2.getLedger().setLedgerCreatedDate(String.valueOf(new Date()));
			c2.getLedger().setTotalLoanAmount(c2.getSanctionLetter().getLoanAmtSanctioned());
			c2.getLedger().setPayableAmountwithInterest(c2.getCurrentLoanDetails().getTotalAmountToBePaidDouble());
			c2.getLedger().setTenure(c2.getCurrentLoanDetails().getTenure());
			c2.getLedger().setMonthlyEMI(c2.getCurrentLoanDetails().getEmiDetails().getEmiAmountMonthly());
			c2.getLedger().setRemainingAmount(c2.getCurrentLoanDetails().getTotalAmountToBePaidDouble());
			
			
			int tenure = c2.getCurrentLoanDetails().getTenure();
			double emi = c2.getCurrentLoanDetails().getEmiAmountMonthly();
			c2.setCustomerstatus(Currentloanstatus.LEDGER.toString());
			 Date d=new Date();
			 
			 Set<InstallmentsDetails>install=new HashSet<>();
			
			for(int i=1;i<=tenure;i++)
			{
				InstallmentsDetails installment=new InstallmentsDetails();
				installment.setInstallmentsAmount(emi);
				
				
				 
				   Calendar cal = Calendar.getInstance();
			        cal.setTime(d);
			        cal.add(Calendar.DATE, 30);
			          Date d2 = cal.getTime();
			          if(i==1)
			          {
			        	  c2.getLedger().setNextEmiDatestart(String.valueOf(d2));
			        	  c2.getLedger().setNextEmiDateEnd(String.valueOf(d2));
			          }
			          else if (i==tenure) 
			          {
						c2.getLedger().setLoanEndDate(String.valueOf(d2));
					  }
			          {
			        	  
			          }
			          d=d2;
			          
				
				installment.setInstallmentsDate(d2.toString());
				

			install.add(installment);
				
			}
			c2.getLedger().setInstallmentsDetails(install);
			c2.getLedger().setLoanStatus(c2.getCustomerstatus());
			rc.save(c2);
			return c2;
	    }
		
		return null;
		
	}


	@Override
	public EnquiryDetails getsingleEnq(String pancardNumber) {

		EnquiryDetails ed=re.findByPancardNumber(pancardNumber);
		System.out.println(ed.getPancardNumber());
		
		return ed;
	}


	@Override
	public ByteArrayInputStream getpdf(int customerId) {
		
		Customer cust = rc.findByCustomerId(customerId);
		 byte[] applicationpdf = cust.getApplicationpdf();
		
		 

		return new ByteArrayInputStream(applicationpdf);
	}


	



	
	


	

	
	


}
