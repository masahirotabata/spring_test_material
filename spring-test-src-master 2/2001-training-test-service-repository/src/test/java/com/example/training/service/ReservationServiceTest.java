package com.example.training.service;

import com.example.training.entity.Reservation;
import com.example.training.entity.StudentType;
import com.example.training.exception.CapacityOverException;
import com.example.training.input.ReservationInput;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("ReservationServiceTest.sql")
@Transactional
public class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test_findStudentTypeByCode() {
        StudentType studentTYpe = reservationService.findStudentTypeByCode("FREELANCE");
        assertThat(studentTYpe.getName()).isEqualTo("フリーランス");
    }

    @Test
    public void test_findAllStudentType() {
        List<StudentType> studentTypes = reservationService.findAllStudentType();
        assertThat(studentTypes.size()).isEqualTo(3);
    }


    @Test
    public void test_reserve() {

        ReservationInput input = new ReservationInput();
        input.setName("東京太郎");
        input.setPhone("090-0000-0000");
        input.setEmailAddress("taro@example.com");
        input.setStudentTypeCode("FREELANCE");
        input.setTrainingId("t01");

        Reservation reservation = reservationService.reserve(input);

        Map<String, Object> reservationMap = jdbcTemplate.queryForMap("select * from reservation where id=?", reservation.getId());

        assertThat(reservationMap.get("name")).isEqualTo("東京太郎");
        assertThat(reservationMap.get("phone")).isEqualTo("090-0000-0000");
        assertThat(reservationMap.get("email_address")).isEqualTo("taro@example.com");
        assertThat(reservationMap.get("student_type_id")).isEqualTo("st03");
        assertThat(reservationMap.get("training_id")).isEqualTo("t01");

        Map<String, Object> trainingMap = jdbcTemplate.queryForMap("select * from training where id=?", "t01");
        assertThat(trainingMap.get("reserved")).isEqualTo(4);

    }

    @Test
    public void test_reserve_定員オーバー() {

        ReservationInput input = new ReservationInput();
        input.setTrainingId("t02");

        assertThatThrownBy(() -> {
            reservationService.reserve(input);
        }).isInstanceOf(CapacityOverException.class);

    }
}
