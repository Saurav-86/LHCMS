package com.lhcms.service;

import com.lhcms.model.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void notifyAppointmentBooked(Appointment appointment) {
        String body = String.format(
                "Dear %s,\n\nYour appointment has been confirmed.\n\nDoctor: Dr. %s\nSpecialization: %s\nDate & Time: %s\nReason: %s\n\nLHCMS Team",
                appointment.getPatient().getFirstName(),
                appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName(),
                appointment.getSpecialization().getName(),
                appointment.getAppointmentDate(),
                appointment.getReason() != null ? appointment.getReason() : "—"
        );
        sendEmail(appointment.getPatient().getUsername(), "Appointment Confirmed — LHCMS", body);

        String doctorBody = String.format(
                "Dear Dr. %s,\n\nA new appointment has been booked.\n\nPatient: %s\nDate & Time: %s\nReason: %s\n\nLHCMS Team",
                appointment.getDoctor().getFirstName(),
                appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName(),
                appointment.getAppointmentDate(),
                appointment.getReason() != null ? appointment.getReason() : "—"
        );
        sendEmail(appointment.getDoctor().getUsername(), "New Appointment — LHCMS", doctorBody);
    }

    public void notifyAppointmentRescheduled(Appointment appointment) {
        String body = String.format(
                "Dear %s,\n\nYour appointment has been rescheduled.\n\nNew Date & Time: %s\nDoctor: Dr. %s\n\nLHCMS Team",
                appointment.getPatient().getFirstName(),
                appointment.getAppointmentDate(),
                appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName()
        );
        sendEmail(appointment.getPatient().getUsername(), "Appointment Rescheduled — LHCMS", body);
    }

    public void notifyAppointmentCancelled(Appointment appointment) {
        String body = String.format(
                "Dear %s,\n\nYour appointment on %s with Dr. %s has been cancelled.\n\nLHCMS Team",
                appointment.getPatient().getFirstName(),
                appointment.getAppointmentDate(),
                appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName()
        );
        sendEmail(appointment.getPatient().getUsername(), "Appointment Cancelled — LHCMS", body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email sent to {}", to);
        } catch (MailException e) {
            log.warn("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
