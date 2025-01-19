package com.example.training.service;

import com.example.training.entity.Training;
import com.example.training.input.TrainingAdminInput;
import com.example.training.repository.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingAdminServiceImplTest {

    @InjectMocks
    TrainingAdminServiceImpl trainingAdminService;

    @Mock
    TrainingRepository trainingRepository;

    @Test
    void test_findById() {
        Training training = new Training();
        training.setTitle("ビジネスマナー研修");
        doReturn(training).when(trainingRepository).selectById("t01");

        Training actual = trainingAdminService.findById("t01");
        assertThat(actual.getTitle()).isEqualTo("ビジネスマナー研修");
    }

    @Test
    void test_register() {
        TrainingAdminInput trainingAdminInput = new TrainingAdminInput();
        trainingAdminInput.setTitle("SQL入門");
        trainingAdminInput.setReserved(0);
        trainingAdminInput.setCapacity(8);

        trainingAdminService.register(trainingAdminInput);

        ArgumentCaptor<Training> trainingCaptor = ArgumentCaptor.forClass(Training.class);
        verify(trainingRepository).insert(trainingCaptor.capture());
        Training training = trainingCaptor.getValue();
        assertThat(training.getTitle()).isEqualTo("SQL入門");
        assertThat(training.getReserved()).isEqualTo(0);
        assertThat(training.getCapacity()).isEqualTo(8);
    }

}
