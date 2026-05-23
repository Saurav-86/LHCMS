package com.lhcms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String role;
}
