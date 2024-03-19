package com.github.jezstewartdev.springexercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.jezstewartdev.springexercise.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, String>

{
}
