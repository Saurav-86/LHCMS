package com.lhcms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabResultRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Test name is required")
    private String testName;

    @NotBlank(message = "Result is required")
    private String result;

    private String unit;

    private String referenceRange;

    private String labStatus;

    private String description;
}
