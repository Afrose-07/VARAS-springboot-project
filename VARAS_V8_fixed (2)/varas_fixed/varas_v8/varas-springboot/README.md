# VARAS - Vehicle Accident Response System
## Spring Boot + SQLite + REST API + JPA + Thymeleaf HTML

### District: TIRUNELVELI

---

## Tech Stack
| Component | Technology |
|-----------|-----------|
| Backend   | Spring Boot 3.2 |
| ORM       | Spring Data JPA (Hibernate) |
| Database  | SQLite (via sqlite-jdbc + hibernate-community-dialects) |
| Build     | Maven |
| Frontend  | Thymeleaf HTML + Vanilla JS |
| REST API  | Spring MVC REST Controllers |
| Exception | Global Exception Handler (@RestControllerAdvice) |

---

## How to Run

### Prerequisites
- Java 17+
- Maven 3.6+

### Steps
```bash
# 1. Navigate to project root
cd varas-springboot

# 2. Build
mvn clean package -DskipTests

# 3. Run
java -jar target/varas-1.0.0.jar

# 4. Open browser
http://localhost:8080
```

The SQLite database file `varas.db` is created automatically in the working directory.

---

## Default Credentials
| Role     | Username | Password |
|----------|----------|----------|
| Police   | police   | 123      |
| Hospital | hospital | 123      |
| Admin    | admin    | 123      |

---

## REST API Endpoints

### Public
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/pincode/{pincode}/villages` | Get villages by pincode |
| POST | `/api/accidents/report` | Report a new accident |

### Police
| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/police/login` | Police login |
| GET | `/api/police/cases` | Get all cases |
| POST | `/api/police/accept/{caseId}` | Accept a case |

### Hospital
| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/hospital/login` | Hospital login |
| GET | `/api/hospital/cases` | Get all cases |
| POST | `/api/hospital/accept/{caseId}` | Accept a case |

### Admin
| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/admin/login` | Admin login |
| GET | `/api/admin/accidents` | All accidents |
| GET | `/api/admin/statistics` | Statistics |
| GET | `/api/admin/search/date/{date}` | Search by date (DD/MM/YYYY) |
| GET | `/api/admin/search/pincode/{pincode}` | Search by pincode |
| GET | `/api/admin/search/case/{caseId}` | Search by case ID |
| GET | `/api/admin/search/severity/{severity}` | Search by severity |
| GET | `/api/admin/police-stations` | All police stations |
| GET | `/api/admin/hospitals` | All hospitals |

---

## Original Functionalities Preserved
- Vehicle number validation (TN72XXXX format)
- Tirunelveli district pincode validation (50+ pincodes)
- Village selection based on pincode
- Nearby police & hospital auto-assignment
- Duplicate accident detection (same vehicle + pincode + village)
- Case ID format: TIRUNELVELI{counter} (starts at 101)
- Police accept case (dispatches police, notifies others)
- Hospital accept case (dispatches ambulance, notifies others)
- Admin: view all reports, statistics, search by date/pincode/case ID/severity
- Admin: view all police stations & hospitals listing
- Severity levels: low / medium / high
- Status tracking: Awaiting → Police Dispatched / Ambulance Dispatched / Both Dispatched

---

## Exception Handling
- `AccidentNotFoundException` → 404
- `DuplicateAccidentException` → 409
- `InvalidCredentialsException` → 401
- `InvalidPincodeException` → 400
- `CaseAlreadyAcceptedException` → 409
- Validation errors → 400
- Generic errors → 500
