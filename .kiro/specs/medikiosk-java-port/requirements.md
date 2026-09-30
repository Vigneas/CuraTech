# Requirements Document

## Introduction
Port the MediKiosk outpatient intake and triage platform from its original web stack into a strict Java-based architecture for a university Mini Project. The system requires JavaFX for the frontend, Core Java (OOP, Collections, Data Structures) for the backend, a rule-based expert system for AI triage, and MySQL via pure JDBC for the database.

## Boundary Context
- **In scope**: Patient intake forms, automated triage assessment, live priority queues for physicians, and basic admin monitoring.
- **Out of scope**: External API integrations, real-time cloud notifications, or advanced machine learning models (AI is rule-based).
- **Adjacent expectations**: Requires a local database instance to store structured patient and appointment data.

## Requirements

### Requirement 1: Patient Intake & Registration
**Objective:** As a Patient, I want to register my details and input my symptoms, so that I can be assessed by the hospital staff.

#### Acceptance Criteria
1. When the patient submits their basic demographic details, the system shall save a new patient record.
2. When the patient selects a list of their current symptoms, the system shall create an intake assessment record.
3. If mandatory fields are omitted during registration, the system shall display an error message prompting for completion.

### Requirement 2: Automated Triage & Emergency Assessment
**Objective:** As a Triage Officer, I want the system to automatically assess patient symptoms, so that critical cases are prioritized immediately.

#### Acceptance Criteria
1. When an intake assessment is submitted, the system shall calculate a severity score based on the reported symptoms.
2. If the severity score exceeds the emergency threshold, the system shall flag the patient as "Red/Emergency".
3. While a patient is flagged as "Red/Emergency", the system shall place them at the front of the physician queue.
4. When a non-emergency assessment is submitted, the system shall place the patient at the end of their respective priority tier.

### Requirement 3: Physician Live Queue
**Objective:** As a Physician, I want to see a live list of patients ordered by priority, so that I can treat the most urgent cases first.

#### Acceptance Criteria
1. The system shall display the patient queue ordered strictly by triage priority (highest severity first) and then by arrival time.
2. When a new patient is added to the queue, the system shall update the list immediately to reflect their priority position.
3. When the physician marks a patient as "Consultation Complete", the system shall remove the patient from the active queue and update their record.

### Requirement 4: Administrative Dashboard
**Objective:** As an Administrator, I want to monitor the live outpatient registry, so that I can oversee hospital operations.

#### Acceptance Criteria
1. The system shall display the total number of waiting patients and their triage distribution (Emergency, Urgent, Normal).
2. When the administrator views the dashboard, the system shall provide a summary of the day's completed consultations.
