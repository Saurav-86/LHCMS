package com.lhcms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("LAB_RESULT")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LabResult extends MedicalRecord {

    @Column(nullable = false)
    private String testName;

    @Column(nullable = false)
    private String result;

    private String unit;

    private String referenceRange;

    private String labStatus;
}
