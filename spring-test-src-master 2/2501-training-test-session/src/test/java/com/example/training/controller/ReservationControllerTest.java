package com.example.training.controller;


import com.example.training.entity.Reservation;
import com.example.training.entity.StudentType;
import com.example.training.entity.Training;
import com.example.training.exception.CapacityOverException;
import com.example.training.input.ReservationInput;
import com.example.training.service.ReservationService;
import com.example.training.service.TrainingService;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ReservationController.class)
@Import(ReservationSession.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private TrainingService trainingService;

    @Test
    void test_validateInput() throws Exception {
        StudentType studentType = new StudentType();
        doReturn(studentType).when(reservationService).findStudentTypeByCode("EMPLOYEE");
        Training training = new Training();
        doReturn(training).when(trainingService).findById("t01");
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
        ReservationSession reservationSession = (ReservationSession)mvcResult.getRequest().getSession().getAttribute("scopedTarget.reservationSession");
        ReservationInput reservationInput = reservationSession.getReservationInput();
        assertThat(reservationInput.getName()).isEqualTo("東京太郎");
        assertThat(reservationInput.getPhone()).isEqualTo("090-0000-0000");
        assertThat(reservationInput.getEmailAddress()).isEqualTo("taro@example.com");
        assertThat(reservationInput.getStudentTypeCode()).isEqualTo("EMPLOYEE");
        assertThat(reservationInput.getTrainingId()).isEqualTo("t01");
    }

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

        ReservationInput reservationInput = new ReservationInput();
        reservationInput.setName("東京太郎");
        ReservationSession reservationSession = new ReservationSession();
        reservationSession.setReservationInput(reservationInput);

        mockMvc.perform(
                post("/reservation/reserve")
                    .param("reserve", "")
                    .sessionAttr("scopedTarget.reservationSession", reservationSession)
            )
            .andExpect(flash().attribute("reservation", reservation))
            .andExpect(redirectedUrl("/reservation/display-completion"))
        ;

        ArgumentCaptor<ReservationInput> captor = ArgumentCaptor.forClass(ReservationInput.class);
        verify(reservationService).reserve(captor.capture());
        ReservationInput captoredInput = captor.getValue();
        assertThat(captoredInput.getName()).isEqualTo("東京太郎");
    }

    @Test
    public void test_reserve_定員オーバー() throws Exception {

        doThrow(CapacityOverException.class).when(reservationService).reserve(any());

        mockMvc.perform(post("/reservation/reserve").param("reserve", ""))
            .andExpect(content().string(containsString("<h1>定員オーバー</h1>")))
        ;
    }
}