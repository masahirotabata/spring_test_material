package com.example.training.controller;


import com.example.training.entity.Reservation;
import com.example.training.exception.CapacityOverException;
import com.example.training.service.ReservationService;
import com.example.training.service.TrainingService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private TrainingService trainingService;


    @Test
    void test_validateInput_入力エラー() throws Exception {
        mockMvc.perform(
                post("/reservation/validate-input")
            )
            .andExpect(view().name("reservation/reservationForm"))
            .andExpect(model().attributeHasFieldErrorCode("reservationInput", "name", "NotBlank"))
            .andExpect(model().attributeHasFieldErrorCode("reservationInput", "phone", "NotBlank"))
            .andExpect(model().attributeHasFieldErrorCode("reservationInput", "emailAddress", "NotBlank"))
            .andExpect(model().attributeHasFieldErrorCode("reservationInput", "studentTypeCode", "NotBlank"))
        ;
    }

    @Test
    public void test_validateInput_入力エラー2() throws Exception {
        mockMvc.perform(post("/reservation/validate-input"))
            .andExpect(model().attributeHasFieldErrors("reservationInput", "name", "phone", "emailAddress", "studentTypeCode"))
        ;
    }

    @Test
    public void test_reserve() throws Exception {
        Reservation reservation = new Reservation();
        doReturn(reservation).when(reservationService).reserve(any());
        mockMvc.perform(post("/reservation/reserve").param("reserve", "")
                .param("name", "foo")
                .param("phone", "090-0000-0000")
                .param("emailAddress", "aaa@example.com")
                .param("studentTypeCode", "EMPLOYEE")
            )
            .andExpect(flash().attribute("reservation", reservation))
            .andExpect(redirectedUrl("/reservation/display-completion"))
        ;
    }

    @Test
    public void test_reserve_定員オーバー() throws Exception {

        doThrow(CapacityOverException.class).when(reservationService).reserve(any());

        mockMvc.perform(post("/reservation/reserve").param("reserve", "")
                .param("name", "foo")
                .param("phone", "090-0000-0000")
                .param("emailAddress", "aaa@example.com")
                .param("studentTypeCode", "EMPLOYEE")
            )
            .andExpect(content().string(containsString("<h1>定員オーバー</h1>")))
        ;
    }
}