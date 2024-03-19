package com.github.jezstewartdev.springexercise.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="officers")
public class Officer {
	
	@Id
	@GeneratedValue
	private int id;
    private String name;
    private String officer_role;
    private String appointed_on;
    @OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
    @ManyToOne 
    @JoinColumn(name = "company_number",referencedColumnName="company_number") 
    private Company company;

}