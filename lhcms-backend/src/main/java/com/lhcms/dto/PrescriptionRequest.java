package com.lhcms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Medication is required")
    private String medication;

    private String dosage;

    private String frequency;

    private String duration;

    private String description;

    private String prescriptionNotes;
}
