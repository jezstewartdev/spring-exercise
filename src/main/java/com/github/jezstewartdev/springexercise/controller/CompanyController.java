package com.github.jezstewartdev.springexercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.jezstewartdev.springexercise.dto.CompanyLookupRequest;
import com.github.jezstewartdev.springexercise.dto.CompanyLookupResponse;
import com.github.jezstewartdev.springexercise.exception.ValidationException;
import com.github.jezstewartdev.springexercise.service.CompanyService;

@RestController
@RequestMapping("/companies")
public class CompanyController {
	
	@Autowired
	CompanyService service;
	
	@GetMapping()
	public CompanyLookupResponse getCompanies(@RequestBody CompanyLookupRequest request) {
	    if (!StringUtils.hasLength(request.getCompanyNumber()) && !StringUtils.hasLength(request.getCompanyName())) {
	    	throw new ValidationException("Either companyNumber or companyName must be populated");
	    }
		return service.getCompanies(request);
	}

}
