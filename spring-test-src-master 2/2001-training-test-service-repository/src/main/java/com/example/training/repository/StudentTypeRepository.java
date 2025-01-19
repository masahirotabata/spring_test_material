package com.example.training.repository;

import com.example.training.entity.StudentType;

import java.util.List;

public interface StudentTypeRepository {
    List<StudentType> selectAll();

    StudentType selectByCode(String studentTypeCode);
}
