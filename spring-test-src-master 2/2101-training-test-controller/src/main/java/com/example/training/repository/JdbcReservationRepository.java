package com.example.training.repository;

import com.example.training.entity.Reservation;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insert(Reservation reservation) {
        jdbcTemplate.update("insert into reservation values (?,?,?,?,?,?,?)",
            reservation.getId(),
            reservation.getTrainingId(),
            reservation.getStudentTypeId(),
            reservation.getReservedDateTime(),
            reservation.getName(),
            reservation.getPhone(),
            reservation.getEmailAddress());
    }
}
