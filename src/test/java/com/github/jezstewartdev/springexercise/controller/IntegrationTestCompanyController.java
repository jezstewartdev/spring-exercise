package com.github.jezstewartdev.springexercise.controller;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jezstewartdev.springexercise.scenario.Scenario;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@WireMockTest(httpPort = 8080)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationTestCompanyController {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	private ClassLoader classLoader = getClass().getClassLoader();

	@Test
	@Order(1) 
	void requestWithCompanyNumberShouldReturnOneCompanyAndListOfOfficers() throws Exception {

		performTest("test_with_company_number.json");
		
	}
	
	@Test
	@Order(2)
	void requestWithSameCompanyNumberShouldReturnSameResultFromDatabase() throws Exception {

		performTest("test_with_company_number_database.json");
		
	}
	
	@Test
	void requestWithCompanyNumberAndCompanyNameShouldReturnDetailsForCompanyNumber() throws Exception {

		performTest("test_with_company_number_and_company_name.json");
		
	}
	
	@Test
	void requestWithCompanyNameShouldReturnListOfCompanies() throws Exception {

		performTest("test_with_company_name.json");
		
	}
	
	@Test
	void requestWithNoCompanyNumberOrCompanyNameReturns422() throws Exception {

		performTest("test_with_no_company_number_or_company_name.json");
		
	}
	
	@Test
	void requestWithNoResponseFromApiReturnsNoCompaies() throws Exception {

		performTest("test_with_no_results.json");
		
	}
	
	@Test
	void requestWithAllActiveFlagSetOnlyReturnsActiveCompanies() throws Exception {

		performTest("test_with_company_name_only_active.json");
		
	}

	private void performTest(String testFileName) throws IOException, JsonProcessingException, JsonMappingException, Exception {
		
		Scenario scenario = createScenario(testFileName);

		createWireMocks(scenario);

		sendRequest(scenario);
		
	}

	private void sendRequest(Scenario scenario) throws Exception {
		mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get(scenario.getUrl())
				.contentType(MediaType.APPLICATION_JSON).content(scenario.getRequestJsonNode().toString()))
				.andExpect(status().is(scenario.getExpectedStatus())).andExpect(content().json(scenario.getExpectedResponseJsonNode().toString()));
	}

	private void createWireMocks(Scenario scenario) {
		
		scenario.getMocks().forEach(mock -> {
			stubFor(get(mock.getUrl()).withHost(WireMock.equalTo("localhost"))
					.willReturn(aResponse().withBody(mock.getResponse().toString())
							.withHeader("Content-Type", "application/json").withStatus(200)));
		});
	}

	private Scenario createScenario(String testFileName)
			throws IOException, JsonProcessingException, JsonMappingException {
		
		Scenario scenario = objectMapper.readValue(createScenarioJsonStringFromFile("scenarios/" + testFileName), Scenario.class);
		return scenario;
		
	}

	private String createScenarioJsonStringFromFile(String testFileName) throws IOException {
		URL url = classLoader.getResource(testFileName);
		String str = Files.readString(Path.of(url.getPath()));
		return str;
	}

}
