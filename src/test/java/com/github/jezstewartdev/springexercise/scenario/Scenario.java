package com.github.jezstewartdev.springexercise.scenario;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.jezstewartdev.springexercise.dto.CompanyLookupRequest;
import com.github.jezstewartdev.springexercise.dto.CompanyLookupResponse;

import lombok.Data;

@Data
public class Scenario{
	public String url;
	public int expectedStatus;
	public JsonNode requestJsonNode;
    public JsonNode expectedResponseJsonNode;
    public ArrayList<Mock> mocks;
    public CompanyLookupRequest request;
    public CompanyLookupResponse expectedResponse;
}

