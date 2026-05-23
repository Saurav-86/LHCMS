package com.lhcms.dto;

import com.lhcms.model.Appointment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AppointmentResponse {

    private Long id;
    private LocalDateTime appointmentDate;
    private String status;
    private String reason;
    private String notes;
    private LocalDateTime createdAt;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;

    private Long specializationId;
    private String specializationName;

    public static AppointmentResponse from(Appointment a) {
        return new AppointmentResponse(
                a.getId(),
                a.getAppointmentDate(),
                a.getStatus().name(),
                a.getReason(),
                a.getNotes(),
                a.getCreatedAt(),
                a.getPatient().getId(),
                a.getPatient().getFirstName() + " " + a.getPatient().getLastName(),
                a.getDoctor().getId(),
                a.getDoctor().getFirstName() + " " + a.getDoctor().getLastName(),
                a.getSpecialization().getId(),
                a.getSpecialization().getName()
        );
    }
}
