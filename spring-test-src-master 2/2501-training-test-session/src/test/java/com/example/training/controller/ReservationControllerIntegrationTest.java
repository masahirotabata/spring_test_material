package com.example.training.controller;

import com.example.training.entity.Reservation;
import com.example.training.entity.StudentType;
import com.example.training.entity.Training;
import com.example.training.input.ReservationInput;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
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
    void test_validateInput() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                post("/reservation/validate-input")
                    .param("name", "東京太郎")
                    .param("phone", "090-0000-0000")
                    .param("emailAddress", "taro@example.com")
                    .param("studentTypeCode", "EMPLOYEE")
                    .param("trainingId", "t01")
            )
            .andExpect(view().name("reservation/reservationConfirmation"))
            .andReturn();
        ;
        ReservationSession reservationSession = (ReservationSession)mvcResult.getRequest().getSession().getAttribute("scopedTarget.reservationSession");
        ReservationInput reservationInput = reservationSession.getReservationInput();
        assertThat(reservationInput.getName()).isEqualTo("東京太郎");
        assertThat(reservationInput.getPhone()).isEqualTo("090-0000-0000");
        assertThat(reservationInput.getEmailAddress()).isEqualTo("taro@example.com");
        assertThat(reservationInput.getStudentTypeCode()).isEqualTo("EMPLOYEE");
        assertThat(reservationInput.getTrainingId()).isEqualTo("t01");
    }



    @Test
    public void test_reserve() throws Exception {
        ReservationInput reservationInput = new ReservationInput();
        reservationInput.setTrainingId("t01");
        reservationInput.setName("東京太郎");
        reservationInput.setPhone("090-0000-0000");
        reservationInput.setEmailAddress("taro@example.com");
        reservationInput.setStudentTypeCode("FREELANCE");

        ReservationSession reservationSession = new ReservationSession();
        reservationSession.setReservationInput(reservationInput);

        MvcResult mvcResult = mockMvc.perform(
                post("/reservation/reserve")
                    .param("reserve", "")
                    .sessionAttr("scopedTarget.reservationSession", reservationSession)
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