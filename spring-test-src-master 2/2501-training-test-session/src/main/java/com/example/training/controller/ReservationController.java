package com.example.training.controller;

import java.util.List;

import com.example.training.entity.Reservation;
import com.example.training.entity.StudentType;
import com.example.training.entity.Training;
import com.example.training.exception.CapacityOverException;
import com.example.training.input.ReservationInput;
import com.example.training.service.ReservationService;
import com.example.training.service.TrainingService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private ReservationService reservationService;

    private TrainingService trainingService;

    private ReservationSession reservationSession;

    public ReservationController(ReservationService reservationService, TrainingService trainingService, ReservationSession reservationSession) {
        this.reservationService = reservationService;
        this.trainingService = trainingService;
        this.reservationSession = reservationSession;
    }

    @GetMapping("/display-form")
    public String displayForm(@RequestParam String trainingId, Model model) {
        ReservationInput input = new ReservationInput();
        input.setTrainingId(trainingId);
        input.setStudentTypeCode("EMPLOYEE");
        model.addAttribute(input);
        List<StudentType> studentTypes = reservationService.findAllStudentType();
        model.addAttribute(studentTypes);
        return "reservation/reservationForm";
    }

    @PostMapping("/validate-input")
    public String validateInput(@Validated ReservationInput input, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(reservationService.findAllStudentType());
            return "reservation/reservationForm";
        }
        StudentType studentType = reservationService.findStudentTypeByCode(input.getStudentTypeCode());
        model.addAttribute(studentType);
        Training training = trainingService.findById(input.getTrainingId());
        model.addAttribute(training);

        reservationSession.setReservationInput(input);
        return "reservation/reservationConfirmation";
    }

    @PostMapping(value = "/reserve", params = "correct")
    public String correctForm(Model model) {
        model.addAttribute(reservationSession.getReservationInput());
        model.addAttribute(reservationService.findAllStudentType());
        return "reservation/reservationForm";
    }

    @PostMapping(value = "/reserve", params = "reserve")
    public String reserve(RedirectAttributes redirectAttributes) {
        ReservationInput input = reservationSession.getReservationInput();
        Reservation reservation = reservationService.reserve(input);
        redirectAttributes.addFlashAttribute(reservation);
        return "redirect:/reservation/display-completion";
    }

    @GetMapping("/display-completion")
    public String displayCompletion() {
        reservationSession.clearSession();
        return "reservation/reservationCompletion";
    }

    @ExceptionHandler(CapacityOverException.class)
    public String displayCapcityOverPage() {
        return "reservation/capacityOver";
    }

}
