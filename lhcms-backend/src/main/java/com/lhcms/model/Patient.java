package com.lhcms.model;

import com.lhcms.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
@DiscriminatorValue("PATIENT")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "appointments")
@EqualsAndHashCode(callSuper = true)
public class Patient extends User {

    private LocalDate dateOfBirth;

    private String bloodGroup;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    @Column(columnDefinition = "TEXT")
    private String chronicConditions;

    private String address;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    @PostLoad
    @PostPersist
    private void ensureRole() {
        if (getRole() == null) setRole(Role.PATIENT);
    }

    @PrePersist
    protected void setDefaultRole() {
        if (getRole() == null) setRole(Role.PATIENT);
    }
}
