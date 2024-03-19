package com.github.jezstewartdev.springexercise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompanyLookupRequest {
	String companyNumber;
	String companyName;
}
