package com.github.jezstewartdev.springexercise.service;

import org.springframework.stereotype.Component;

import com.github.jezstewartdev.springexercise.dto.CompanyLookupRequest;
import com.github.jezstewartdev.springexercise.dto.CompanyLookupResponse;

@Component
public interface CompanyService {

	public CompanyLookupResponse getCompanies(CompanyLookupRequest request);

	public CompanyLookupResponse getActiveCompanies(CompanyLookupRequest request);

}


