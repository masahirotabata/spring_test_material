package com.example.training.controller;

import java.io.Serializable;

import com.example.training.input.ReservationInput;

import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("serial")
public class ReservationSession implements Serializable {
    private ReservationInput reservationInput;

    public void clearSession() {
        this.reservationInput = null;
    }

    public ReservationInput getReservationInput() {
        return reservationInput;
    }

    public void setReservationInput(ReservationInput reservationInput) {
        this.reservationInput = reservationInput;
    }
}
