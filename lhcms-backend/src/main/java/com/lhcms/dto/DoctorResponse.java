package com.lhcms.dto;

import com.lhcms.model.Doctor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DoctorResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String licenseNumber;
    private String qualification;
    private Integer yearsOfExperience;
    private String bio;
    private Long specializationId;
    private String specializationName;

    public static DoctorResponse from(Doctor d) {
        return new DoctorResponse(
                d.getId(),
                d.getFirstName(),
                d.getLastName(),
                d.getLicenseNumber(),
                d.getQualification(),
                d.getYearsOfExperience(),
                d.getBio(),
                d.getSpecialization() != null ? d.getSpecialization().getId() : null,
                d.getSpecialization() != null ? d.getSpecialization().getName() : null
        );
    }
}
