# Lalitpur Health Care Management System (LHCMS)

A full-stack web application for managing patient records, appointments, and doctor interactions.

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3.5.0 (Java 21) |
| Frontend | React 18 + TypeScript |
| Database | PostgreSQL 15 |
| Auth | Spring Security + JWT |

## Getting Started

### Prerequisites
- Java 21
- Maven 3.9+
- Node.js 20+
- Docker

### Setup

1. Clone the repository
\`\`\`bash
git clone https://github.com/YOUR_USERNAME/lhcms.git
cd lhcms
\`\`\`

2. Start database
\`\`\`bash
docker compose up -d postgres
\`\`\`

3. Configure backend
\`\`\`bash
cp lhcms-backend/src/main/resources/application.properties.template \
   lhcms-backend/src/main/resources/application.properties
# Edit application.properties with your values
\`\`\`

4. Run backend
\`\`\`bash
cd lhcms-backend
mvn spring-boot:run
\`\`\`

5. Run frontend
\`\`\`bash
cd lhcms-frontend
npm install
npm start
\`\`\`

## Access
| Service | URL |
|---|---|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080 |
