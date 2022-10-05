package com.krati.score_keeper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krati.score_keeper.Constants;
import com.krati.score_keeper.model.PlayerScore;

import java.util.HashMap;


import org.junit.jupiter.api.Test;


@WebMvcTest(ScoreController.class)
public class ScoreControllerTests {
	
	ScoreController scoreController;
	
	public ScoreControllerTests() {
		super();
		this.scoreController = new ScoreController();
	}
	
	@Autowired
	 private MockMvc mockMvc;
	
	@Autowired
	  private ObjectMapper objectMapper;

	@Test
	public void testAppend() throws Exception {
		HashMap<String,String>params = new HashMap<>();
		params.put(Constants.playerName,"Virat Kohli");
		params.put(Constants.score,"150");
		mockMvc.perform(post("/score/append").contentType(MediaType.ALL)
				.content(objectMapper.writeValueAsString(new PlayerScore("Hardik Pandya", 100))))
		.andExpect(status().isOk())
		.andDo(print());
		
		mockMvc.perform(post("/score/append").contentType(MediaType.ALL)
				.content(objectMapper.writeValueAsString(new PlayerScore("Virat Kohli", 200))))
		.andExpect(status().isOk())
		.andDo(print());
		
		mockMvc.perform(post("/score/append").contentType(MediaType.ALL)
				.content(objectMapper.writeValueAsString(new PlayerScore("Rohit Sharma", 120))))
		.andExpect(status().isOk())
		.andDo(print());
		
		mockMvc.perform(post("/score/append").contentType(MediaType.ALL)
				.content(objectMapper.writeValueAsString(new PlayerScore("Yuzvendra Chahal", 80))))
		.andExpect(status().isOk())
		.andDo(print());
		
		mockMvc.perform(post("/score/append").contentType(MediaType.ALL)
				.content(objectMapper.writeValueAsString(new PlayerScore("MS Dhoni", 110))))
		.andExpect(status().isOk())
		.andDo(print());
		
	}
	
	@Test
	public void testGetTop5Scores() throws Exception {
		String top5Scores = scoreController.getTop5Scores().getBody().toString();
		System.out.println(top5Scores + "...");
		mockMvc.perform(get("/score/getTop5Scores"))
		.andExpect(status().isOk())
		.andDo(print());
	}

}
