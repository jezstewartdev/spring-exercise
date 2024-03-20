package com.github.jezstewartdev.springexercise.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OfficersResponse {
	List<Officer> items;
}

