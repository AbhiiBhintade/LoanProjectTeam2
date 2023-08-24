package com.cjc.main.FinalLoanApplication.webapp.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.cjc.main.FinalLoanApplication.webapp.entity.BaseResponse;
import com.cjc.main.FinalLoanApplication.webapp.entity.Cibil;
import com.cjc.main.FinalLoanApplication.webapp.entity.EnquiryDetails;
import com.cjc.main.FinalLoanApplication.webapp.entity.MailDetails;
import com.cjc.main.FinalLoanApplication.webapp.entity.Users;
import com.cjc.main.FinalLoanApplication.webapp.loanservice.LoanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Delegate;


@RestController
@CrossOrigin("*")
public class LoanController {

	
	
	@Autowired
	LoanService ls;
	
	@Autowired
	RestTemplate rt;
	
	//for adding user through admin
	
	@PostMapping("/adduser")
	public ResponseEntity<BaseResponse<Users>> adduser(@RequestPart("data") String usersjson,
			                               @RequestPart("profile") MultipartFile profilephoto) throws JsonMappingException, JsonProcessingException
	{
		ObjectMapper om=new ObjectMapper();
		Users u = om.readValue(usersjson, Users.class);
		Users ud=ls.adduser(u,profilephoto);
		
		return new ResponseEntity<BaseResponse<Users>>(new BaseResponse<Users>(200, "USER CREATED", new Date(), ud),HttpStatus.CREATED);
	}
	
	
	
      @GetMapping("/getuserdata/{userName}/{passWord}")
      public ResponseEntity<BaseResponse<Users>>getuserdata(@PathVariable("userName") String userName,
    		                                                @PathVariable ("passWord") String passWord)
      {
    	  
    	  
    	 Users ud= ls.getuserdata(userName,passWord);
    	
    	  return new ResponseEntity<BaseResponse<Users>>(new BaseResponse<Users>(200, "USER FOUND",
    			                                         new Date(), ud),HttpStatus.OK);
      }
     
      
//      FOR ENQUIRY POST
      
      @PostMapping("/addenquiry")
      public ResponseEntity<BaseResponse<EnquiryDetails>>addenquiry(@RequestBody EnquiryDetails e)
      {
    	  
    	  EnquiryDetails ed =ls.addenquiry(e);
    	  
    	  return new ResponseEntity<BaseResponse<EnquiryDetails>>(new BaseResponse<EnquiryDetails>(200, "ENQUIRY ADDED"
    			  									, new Date(), ed),HttpStatus.CREATED);
      }
      
     @GetMapping("/getenq/{enquirystatus1}/{enquirystatus2}") 
     public ResponseEntity<BaseResponse<Iterable<EnquiryDetails>>>getenq(
    		 										@PathVariable String enquirystatus1,
    		 										@PathVariable String enquirystatus2)
     {
    	 
    	Iterable<EnquiryDetails> it=ls.getenquiry(enquirystatus1,enquirystatus2);
    	 
    	 return new ResponseEntity<BaseResponse<Iterable<EnquiryDetails>>>(new BaseResponse<Iterable<EnquiryDetails>>(200, "ENQUIRY FOUND"
					, new Date(), it),HttpStatus.OK);
     }
 
     @GetMapping("/getallusers") 
     public ResponseEntity<BaseResponse<List<Users>>>getallusers()
    		 										
     {
    	 List<Users> users=ls.getallusers();

    	 
    	 return new ResponseEntity<BaseResponse<List<Users>>>(new BaseResponse<List<Users>>(200, "USERS FOUND"
					, new Date(), users),HttpStatus.OK);
     }
    
     @DeleteMapping("/deletuser/{userId}")
     public ResponseEntity<BaseResponse<Users>>deleteuser(@PathVariable int userId)
     {
    	 ls.deleteusers(userId);
    	 
    	 return new ResponseEntity<BaseResponse<Users>>(new BaseResponse<Users>(200, "USER DELETED",
    			                                        new Date(), null),HttpStatus.OK);		
     }
     
     @PutMapping("/updatestatus/{eid}")
     public ResponseEntity<BaseResponse<EnquiryDetails>>updatestatus(@PathVariable int eid)
     {
    	 System.out.println(eid);
    	 EnquiryDetails e=ls.updatestatus(eid);
    	 
    	 return new ResponseEntity<BaseResponse<EnquiryDetails>>(new BaseResponse<EnquiryDetails>(200, "ENQUIRY SEND OE",
    			                 new Date(), e),HttpStatus.OK);		
     }
     
     @GetMapping("/getsingleuser/{userType}")
     public ResponseEntity<BaseResponse<Users>>getsingle(@PathVariable String userType)
     {


    	 	Users ud= ls.getsingleuser(userType);

    	 	return new ResponseEntity<BaseResponse<Users>>(new BaseResponse<Users>(200, "USER FOUND",
    	 			new Date(), ud),HttpStatus.OK);
	}

     //get produce data of CIBIL;
     
    @GetMapping("/getcibil/{pancardNumber}")
    public ResponseEntity<BaseResponse<Integer>>getcibil(@PathVariable String pancardNumber
    													)
    {
    	String url="http://localhost:9092/getpancard/"+pancardNumber;
    	int cibil = rt.getForObject(url,Integer.class);
    	
    	
    	return new ResponseEntity<BaseResponse<Integer>>(new BaseResponse<Integer>(200, "CIBIL FOUND",
    										new Date(),cibil),HttpStatus.OK);
    }
    
	@PostMapping("/sendmailwithattachment")
	public ResponseEntity<BaseResponse<MailDetails>>sendmailwithattachment(
							@RequestPart("attach") MultipartFile attachment,
							@RequestPart("mail") String maildetailsJson) throws JsonMappingException, JsonProcessingException
	{
		ObjectMapper om=new ObjectMapper();
		MailDetails mailDetails = om.readValue(maildetailsJson, MailDetails.class);
		
		String m = ls.sendmailwithattachment(mailDetails,attachment);
		
		
		return new ResponseEntity<BaseResponse<MailDetails>>(new BaseResponse<MailDetails>(201
				, "MAIL SEND SUCCESSFULLY", new Date(), null), HttpStatus.OK);
	}
     
}
