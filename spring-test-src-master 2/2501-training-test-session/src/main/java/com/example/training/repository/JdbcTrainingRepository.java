package com.example.training.repository;

import java.util.List;

import com.example.training.entity.Training;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTrainingRepository implements TrainingRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcTrainingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Training selectById(String id) {
        return jdbcTemplate.queryForObject("select * from training where id=?",
            new BeanPropertyRowMapper<>(Training.class),
            id);
    }

    @Override
    public List<Training> selectAll() {
        return jdbcTemplate.query("select * from training", new BeanPropertyRowMapper<>(Training.class));
    }

    @Override
    public boolean update(Training training) {
        int count = jdbcTemplate.update("update training set title=?, start_date_time=?, end_date_time=?, reserved=?, capacity=? where id=?",
            training.getTitle(),
            training.getStartDateTime(),
            training.getEndDateTime(),
            training.getReserved(),
            training.getCapacity(),
            training.getId());
        return count > 0;
    }

}
