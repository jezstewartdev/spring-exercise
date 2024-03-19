package com.github.jezstewartdev.springexercise.dto;

import java.util.List;

import lombok.Data;

@Data
public class CompanyLookupResponse {
	int total_results;
	List<CompanyDto> items;
}

