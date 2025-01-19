package com.example.training.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.training.entity.Training;
import com.example.training.service.TrainingService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(TrainingController.class)
public class TrainingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingService trainingService;

    @Test
    public void test_displayDetails() throws Exception {

        Training training = new Training();
        training.setTitle("Java研修");
        doReturn(training).when(trainingService).findById("t02");

        mockMvc.perform(get("/training/display-details").param("trainingId", "t02"))
            .andExpect(content().string(containsString("Java研修")))
        ;
    }

    @Test
    public void test_displayList() throws Exception {
        List<Training> trainings = new ArrayList<>();
        Training training = new Training();
        training.setTitle("Java研修");
        trainings.add(training);
        training = new Training();
        training.setTitle("ビジネスマナー研修");
        trainings.add(training);
        doReturn(trainings).when(trainingService).findAll();

        mockMvc.perform(get("/training/display-list"))
            .andExpect(content().string(containsString("Java研修")))
            .andExpect(content().string(containsString("ビジネスマナー研修")))
        ;

    }

}