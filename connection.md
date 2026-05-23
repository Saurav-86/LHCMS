# LHCMS — Frontend ↔ Backend Connection Reference

Everything the backend must conform to so the React frontend connects without changes.

---

## Base URLs

| Service   | URL                       |
|-----------|---------------------------|
| Frontend  | http://localhost:3000     |
| Backend   | http://localhost:8080     |
| Database  | localhost:5432            |

---

## CORS — Required or every request is blocked

The browser blocks all cross-origin requests unless the backend explicitly allows them.

```java
// SecurityConfig.java — inside the SecurityFilterChain bean
@Bean
CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:3000"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
}
```

Also call `.cors(cors -> cors.configurationSource(corsConfigurationSource()))` inside your `http` chain.

---

## Auth Endpoints

### POST `/api/auth/register`

Registers a new patient. Doctors are registered by the clinic (not via this endpoint).

**Request body the frontend sends:**
```json
{
  "username": "john@email.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1995-01-01",
  "allergies": "Penicillin",
  "chronicConditions": "Diabetes"
}
```

- `username` is the email address used as the login credential
- `dateOfBirth` is ISO format `yyyy-MM-dd`
- `allergies` and `chronicConditions` are **optional** — backend must accept them missing/null

---

### POST `/api/auth/login`

**Request body the frontend sends:**
```json
{
  "username": "john@email.com",
  "password": "password123"
}
```

---

### Response — BOTH endpoints must return this exact JSON shape

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "user": {
    "id": 1,
    "username": "john@email.com",
    "email": "john@email.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "PATIENT"
  }
}
```

**Rules:**
- Must be a nested `user` object — NOT flat fields like `token`, `username`, `role` at top level
- `role` must be exactly `"PATIENT"` or `"DOCTOR"` — all uppercase — the frontend does `user.role === 'DOCTOR'` to route to the correct dashboard
- `id` must be a number (not a string)
- All six user fields (`id`, `username`, `email`, `firstName`, `lastName`, `role`) must be present

**What the frontend does with this response:**
- Stores `token` in `localStorage` as key `"token"`
- Stores the full `user` object in `localStorage` as key `"user"` (JSON.stringify)
- Redirects to `/doctor/dashboard` if `role === "DOCTOR"`, otherwise `/patient/dashboard`

---

### Error Response Shape

When login or registration fails, the frontend reads `err.response.data.message` for the toast message.

```json
{
  "message": "Invalid credentials"
}
```

If this field is missing or named differently (`error`, `detail`, etc.), the toast will fall back to the generic message "Login failed" / "Registration failed".

**HTTP status codes the frontend handles:**
- `200` / `201` — success, reads the `{ token, user }` body
- `4xx` / `5xx` — failure, reads `response.data.message`

---

## JWT — How the frontend uses the token

After login, all subsequent protected API calls will include:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

The backend's `JwtAuthFilter` must extract the token from the `Authorization` header with the `Bearer ` prefix.

**Public endpoints** (no token required — must be permitted in SecurityConfig):
```
POST /api/auth/register
POST /api/auth/login
```

**Protected endpoints** (token required):
```
Everything else under /api/**
```

---

## Role Enum

The frontend TypeScript type is:
```ts
role: 'PATIENT' | 'DOCTOR'
```

The backend Java enum must serialize to the same uppercase strings:

```java
public enum Role {
    PATIENT, DOCTOR
}
```

If you use `@JsonProperty` or any custom serializer, ensure the output is `"PATIENT"` or `"DOCTOR"` — not `"patient"`, `"Patient"`, `"ROLE_PATIENT"`, etc.

---

## AuthResponse DTO the Backend Must Return

```java
// dto/AuthResponse.java
public class AuthResponse {
    private String token;
    private UserDto user;
    // getters/setters or Lombok @Data
}

// dto/UserDto.java
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;   // "PATIENT" or "DOCTOR"
    // getters/setters or Lombok @Data
}
```

---

## RegisterRequest DTO the Backend Must Accept

```java
// dto/RegisterRequest.java
public class RegisterRequest {
    private String username;        // used as email
    private String password;
    private String firstName;
    private String lastName;
    private String dateOfBirth;     // "yyyy-MM-dd"
    private String allergies;       // nullable
    private String chronicConditions; // nullable
}
```

---

## LoginRequest DTO the Backend Must Accept

```java
// dto/LoginRequest.java
public class LoginRequest {
    private String username;
    private String password;
}
```

---

## Quick Verification Checklist

After building the auth API, test with these curl commands:

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test@lhcms.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "Patient",
    "dateOfBirth": "1995-01-01"
  }'

# Expected: { "token": "...", "user": { "id": 1, "username": "test@lhcms.com", ... "role": "PATIENT" } }

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "test@lhcms.com", "password": "password123"}'

# Expected: same shape as register response
```

Then start the frontend (`npm start` in `lhcms-frontend/`) and try logging in — the browser network tab will show the exact request/response.

---

## Future Endpoints (to be added as frontend phases are built)

| Method | Endpoint                          | Who calls it   |
|--------|-----------------------------------|----------------|
| GET    | `/api/doctors?specialization=`    | Patient        |
| POST   | `/api/appointments`               | Patient        |
| PUT    | `/api/appointments/{id}`          | Patient        |
| GET    | `/api/patients/{id}/records`      | Patient        |
| POST   | `/api/payments`                   | Patient        |
| GET    | `/api/doctors/{id}/appointments`  | Doctor         |
| POST   | `/api/medical-records`            | Doctor         |
| GET    | `/api/conversations/{id}/messages`| Both           |
| POST   | `/api/conversations/{id}/messages`| Both           |

All of these will send `Authorization: Bearer <token>` in the header.

---

*Updated: 2026-05-23 — covers Frontend Phase 1 (Auth). Update this file as new frontend phases are built.*
