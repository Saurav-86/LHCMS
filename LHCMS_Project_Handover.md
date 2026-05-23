# LHCMS Project Handover
## Lalitpur Health Care Management System
> Paste this into a new chat to continue development. All items marked ✅ are DONE — do not redo them.

---

## Project Overview

A full-stack web application for a group of private clinics in Lalitpur to manage patient records, appointments, doctor interactions, payments, and real-time chat.

---

## Case Study (Full Text)

> "A group of private clinics in Lalitpur are looking to have a system that stores patient's medical history and records. The system is needed in order to book appointments, view results of tests, chat with the doctors, while enabling hospitals to maintain a shared medical database. Patients can register via the web or a mobile app. Their Profile stores basic information, allergies, chronic conditions, and medical history. Patients can book appointments of doctors by specialization. An email/SMS is automatically sent when booking is confirmed. There should be an option to reschedule or cancel the booking. Doctors can upload prescriptions, lab results, and medical notes. Patients should be able to view their medical records. Payments can be done online via digital wallets like E-Sewa and Khalti. Doctors can view the upcoming appointments, patient history, and pending test results and update consultation notes in real-time."

---

## Tech Stack

| Layer | Technology | Version |
|---|---|---|
| Backend | Spring Boot | 3.5.0 |
| Language | Java | 21 |
| Build Tool | Maven | 3.9.12 |
| Frontend | React + TypeScript | 18 |
| Database | PostgreSQL | 15 (Docker) |
| Auth | Spring Security + JWT | — |
| Real-time | WebSocket (STOMP) | — |
| Container | Docker | 29.5.0 |
| IDE Backend | IntelliJ Ultimate | — |
| IDE Frontend | VS Code | — |
| Version Control | Git + GitHub | — |

---

## Environment Setup ✅ DONE

### What is Installed
- Java 21 ✅
- Maven 3.9.12 ✅
- Node.js 25.9.0 + npm 11.12.1 ✅
- Git 2.53.0 ✅
- Docker 29.5.0 ✅
- IntelliJ Ultimate ✅
- VS Code ✅

### Project Location
```
~/lhcms/
├── docker-compose.yml      ← PostgreSQL only
├── lhcms-backend/          ← Spring Boot (open in IntelliJ)
│   ├── src/main/java/com/lhcms/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   ├── dto/
│   │   ├── security/
│   │   └── config/
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
└── lhcms-frontend/         ← React (open in VS Code)
    ├── src/
    └── package.json
```

### GitHub Repository
- URL: https://github.com/Saurav-86/LHCMS.git
- Branch: main
- Initial commit pushed ✅

### Docker — PostgreSQL Only
```yaml
# ~/lhcms/docker-compose.yml
services:
  postgres:
    image: postgres:15
    container_name: lhcms-postgres
    environment:
      POSTGRES_DB: lhcms
      POSTGRES_USER: lhcmsuser
      POSTGRES_PASSWORD: yourpassword
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
volumes:
  postgres_data:
```

### application.properties
```properties
server.port=8080
spring.datasource.url=jdbc:postgresql://localhost:5432/lhcms
spring.datasource.username=lhcmsuser
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true
jwt.secret=lhcms-secret-key-minimum-32-characters-long
jwt.expiration=86400000
logging.level.com.lhcms=DEBUG
```

### Frontend Dependencies Installed ✅
```
axios, react-router-dom, @reduxjs/toolkit, react-redux,
@mui/material, @emotion/react, @emotion/styled,
react-hook-form, yup, @hookform/resolvers,
react-toastify, @mui/icons-material
```

---

## Daily Startup Commands

```bash
# 1. Start database
cd ~/lhcms && docker compose up -d postgres

# 2. Run backend
# Press Shift+F10 in IntelliJ

# 3. Run frontend
cd ~/lhcms/lhcms-frontend && npm start
```

---

## Domain Classes (13 Total)

`User`, `Patient`, `Doctor`, `Appointment`, `Specialization`,
`MedicalHistory`, `MedicalRecord`, `Prescription`, `LabResult`,
`Payment`, `Notification`, `Message`, `Conversation`

---

## Class Diagram Relationships

### Generalizations (is-a)
1. Patient → User
2. Doctor → User
3. Prescription → MedicalRecord
4. LabResult → MedicalRecord

### Compositions (strong has-a ◆)
1. Patient ◆→ MedicalHistory (1 to 1)
2. MedicalHistory ◆→ MedicalRecord (1 to 0..*)

### Aggregations (weak has-a ◇)
1. Appointment ◇→ Notification (1 to 0..*)
2. Conversation ◇→ Message (1 to 1..*)

### Associations
| From | To | Multiplicity | Name |
|---|---|---|---|
| Patient | Appointment | 1 to 0..* | books |
| Doctor | Appointment | 1 to 0..* | manages |
| Appointment | Specialization | * to 1 | requires |
| Payment | Appointment | 1 to 1 | covers |
| Conversation | Patient | * to 1 | involves |
| Conversation | Doctor | * to 1 | involves |
| MedicalRecord | Doctor | * to 1 | uploadedBy |

---

## API Endpoints to Build

| Method | Endpoint | Description | Actor |
|---|---|---|---|
| POST | `/api/auth/register` | Register patient | Patient |
| POST | `/api/auth/login` | Login | Patient + Doctor |
| GET | `/api/doctors?specialization=` | Search doctors | Patient |
| POST | `/api/appointments` | Book appointment | Patient |
| PUT | `/api/appointments/{id}` | Reschedule/Cancel | Patient |
| GET | `/api/patients/{id}/records` | View medical records | Patient |
| POST | `/api/payments` | Make payment | Patient |
| GET | `/api/doctors/{id}/appointments` | View upcoming | Doctor |
| POST | `/api/medical-records` | Upload document | Doctor |
| GET | `/api/conversations/{id}/messages` | Get messages | Both |
| POST | `/api/conversations/{id}/messages` | Send message | Both |

---

## Development Scope

### Backend (Spring Boot)
- [ ] User/Patient/Doctor models + JPA entities
- [ ] JWT Authentication (register + login)
- [ ] Appointment booking + management
- [ ] Medical records upload + view
- [ ] Payment integration (eSewa + Khalti)
- [ ] Email/SMS notifications
- [ ] Real-time chat (WebSocket)
- [ ] Doctor dashboard APIs

### Frontend (React + TypeScript)
- [ ] Login + Register pages
- [ ] Patient dashboard
- [ ] Doctor dashboard
- [ ] Book appointment flow
- [ ] Medical records viewer
- [ ] Payment page
- [ ] Real-time chat UI

---

## Design Patterns Used

| Pattern | Where |
|---|---|
| Singleton | Spring Beans |
| Factory | Payment factory (eSewa/Khalti) |
| Observer | Notification on appointment booking |
| Strategy | Payment method selection |
| Layered Architecture | Controller → Service → Repository → DB |

---

## What to Do Next

1. Enable Lombok annotation processing in IntelliJ
   - File → Settings → Build → Compiler → Annotation Processors → ✅ Enable
2. Start writing backend code in this order:
   - User entity (model)
   - Patient entity
   - Doctor entity
   - UserRepository
   - RegisterRequest DTO
   - AuthService
   - AuthController
   - Test with curl/Postman
3. Set up JWT security config
4. Build remaining APIs
5. Start React frontend pages

---

## Constraints

1. No Admin actor — not in case study
2. Patients self-register — doctors registered by clinic
3. eSewa and Khalti are payment methods — not actors
4. All entities must map to domain classes above
5. Follow layered architecture strictly

---

*Handover prepared after: environment setup, project generation, GitHub push.*
*Next task: Write User/Patient model and Registration API.*
