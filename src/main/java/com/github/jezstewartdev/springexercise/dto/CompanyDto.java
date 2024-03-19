package com.github.jezstewartdev.springexercise.dto;

import java.util.List;

import lombok.Data;

@Data
public class CompanyDto {
	
	private String company_number;
	private String company_type;
	private String title;
	private String company_status;
	private String date_of_creation;
    private Address address;
    private List<Officer> officers;

}

