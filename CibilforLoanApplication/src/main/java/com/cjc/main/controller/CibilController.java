package com.cjc.main.controller;

import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CibilController {
	
	Random r=new Random();
	
	@RequestMapping("/getpancard/{pancard}")
	public int getcibil(@PathVariable String pancard)
	{
		 System.out.println(pancard);
		if(pancard.length()==12)
				{
					 int cibil = r.nextInt(600,900);

					 return cibil;
			
				}
		
		return 0;
	}
	

}
