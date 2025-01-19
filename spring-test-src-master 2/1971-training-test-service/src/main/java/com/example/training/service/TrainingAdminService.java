package com.example.training.service;

import com.example.training.entity.Training;
import com.example.training.input.TrainingAdminInput;

import java.util.List;

public interface TrainingAdminService {
    List<Training> findAll();

    Training findById(String trainingId);

    void update(TrainingAdminInput trainingAdminInput);

    void delete(String id);

    Training register(TrainingAdminInput trainingAdminInput);
}
