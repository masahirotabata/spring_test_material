package com.example.training.repository;

import com.example.training.entity.StudentType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcStudentTypeRepository implements StudentTypeRepository {
    private JdbcTemplate jdbcTemplate;

    public JdbcStudentTypeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<StudentType> selectAll() {
        return jdbcTemplate.query("select * from student_type", new BeanPropertyRowMapper<>(StudentType.class));
    }

    @Override
    public StudentType selectByCode(String studentTypeCode) {
        return jdbcTemplate.queryForObject("select * from student_type where code=?", new BeanPropertyRowMapper<>(StudentType.class), studentTypeCode);
    }
}
