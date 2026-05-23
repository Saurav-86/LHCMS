# LHCMS — Claude Code Instructions
## How to use Claude Code effectively for this project

---

## What is Claude Code?

Claude Code is a CLI tool that runs in your terminal and can:
- Read and write files directly
- Run shell commands
- Understand your entire codebase
- Write, debug, and refactor code
- Run tests and fix errors

---

## Installation

```bash
# Install Claude Code globally
npm install -g @anthropic-ai/claude-code

# Verify
claude --version
```

---

## How to Start Claude Code

```bash
# Always start from project root
cd ~/lhcms

# Start Claude Code
claude
```

Or for a specific folder:
```bash
# Backend only
cd ~/lhcms/lhcms-backend
claude

# Frontend only
cd ~/lhcms/lhcms-frontend
claude
```

---

## Project Context to Give Claude Code

**Always paste this at the start of every Claude Code session:**

```
This is the LHCMS (Lalitpur Health Care Management System) project.

Tech stack:
- Backend: Spring Boot 3.5.0, Java 21, Maven
- Frontend: React 18 + TypeScript
- Database: PostgreSQL 15 (running in Docker on port 5432)
- Auth: Spring Security + JWT
- Real-time: WebSocket

Project location:
- Backend: ~/lhcms/lhcms-backend
- Frontend: ~/lhcms/lhcms-frontend

Package: com.lhcms
DB: lhcms, user: lhcmsuser, password: yourpassword

Architecture: Controller → Service → Repository → DB (Layered)

Domain classes: User, Patient, Doctor, Appointment, Specialization,
MedicalHistory, MedicalRecord, Prescription, LabResult,
Payment, Notification, Message, Conversation
```

---

## Effective Claude Code Prompts

### Creating New Files
```
Create a JPA entity class for Patient in 
src/main/java/com/lhcms/model/Patient.java
It should extend User, have patientId, dateOfBirth, 
allergies, chronicConditions fields with Lombok annotations
```

### Writing Services
```
Create PatientService in service/PatientService.java
with methods:
- registerPatient(RegisterRequest request)
- getPatientById(Long id)
- updatePatient(Long id, UpdateRequest request)
Use constructor injection, not @Autowired
```

### Fixing Errors
```
I'm getting this error when running the app:
[paste full stack trace here]
Fix it
```

### Writing Tests
```
Write unit tests for AuthService 
using JUnit 5 and Mockito
Test: registerPatient, login, invalid credentials
```

### Frontend Components
```
Create a Login page component in 
src/pages/Login.tsx
Use Material UI, React Hook Form, axios
On submit POST to http://localhost:8080/api/auth/login
Store JWT token in localStorage
Redirect to dashboard on success
```

---

## Workflow — Backend Feature

Use this order for every new feature:

```
1. Model/Entity first
   "Create JPA entity for [ClassName]"

2. Repository
   "Create Spring Data JPA repository for [ClassName]"

3. DTO
   "Create request/response DTOs for [feature]"

4. Service
   "Create service with business logic for [feature]"

5. Controller
   "Create REST controller with endpoints for [feature]"

6. Test
   "Test this endpoint with curl"
```

---

## Workflow — Frontend Feature

```
1. Service/API call
   "Create axios service for [feature] API calls"

2. Types
   "Create TypeScript interfaces for [feature]"

3. Page/Component
   "Create React page for [feature]"

4. Add to Router
   "Add route for [feature] in App.tsx"
```

---

## Useful Claude Code Commands

```bash
# Ask Claude Code to explain a file
"Explain what this file does: src/main/java/com/lhcms/security/JwtTokenProvider.java"

# Ask it to find bugs
"Find any bugs or issues in the AuthService"

# Ask it to refactor
"Refactor AppointmentController to follow REST best practices"

# Ask it to add validation
"Add proper validation to RegisterRequest DTO"

# Ask it to write the whole feature
"Implement the complete Book Appointment feature —
model, repository, service, controller, and DTOs"
```

---

## Important Rules for Claude Code

1. **Always specify file paths** — tell it exactly where to create files
2. **Always specify package names** — `com.lhcms.model`, `com.lhcms.service` etc.
3. **Paste errors in full** — give complete stack traces, not summaries
4. **One feature at a time** — don't ask for everything at once
5. **Review before applying** — read the code before saying yes
6. **Commit after each feature** — don't lose working code

---

## Git Workflow with Claude Code

```bash
# After Claude Code writes a feature
git add .
git commit -m "Add patient registration — model, service, controller"
git push
```

If something breaks:
```bash
# Revert to last working commit
git checkout -- .
```

---

## Backend Build Order (Follow This Exactly)

### Phase 1 — Auth (Start Here)
```
1. User.java (model)
2. Patient.java (model)
3. Doctor.java (model)
4. UserRepository.java
5. PatientRepository.java
6. RegisterRequest.java (DTO)
7. LoginRequest.java (DTO)
8. AuthResponse.java (DTO)
9. JwtTokenProvider.java (security)
10. JwtAuthFilter.java (security)
11. SecurityConfig.java (config)
12. AuthService.java
13. AuthController.java
```

### Phase 2 — Appointments
```
14. Specialization.java (model)
15. Appointment.java (model)
16. Notification.java (model)
17. AppointmentRepository.java
18. AppointmentRequest.java (DTO)
19. AppointmentService.java
20. AppointmentController.java
```

### Phase 3 — Medical Records
```
21. MedicalHistory.java (model)
22. MedicalRecord.java (model)
23. Prescription.java (model)
24. LabResult.java (model)
25. MedicalRecordService.java
26. MedicalRecordController.java
```

### Phase 4 — Payment
```
27. Payment.java (model)
28. PaymentService.java (Factory pattern)
29. PaymentController.java
```

### Phase 5 — Chat
```
30. Conversation.java (model)
31. Message.java (model)
32. WebSocketConfig.java
33. ChatController.java (WebSocket)
```

---

## Frontend Build Order

### Phase 1 — Auth
```
1. src/types/auth.ts (interfaces)
2. src/services/authService.ts (axios calls)
3. src/store/slices/authSlice.ts (Redux)
4. src/pages/Login.tsx
5. src/pages/Register.tsx
```

### Phase 2 — Patient Dashboard
```
6. src/pages/PatientDashboard.tsx
7. src/pages/BookAppointment.tsx
8. src/pages/MedicalRecords.tsx
9. src/pages/Payment.tsx
```

### Phase 3 — Doctor Dashboard
```
10. src/pages/DoctorDashboard.tsx
11. src/pages/UploadDocument.tsx
```

### Phase 4 — Chat
```
12. src/pages/Chat.tsx
13. src/components/ChatBox.tsx
```

---

## Testing Each Endpoint

### After AuthController is built, test with curl:

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

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test@lhcms.com",
    "password": "password123"
  }'
```

---

## Common Issues + Fixes

| Issue | Fix |
|---|---|
| `Could not autowire` | Check @Service/@Repository annotations |
| `Table not found` | Check ddl-auto=update in properties |
| `JWT invalid` | Check secret key length (min 32 chars) |
| `CORS error` | Add @CrossOrigin or CorsConfig |
| `Port 8080 in use` | `sudo lsof -i :8080` then kill process |
| `Lombok not working` | Enable annotation processing in IntelliJ |

---

## Access Points

| Service | URL |
|---|---|
| Backend API | http://localhost:8080 |
| Frontend | http://localhost:3000 |
| Database | localhost:5432 |
| GitHub | https://github.com/Saurav-86/LHCMS |

---

*Use this file as context at the start of every Claude Code session.*
*Follow the build order strictly — don't skip phases.*
