package com.example.training.controller;

import com.example.training.entity.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("ReservationControllerIntegrationTest.sql")
public class ReservationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void test_reserve() throws Exception {

        MvcResult mvcResult = mockMvc.perform(
                post("/reservation/reserve")
                    .param("reserve", "")
                    .param("name", "東京太郎")
                    .param("phone", "090-0000-0000")
                    .param("emailAddress", "taro@example.com")
                    .param("studentTypeCode", "FREELANCE")
                    .param("trainingId", "t01")
            )
            .andExpect(redirectedUrl("/reservation/display-completion"))
            .andReturn();

        Reservation reservation = (Reservation)mvcResult.getFlashMap().get("reservation");

        Map<String, Object> reservationMap = jdbcTemplate.queryForMap("select * from reservation where id=?", reservation.getId());

        assertThat(reservationMap.get("name")).isEqualTo("東京太郎");
        assertThat(reservationMap.get("phone")).isEqualTo("090-0000-0000");
        assertThat(reservationMap.get("email_address")).isEqualTo("taro@example.com");
        assertThat(reservationMap.get("student_type_id")).isEqualTo("st03");
        assertThat(reservationMap.get("training_id")).isEqualTo("t01");

        Map<String, Object> trainingMap = jdbcTemplate.queryForMap("select * from training where id=?", "t01");
        assertThat(trainingMap.get("reserved")).isEqualTo(2);


    }
}