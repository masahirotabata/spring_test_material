package com.example.training.repository;

import com.example.training.entity.Training;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest
@Import(JdbcTrainingRepository.class)
@Sql("JdbcTrainingRepositoryTest.sql")
class JdbcTrainingRepositoryTest {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    TrainingRepository trainingRepository;

    @Test
    void test_selectById() {
        Training training = trainingRepository.selectById("t01");
        assertThat(training.getTitle()).isEqualTo("ビジネスマナー研修");
    }

    @Test
    @Sql("JdbcTrainingRepositoryTest_2.sql")
    void test_selectAll() {
        List<Training> trainings = trainingRepository.selectAll();
        assertThat(trainings.size()).isEqualTo(2);
    }

    @Test
    void test_update() {
        Training training = new Training();
        training.setId("t01");
        training.setTitle("SQL入門");
        boolean result = trainingRepository.update(training);
        assertThat(result).isEqualTo(true);
        Map<String, Object> trainingMap = jdbcTemplate.queryForMap(
            "SELECT * FROM training WHERE id=?", "t01");
        assertThat(trainingMap.get("title")).isEqualTo("SQL入門");
    }
}

