package com.github.jezstewartdev.springexercise.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Officer {
	
    private String name;
    private String officer_role;
    private String appointed_on;
    private Address address;
    private String resigned_on;
    @JsonIgnore
    private CompanyDto company;

}