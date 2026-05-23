package com.lhcms.model;

import com.lhcms.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
@DiscriminatorValue("DOCTOR")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "appointments")
@EqualsAndHashCode(callSuper = true)
public class Doctor extends User {

    @Column(unique = true)
    private String licenseNumber;

    private String qualification;

    private Integer yearsOfExperience;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    @PrePersist
    protected void setDefaultRole() {
        if (getRole() == null) setRole(Role.DOCTOR);
    }
}
