package com.github.jezstewartdev.springexercise.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.github.jezstewartdev.springexercise.dto.CompanyDto;
import com.github.jezstewartdev.springexercise.dto.CompanyLookupRequest;
import com.github.jezstewartdev.springexercise.dto.CompanyLookupResponse;
import com.github.jezstewartdev.springexercise.dto.Officer;
import com.github.jezstewartdev.springexercise.dto.OfficersResponse;
import com.github.jezstewartdev.springexercise.entity.Company;
import com.github.jezstewartdev.springexercise.repository.CompanyRepository;
import com.github.jezstewartdev.springexercise.service.CompanyService;

@Component
public class CompanyServiceImpl implements CompanyService {

	@Value("${api.host}")
	private String host;

	@Value("${api.company.search.url}")
	private String companySearchUrl;

	@Value("${api.company.officers.url}")
	private String companyOfficersUrl;
	
	private ModelMapper modelMapper = new ModelMapper();

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private HttpEntity<String> httpEntity;

	@Override
	public CompanyLookupResponse getCompanies(CompanyLookupRequest request) {

		String searchTerm = StringUtils.hasLength(request.getCompanyNumber()) ? request.getCompanyNumber()
				: request.getCompanyName();

		if (StringUtils.hasLength(request.getCompanyNumber())) {
			Optional<Company> company = companyRepository.findById(request.getCompanyNumber());
			if (company.isPresent()) {
				return createCompanyLookupResponseFromEntity(company.get());
			}
		}

		CompanyLookupResponse response = getCompanyLookupResponseFromApi(searchTerm);

		if (response.getItems() != null) {
			for (CompanyDto companyDto : response.getItems()) {
				companyDto.setOfficers(getOfficersFromApi(companyDto.getCompany_number()));
				companyRepository.save(createCompanyEntity(companyDto));
			}
		}

		return response;
	}

	private CompanyLookupResponse createCompanyLookupResponseFromEntity(Company companyEntity) {
		CompanyLookupResponse response = new CompanyLookupResponse();
		response.setItems(new ArrayList<>(List.of(modelMapper.map(companyEntity, CompanyDto.class))));
		response.setTotal_results(response.getItems().size());
		return response;
	}

	private Company createCompanyEntity(CompanyDto companyDto) {
		Company companyEntity = modelMapper.map(companyDto, Company.class);
		companyEntity.getOfficers().forEach(officer -> officer.setCompany(createForeignKey(companyDto)));
		return companyEntity;
	}

	private Company createForeignKey(CompanyDto companyDto) {
		Company company = new Company();
		company.setCompany_number(companyDto.getCompany_number());
		return company;
	}

	private CompanyLookupResponse getCompanyLookupResponseFromApi(String searchTerm) {
		return restTemplate.exchange(String.format("%s%s", host, companySearchUrl), HttpMethod.GET, httpEntity,
				CompanyLookupResponse.class, searchTerm).getBody();
	}

	private List<Officer> getOfficersFromApi(String companyNumber) {
		ResponseEntity<OfficersResponse> response = restTemplate.exchange(
				String.format("%s%s", host, companyOfficersUrl), HttpMethod.GET, httpEntity, OfficersResponse.class,
				companyNumber);
		List<Officer> officers = new ArrayList<>();
		if (response.getBody().getItems() != null) {
			officers = response.getBody().getItems();
			for (Iterator<Officer> it = officers.iterator(); it.hasNext();)
				if (it.next().getResigned_on() != null)
					it.remove();
		}
		return officers;
	}

}
