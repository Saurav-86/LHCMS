package com.lhcms.config;

import com.lhcms.model.Doctor;
import com.lhcms.model.Specialization;
import com.lhcms.model.enums.Role;
import com.lhcms.repository.DoctorRepository;
import com.lhcms.repository.SpecializationRepository;
import com.lhcms.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SpecializationRepository specializationRepo;
    private final DoctorRepository doctorRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(SpecializationRepository specializationRepo,
                           DoctorRepository doctorRepo,
                           UserRepository userRepo,
                           PasswordEncoder passwordEncoder) {
        this.specializationRepo = specializationRepo;
        this.doctorRepo = doctorRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedSpecializations();
        seedDoctors();
    }

    private void seedSpecializations() {
        List<String[]> specs = List.of(
            new String[]{"General Medicine",   "Primary care and general health consultations"},
            new String[]{"Cardiology",         "Heart and cardiovascular system"},
            new String[]{"Orthopedics",        "Bones, joints, muscles and the musculoskeletal system"},
            new String[]{"Neurology",          "Brain, spinal cord and nervous system disorders"},
            new String[]{"Pediatrics",         "Medical care for infants, children and adolescents"},
            new String[]{"Dermatology",        "Skin, hair and nail conditions"},
            new String[]{"Gynecology",         "Female reproductive health"},
            new String[]{"ENT",               "Ear, nose and throat disorders"}
        );

        for (String[] s : specs) {
            if (specializationRepo.findByName(s[0]).isEmpty()) {
                Specialization spec = new Specialization();
                spec.setName(s[0]);
                spec.setDescription(s[1]);
                specializationRepo.save(spec);
            }
        }
    }

    private void seedDoctors() {
        List<Object[]> doctors = List.of(
            new Object[]{"dr.rajan@lhcms.com",  "doctor123", "Rajan",  "Sharma",  "General Medicine",  "MBBS, MD",         10, "LIC-001"},
            new Object[]{"dr.sita@lhcms.com",   "doctor123", "Sita",   "Thapa",   "Cardiology",        "MBBS, DM Cardio",  8,  "LIC-002"},
            new Object[]{"dr.hari@lhcms.com",   "doctor123", "Hari",   "Khadka",  "Orthopedics",       "MBBS, MS Ortho",   12, "LIC-003"},
            new Object[]{"dr.maya@lhcms.com",   "doctor123", "Maya",   "Poudel",  "Pediatrics",        "MBBS, MD Paeds",   6,  "LIC-004"}
        );

        for (Object[] d : doctors) {
            String username = (String) d[0];
            if (userRepo.existsByUsername(username)) continue;

            Specialization spec = specializationRepo.findByName((String) d[4]).orElse(null);

            Doctor doctor = new Doctor();
            doctor.setUsername(username);
            doctor.setPassword(passwordEncoder.encode((String) d[1]));
            doctor.setFirstName((String) d[2]);
            doctor.setLastName((String) d[3]);
            doctor.setSpecialization(spec);
            doctor.setQualification((String) d[5]);
            doctor.setYearsOfExperience((Integer) d[6]);
            doctor.setLicenseNumber((String) d[7]);
            doctor.setRole(Role.DOCTOR);

            doctorRepo.save(doctor);
        }
    }
}
