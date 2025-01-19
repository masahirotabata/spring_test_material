package com.example.training.service;

import com.example.training.entity.Training;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TrainingServiceTest {
    @Autowired
    TrainingService trainingService;

    @Test
    void test_findById() {
        Training training = trainingService.findById("t01");
        assertThat(training.getTitle()).isEqualTo("ビジネスマナー研修");
    }
}
