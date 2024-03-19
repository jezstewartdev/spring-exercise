package com.github.jezstewartdev.springexercise.pojo;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class TestParameters{
	public String url;
	public int expectedStatus;
	public JsonNode request;
    public JsonNode expectedResponse;
    public ArrayList<Mock> mocks;
}

