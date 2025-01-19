package com.example.training.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("TrainingControllerIntegrationTest.sql")
public class TrainingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_displayDetails() throws Exception {
        mockMvc.perform(
                get("/training/display-details")
                    .param("trainingId", "t02")
            )
            .andExpect(content().string(containsString("Java実践")))
        ;
    }

    @Test
    public void test_displayList() throws Exception {
        mockMvc.perform(
                get("/training/display-list")
            )
            .andExpect(content().string(containsString("ビジネスマナー研修")))
            .andExpect(content().string(containsString("Java実践")))
            .andExpect(content().string(containsString("マーケティング研修")))
        ;
    }
}