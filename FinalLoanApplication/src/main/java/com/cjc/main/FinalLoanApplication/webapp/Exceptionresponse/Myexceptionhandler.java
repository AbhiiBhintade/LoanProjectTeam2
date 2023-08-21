package com.cjc.main.FinalLoanApplication.webapp.Exceptionresponse;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cjc.main.FinalLoanApplication.webapp.entity.BaseResponse;
import com.cjc.main.FinalLoanApplication.webapp.entity.EnquiryDetails;
import com.cjc.main.FinalLoanApplication.webapp.entity.Users;
import com.cjc.main.exceptions.EnquiryNotFound;
import com.cjc.main.exceptions.MailIdNotFound;
import com.cjc.main.exceptions.NullPointer;
import com.cjc.main.exceptions.UserNotFound;

@RestControllerAdvice
public class Myexceptionhandler {
	
	@ExceptionHandler(value = UserNotFound.class)
	public ResponseEntity<BaseResponse<Users>> handleUserNotFound()
	{
		return new ResponseEntity<BaseResponse<Users>>
		(new BaseResponse<Users>(404, "USER NOT FOUND", new Date(), null),HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(value = EnquiryNotFound.class)
	public ResponseEntity<BaseResponse<EnquiryDetails>> handleEnquiryNotFound(){
		
		return new ResponseEntity<BaseResponse<EnquiryDetails>>
		(new BaseResponse<EnquiryDetails>(404, "ENQUIRY NOT FOUND", new Date(), null),HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(value = MailIdNotFound.class)
	public ResponseEntity<BaseResponse<Users>> handleMailNotFound()
	{
		return new ResponseEntity<BaseResponse<Users>>
		(new BaseResponse<Users>(404, "EMAIL ID NOT FOUND", new Date(), null),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = NullPointer.class)
	public ResponseEntity<BaseResponse<Users>> handleNullPointer()
	{
		return new ResponseEntity<BaseResponse<Users>>
		(new BaseResponse<Users>(404, "NOT FOUND", new Date(), null),HttpStatus.NOT_FOUND);
	}

}
