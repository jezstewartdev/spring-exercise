package com.github.jezstewartdev.springexercise.scenario;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class Scenario{
	public String url;
	public int expectedStatus;
	public JsonNode request;
    public JsonNode expectedResponse;
    public ArrayList<Mock> mocks;
}

