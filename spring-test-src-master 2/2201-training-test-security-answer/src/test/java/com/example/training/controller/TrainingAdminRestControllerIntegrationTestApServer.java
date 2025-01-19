package com.example.training.controller;

import com.example.training.entity.Training;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("TrainingAdminRestControllerIntegrationTest.sql")
@Sql(value = "clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class TrainingAdminRestControllerIntegrationTestApServer {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void test_getTraining() {
        ResponseEntity<Training> responseEntity = testRestTemplate
                .withBasicAuth("taro", "taro123")
                .getForEntity("/api/trainings/{id}", Training.class, "t01");
        Training training = responseEntity.getBody();
        assertThat(training.getTitle()).isEqualTo("ビジネスマナー研修");
    }
}
