package com.github.jezstewartdev.springexercise.scenario;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class Mock{
    public String url;
    public JsonNode response;
}

