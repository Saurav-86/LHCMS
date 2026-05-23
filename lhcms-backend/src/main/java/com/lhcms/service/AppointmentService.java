package com.lhcms.service;

import com.lhcms.dto.AppointmentRequest;
import com.lhcms.dto.AppointmentResponse;
import com.lhcms.dto.DoctorResponse;
import com.lhcms.model.Appointment;
import com.lhcms.model.Doctor;
import com.lhcms.model.Patient;
import com.lhcms.model.Specialization;
import com.lhcms.model.enums.AppointmentStatus;
import com.lhcms.repository.AppointmentRepository;
import com.lhcms.repository.DoctorRepository;
import com.lhcms.repository.PatientRepository;
import com.lhcms.repository.SpecializationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final SpecializationRepository specializationRepository;
    private final NotificationService notificationService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              SpecializationRepository specializationRepository,
                              NotificationService notificationService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.specializationRepository = specializationRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public AppointmentResponse bookAppointment(AppointmentRequest request, Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + patientId));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + request.getDoctorId()));
        Specialization specialization = specializationRepository.findById(request.getSpecializationId())
                .orElseThrow(() -> new IllegalArgumentException("Specialization not found: " + request.getSpecializationId()));

        boolean conflict = appointmentRepository.existsByDoctorIdAndAppointmentDateAndStatusNot(
                request.getDoctorId(), request.getAppointmentDate(), AppointmentStatus.CANCELLED);
        if (conflict) {
            throw new IllegalArgumentException("Doctor already has an appointment at that date and time.");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setSpecialization(specialization);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setReason(request.getReason());

        Appointment saved = appointmentRepository.save(appointment);
        notificationService.notifyAppointmentBooked(saved);
        return AppointmentResponse.from(saved);
    }

    @Transactional
    public AppointmentResponse rescheduleAppointment(Long id, LocalDateTime newDate, Long requestingUserId) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + id));

        if (!appointment.getPatient().getId().equals(requestingUserId)) {
            throw new IllegalArgumentException("You can only reschedule your own appointments.");
        }
        if (!newDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("New appointment date must be in the future.");
        }

        boolean conflict = appointmentRepository.existsByDoctorIdAndAppointmentDateAndStatusNot(
                appointment.getDoctor().getId(), newDate, AppointmentStatus.CANCELLED);
        if (conflict) {
            throw new IllegalArgumentException("Doctor already has an appointment at that date and time.");
        }

        appointment.setAppointmentDate(newDate);
        appointment.setStatus(AppointmentStatus.RESCHEDULED);
        Appointment saved = appointmentRepository.save(appointment);
        notificationService.notifyAppointmentRescheduled(saved);
        return AppointmentResponse.from(saved);
    }

    @Transactional
    public void cancelAppointment(Long id, Long requestingUserId) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + id));

        boolean isPatient = appointment.getPatient().getId().equals(requestingUserId);
        boolean isDoctor = appointment.getDoctor().getId().equals(requestingUserId);
        if (!isPatient && !isDoctor) {
            throw new IllegalArgumentException("You are not authorized to cancel this appointment.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment saved = appointmentRepository.save(appointment);
        notificationService.notifyAppointmentCancelled(saved);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(AppointmentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(AppointmentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .map(AppointmentResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(DoctorResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DoctorResponse> getDoctorsBySpecialization(String specializationName) {
        return doctorRepository.findBySpecializationName(specializationName).stream()
                .map(DoctorResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DoctorResponse getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .map(DoctorResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Specialization> getAllSpecializations() {
        return specializationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Specialization getSpecializationById(Long id) {
        return specializationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Specialization not found: " + id));
    }
}
