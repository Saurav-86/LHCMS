package com.lhcms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("PRESCRIPTION")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Prescription extends MedicalRecord {

    @Column(nullable = false)
    private String medication;

    private String dosage;

    private String frequency;

    private String duration;

    @Column(columnDefinition = "TEXT")
    private String prescriptionNotes;
}
