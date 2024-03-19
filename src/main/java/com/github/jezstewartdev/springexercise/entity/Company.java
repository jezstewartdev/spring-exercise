package com.github.jezstewartdev.springexercise.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "companies")
@Data
public class Company {

	@Id
	private String company_number;
	private String company_type;
	private String title;
	private String company_status;
	private String date_of_creation;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "id")
	private Address address;
	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
	private List<Officer> officers;

}
