package com.example.training.repository;

import com.example.training.entity.Reservation;
import com.example.training.entity.Training;
import org.junit.jupiter.api.BeforeEach;
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
@Import(JdbcReservationRepository.class)
class JdbcReservationRepositoryTest {
    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void test_insert() {
        Reservation reservation = new Reservation();
        reservation.setId("r01");
        reservation.setName("東京太郎");
        reservationRepository.insert(reservation);
        Map<String, Object> reservationMap = jdbcTemplate.queryForMap("select name from reservation where id=?", "r01");
        assertThat(reservationMap.get("name")).isEqualTo("東京太郎");
    }
}

