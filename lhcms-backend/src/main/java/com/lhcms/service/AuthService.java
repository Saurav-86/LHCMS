package com.lhcms.service;

import com.lhcms.dto.AuthResponse;
import com.lhcms.dto.RegisterRequest;
import com.lhcms.model.Patient;
import com.lhcms.model.enums.Role;
import com.lhcms.repository.PatientRepository;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                       PatientRepository patientRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
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

    public AuthResponse buildAuthResponse(String token, UserPrincipal principal) {
        return new AuthResponse(
                token,
                principal.getId(),
                principal.getUsername(),
                principal.getFirstName(),
                principal.getLastName(),
                principal.getRole()
        );
    }
}
