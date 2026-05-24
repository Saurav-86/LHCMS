package com.lhcms.service;

import com.lhcms.dto.AuthResponse;
import com.lhcms.dto.RegisterDoctorRequest;
import com.lhcms.dto.RegisterRequest;
import com.lhcms.model.Doctor;
import com.lhcms.model.Patient;
import com.lhcms.model.Specialization;
import com.lhcms.model.enums.Role;
import com.lhcms.repository.DoctorRepository;
import com.lhcms.repository.PatientRepository;
import com.lhcms.repository.SpecializationRepository;
import com.lhcms.repository.UserRepository;
import com.lhcms.security.JwtTokenProvider;
import com.lhcms.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final SpecializationRepository specializationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                       PatientRepository patientRepository,
                       DoctorRepository doctorRepository,
                       SpecializationRepository specializationRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.specializationRepository = specializationRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(UserPrincipal::create)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public AuthResponse registerPatient(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Email already registered: " + request.getUsername());
        }

        Patient patient = new Patient();
        patient.setUsername(request.getUsername());
        patient.setPassword(passwordEncoder.encode(request.getPassword()));
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setPhone(request.getPhone());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setRole(Role.PATIENT);

        patientRepository.save(patient);

        UserPrincipal principal = UserPrincipal.create(patient);
        String token = jwtTokenProvider.generateToken(principal);
        return buildAuthResponse(token, principal);
    }

    public AuthResponse registerDoctor(RegisterDoctorRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Email already registered: " + request.getUsername());
        }

        Doctor doctor = new Doctor();
        doctor.setUsername(request.getUsername());
        doctor.setPassword(passwordEncoder.encode(request.getPassword()));
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setPhone(request.getPhone());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setQualification(request.getQualification());
        doctor.setYearsOfExperience(request.getYearsOfExperience());
        doctor.setBio(request.getBio());
        doctor.setRole(Role.DOCTOR);

        if (request.getSpecializationId() != null) {
            Specialization spec = specializationRepository.findById(request.getSpecializationId())
                    .orElseThrow(() -> new IllegalArgumentException("Specialization not found"));
            doctor.setSpecialization(spec);
        }

        doctorRepository.save(doctor);

        UserPrincipal principal = UserPrincipal.create(doctor);
        String token = jwtTokenProvider.generateToken(principal);
        return buildAuthResponse(token, principal);
    }

    public AuthResponse buildAuthResponse(String token, UserPrincipal principal) {
        AuthResponse.UserDto userDto = new AuthResponse.UserDto(
                principal.getId(),
                principal.getUsername(),
                principal.getUsername(),
                principal.getFirstName(),
                principal.getLastName(),
                principal.getRole()
        );
        return new AuthResponse(token, userDto);
    }
}
