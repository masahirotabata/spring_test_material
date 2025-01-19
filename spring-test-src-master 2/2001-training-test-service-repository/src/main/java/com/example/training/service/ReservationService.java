package com.example.training.service;

import com.example.training.entity.Reservation;
import com.example.training.entity.StudentType;
import com.example.training.input.ReservationInput;

import java.util.List;

public interface ReservationService {
    List<StudentType> findAllStudentType();

    StudentType findStudentTypeByCode(String studentTypeCode);

    Reservation reserve(ReservationInput form);
}
