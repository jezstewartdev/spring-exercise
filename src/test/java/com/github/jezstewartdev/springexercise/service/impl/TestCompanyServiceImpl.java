package com.github.jezstewartdev.springexercise.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jezstewartdev.springexercise.dto.CompanyLookupRequest;
import com.github.jezstewartdev.springexercise.dto.CompanyLookupResponse;
import com.github.jezstewartdev.springexercise.dto.OfficersResponse;
import com.github.jezstewartdev.springexercise.entity.Company;
import com.github.jezstewartdev.springexercise.repository.CompanyRepository;
import com.github.jezstewartdev.springexercise.scenario.Scenario;

@ExtendWith(MockitoExtension.class)
class TestCompanyServiceImpl {

	@Value("${api.host}")
	private String host;

	@Value("${api.company.search.url}")
	private String companySearchUrl;

	@Value("${api.company.officers.url}")
	private String companyOfficersUrl;

	private ClassLoader classLoader = getClass().getClassLoader();
	private ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	CompanyRepository companyRepository;

	@Mock
	RestTemplate restTemplate;

	@Mock
	ResponseEntity<CompanyLookupResponse> companyLookupResponseEntity;

	@Mock
	ResponseEntity<OfficersResponse> officersResponseEntity;

	@InjectMocks
	CompanyServiceImpl companyService;

	@Test
	void getCompaniesByCompanyNumberReturnsCompanyLookupResponseFromEntity() throws Exception {

		Scenario scenario = createScenario("test_with_company_number_database.json");

		when(companyRepository.findById(anyString())).thenReturn(Optional.of(createEntityFromJsonFile()));

		assertEquals(scenario.getExpectedResponse(), companyService.getCompanies(scenario.getRequest()));

	}

	@Test
	void getCompaniesByCompanyNumberReturnsCompanyLookupResponseFromApi() throws Exception {

		testUsingApiMock("test_with_company_number.json");

	}

	@Test
	void getCompaniesByCompanyNumberAndCompanyNameReturnsCompanyLookupResponseFromApi() throws Exception {

		testUsingApiMock("test_with_company_number_and_company_name.json");

	}

	@Test
	void getCompaniesByCompanyNameReturnsCompanyLookupResponseFromApi() throws Exception {

		testUsingApiMock("test_with_company_name.json");

	}

	private void testUsingApiMock(String testFile) throws IOException, JsonProcessingException, JsonMappingException {

		Scenario scenario = createScenario(testFile);

		setMocksForApi(scenario);

		assertEquals(scenario.getExpectedResponse(), companyService.getCompanies(scenario.getRequest()));
	}

	private void setMocksForApi(Scenario scenario) {
		
		when(restTemplate.exchange(anyString(), any(), any(),
				eq(CompanyLookupResponse.class), anyString())).thenReturn(companyLookupResponseEntity);
		when(companyLookupResponseEntity.getBody()).thenReturn(scenario.getExpectedResponse());
		when(restTemplate.exchange(anyString(), any(), any(),
				eq(OfficersResponse.class), anyString())).thenReturn(officersResponseEntity);	
		List<OfficersResponse> officersResponses = scenario.getExpectedResponse().getItems().stream().map(company -> new OfficersResponse(company.getOfficers())).collect(Collectors.toList());
		when(officersResponseEntity.getBody()).thenAnswer(AdditionalAnswers.returnsElementsOf(officersResponses));

	}

	private Scenario createScenario(String testFileName)
			throws IOException, JsonProcessingException, JsonMappingException {

		Scenario scenario = objectMapper.readValue(createScenarioJsonStringFromFile("scenarios/" + testFileName),
				Scenario.class);
		scenario.setRequest(
				objectMapper.readValue(scenario.getRequestJsonNode().toString(), CompanyLookupRequest.class));
		scenario.setExpectedResponse(
				objectMapper.readValue(scenario.getExpectedResponseJsonNode().toString(), CompanyLookupResponse.class));
		return scenario;

	}

	private String createScenarioJsonStringFromFile(String testFileName) throws IOException {
		URL url = classLoader.getResource(testFileName);
		String str = Files.readString(Path.of(url.getPath()));
		return str;
	}

	private Company createEntityFromJsonFile() throws IOException, JsonProcessingException, JsonMappingException {
		URL url = classLoader.getResource("entity/company.json");
		String str = Files.readString(Path.of(url.getPath()));
		return objectMapper.readValue(str, Company.class);
	}
}