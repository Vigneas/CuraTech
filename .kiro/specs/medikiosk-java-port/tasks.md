# Implementation Plan

## Task Format Template

- [x] 1. Foundation: Models, Data Structures, and Database Schema
- [x] 1.1 Implement Domain Models and initialize MySQL schema
  - Create `Patient` and `Assessment` Java domain models
  - Create `DatabaseConnection` singleton for JDBC
  - Execute DDL to create `patients` and `assessments` tables in MySQL
  - _Observable completion_: A test query successfully connects to the DB and returns 0 rows.
  - _Requirements: 1.1, 2.1_
  - _Boundary: models, dao_

- [x] 1.2 Implement Custom PatientQueue
  - Create `PatientQueue` wrapping a priority queue
  - Implement sorting logic based on priority tier (Red, Yellow, Green) and arrival time
  - _Observable completion_: Unit test verifies elements are dequeued in correct priority order.
  - _Requirements: 2.3, 3.1, 3.2_
  - _Boundary: datastructures_

- [x] 2. Core Feature: Triage Engine
- [x] 2.1 Implement Rule-Based TriageEngine
  - Create `TriageEngine` class with `calculateSeverityScore` method
  - Map specific symptoms to severity thresholds
  - Create `determinePriorityTier` mapping scores to "Red", "Yellow", "Green"
  - _Observable completion_: Unit tests pass for critical, urgent, and normal symptom combinations.
  - _Requirements: 2.1, 2.2, 2.4_
  - _Boundary: ai_

- [x] 3. Core Feature: Data Access
- [x] 3.1 Implement PatientDAO
  - Implement JDBC inserts for new patients and assessments
  - Implement select queries to load patients and metrics
  - Implement update queries to mark assessments as completed
  - _Observable completion_: Integration test can insert and retrieve a patient record successfully.
  - _Requirements: 1.1, 1.2, 3.3, 4.2_
  - _Boundary: dao_

- [x] 4. Integration: Application Service
- [x] 4.1 Implement KioskService
  - Coordinate `PatientDAO`, `PatientQueue`, and `TriageEngine`
  - Implement `registerPatient`, `getLivePhysicianQueue`, `completeConsultation`, and `getAdminMetrics`
  - _Observable completion_: Calling `registerPatient` with severe symptoms successfully persists data, queries the AI, and adds it to the queue top.
  - _Depends: 1.2, 2.1, 3.1_
  - _Requirements: 1.1, 1.2, 2.1, 2.2, 2.3, 2.4, 3.1, 3.2, 3.3, 4.1, 4.2_
  - _Boundary: services_

- [x] 5. Core Feature: JavaFX Frontend
- [x] 5.1 (P) Build Patient Intake View
  - Create `PatientController` and corresponding UI layout
  - Add form fields for demographic data and symptoms checkboxes
  - Wire submit button to `KioskService.registerPatient`
  - Include validation for mandatory fields
  - _Observable completion_: UI form successfully invokes the backend service and clears on success, or shows an alert on missing fields.
  - _Requirements: 1.1, 1.2, 1.3_
  - _Boundary: ui_

- [x] 5.2 (P) Build Physician Workstation View
  - Create `PhysicianController` and layout
  - Add `ListView` or `TableView` bound to `KioskService.getLivePhysicianQueue()`
  - Add "Mark Complete" button interacting with `KioskService`
  - _Observable completion_: UI list displays mocked data in correct priority order and removes items when marked complete.
  - _Requirements: 3.1, 3.2, 3.3_
  - _Boundary: ui_

- [x] 5.3 (P) Build Admin Dashboard View
  - Create `AdminController` and layout
  - Display counts of waiting patients by tier
  - Display summary of completed consultations
  - _Observable completion_: UI labels render the correct aggregated metrics from `KioskService`.
  - _Requirements: 4.1, 4.2_
  - _Boundary: ui_
