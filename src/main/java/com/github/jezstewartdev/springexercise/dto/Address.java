package com.github.jezstewartdev.springexercise.dto;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Address {
	
    private String locality;
    private String postal_code;
    private String premises;
    private String address_line_1;
    private String country;

}
