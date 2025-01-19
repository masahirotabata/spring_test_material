package com.example.training.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("TrainingAdminRestControllerIntegrationTest.sql")
@Transactional
class TrainingAdminRestControllerIntegrationTestMockMvc {
    @Autowired
    MockMvc mockMvc;

    @Test
    void test_getTrainings() throws Exception {
        mockMvc.perform(
                get("/api/trainings")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].title").value("ビジネスマナー研修"))
            .andExpect(jsonPath("$[1].title").value("Java実践"))
        ;
    }

    @Test
    void test_registerTraining() throws Exception {

        String requestBody = """
                {
                  "title": "SQL入門",
                  "startDateTime": "2021-12-01T09:30:00",
                  "endDateTime": "2021-12-03T17:00:00",
                  "reserved": 0,
                  "capacity": 8
                }""";

        mockMvc.perform(
                post("/api/trainings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isCreated())
            .andExpect(header().string(
                "Location", matchesPattern("http://localhost/api/trainings/.*")))
        ;
    }


}