package com.cjc.main.FinalLoanApplication.webapp.loanRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cjc.main.FinalLoanApplication.webapp.entity.EnquiryDetails;

@Repository
public interface RepoForEnquiry extends JpaRepository<EnquiryDetails, Integer> {

}
