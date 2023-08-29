package com.cjc.main.FinalLoanApplication.webapp.loanRepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cjc.main.FinalLoanApplication.webapp.entity.Customer;

@Repository
public interface RepoForCustomer extends JpaRepository<Customer, Integer> {
	
	List<Customer> findAllByCustomerstatusOrCustomerstatus(String status1,String status2);
	
	Customer findByCustomerId(int customerId);

}
