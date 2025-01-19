package com.example.training.service;

import com.example.training.entity.Reservation;
import com.example.training.entity.StudentType;
import com.example.training.entity.Training;
import com.example.training.exception.CapacityOverException;
import com.example.training.input.ReservationInput;
import com.example.training.repository.ReservationRepository;
import com.example.training.repository.StudentTypeRepository;
import com.example.training.repository.TrainingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private StudentTypeRepository studentTypeRepository;
    private TrainingRepository trainingRepository;
    private ReservationRepository reservationRepository;

    public ReservationServiceImpl(StudentTypeRepository studentTypeRepository, TrainingRepository trainingRepository, ReservationRepository reservationRepository) {
        this.studentTypeRepository = studentTypeRepository;
        this.trainingRepository = trainingRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<StudentType> findAllStudentType() {
        return studentTypeRepository.selectAll();
    }

    @Override
    public StudentType findStudentTypeByCode(String studentTypeCode) {
        return studentTypeRepository.selectByCode(studentTypeCode);
    }

    @Override
    public Reservation reserve(ReservationInput input) {
        Training training = trainingRepository.selectById(input.getTrainingId());
        training.setReserved(training.getReserved() + 1);
        if (training.getReserved() > training.getCapacity()) {
            throw new CapacityOverException("定員オーバー");
        }
        trainingRepository.update(training);

        StudentType studentType = studentTypeRepository.selectByCode(input.getStudentTypeCode());
        Reservation reservation = new Reservation();
        reservation.setId(UUID.randomUUID().toString());
        reservation.setTrainingId(training.getId());
        reservation.setStudentTypeId(studentType.getId());
        reservation.setName(input.getName());
        reservation.setPhone(input.getPhone());
        reservation.setReservedDateTime(LocalDateTime.now());
        reservation.setEmailAddress(input.getEmailAddress());
        reservationRepository.insert(reservation);

        return reservation;
    }
}
