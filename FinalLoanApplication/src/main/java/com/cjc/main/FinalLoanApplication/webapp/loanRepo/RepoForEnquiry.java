package com.cjc.main.FinalLoanApplication.webapp.loanRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cjc.main.FinalLoanApplication.webapp.entity.EnquiryDetails;


public interface RepoForEnquiry extends JpaRepository<EnquiryDetails, Integer> {

	Iterable<EnquiryDetails> findAllByEnquiryStatusOrEnquiryStatus(String enquirystatus1, String enquirystatus2);
	EnquiryDetails findByEid(int eid);

}
