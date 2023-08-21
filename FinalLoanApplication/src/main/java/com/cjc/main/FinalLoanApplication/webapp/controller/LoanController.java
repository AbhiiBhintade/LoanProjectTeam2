package com.cjc.main.FinalLoanApplication.webapp.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cjc.main.FinalLoanApplication.webapp.entity.BaseResponse;
import com.cjc.main.FinalLoanApplication.webapp.entity.EnquiryDetails;
import com.cjc.main.FinalLoanApplication.webapp.entity.Users;
import com.cjc.main.FinalLoanApplication.webapp.loanservice.LoanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin("*")
public class LoanController {

	
	
	@Autowired
	LoanService ls;
	
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
    	 System.out.println(ud.getUserType());
    	  return new ResponseEntity<BaseResponse<Users>>(new BaseResponse<Users>(200, "USER FOUND",
    			                                         new Date(), ud),HttpStatus.FOUND);
      }
     
      
//      FOR ENQUIRY POST
      
      @PostMapping("/addenquiry")
      public ResponseEntity<BaseResponse<EnquiryDetails>>addenquiry(@RequestBody EnquiryDetails e)
      {
    	  
    	  EnquiryDetails ed =ls.addenquiry(e);
    	  
    	  return new ResponseEntity<BaseResponse<EnquiryDetails>>(new BaseResponse<EnquiryDetails>(200, "ENQUIRY ADDED"
    			  									, new Date(), ed),HttpStatus.CREATED);
      }
	
}
