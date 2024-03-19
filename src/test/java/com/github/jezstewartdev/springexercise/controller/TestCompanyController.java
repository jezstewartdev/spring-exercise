package com.github.jezstewartdev.springexercise.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jezstewartdev.springexercise.dto.CompanyDto;
import com.github.jezstewartdev.springexercise.dto.CompanyLookupRequest;
import com.github.jezstewartdev.springexercise.dto.CompanyLookupResponse;
import com.github.jezstewartdev.springexercise.service.CompanyService;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ExceptionHandlingController.class)
class TestCompanyController
{
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private CompanyController controller;

    @Mock
    private CompanyService service;

    @Mock
    private CompanyDto company;
    
    @Mock
    private CompanyLookupResponse response;

    @BeforeEach
    public void setup()
    {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new ExceptionHandlingController())
                .build();
    }

    @Test
    void getCompaniesUsingCompanyNumberShouldReturnCompanyResponse() throws Exception
    {
    	successfulCall(new CompanyLookupRequest("12345678",null));
    }
    
    @Test
    void getCompaniesUsingCompanyNameShouldReturnCompanyResponse() throws Exception
    {
    	successfulCall(new CompanyLookupRequest(null,"amazon.com"));
    }
    
    @Test
    void getCompaniesWithNoCompanyNumberOrCompanyNameShouldReturn422Status() throws Exception
    {
        Map<String, String> response = Map.of("error","Either companyNumber or companyName must be populated");
        			
        mockMvc.perform(get("/companies")
					.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new CompanyLookupRequest())))
			        .andExpect(status().isUnprocessableEntity())
			        .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
    
	private void successfulCall(CompanyLookupRequest request) throws Exception, JsonProcessingException {
		
		when(service.getCompanies(any(CompanyLookupRequest.class))).thenReturn(response);
    
        mockMvc.perform(get("/companies")
        		.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

}
