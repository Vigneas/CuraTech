# Brief: medikiosk-java-port

## Problem
The MediKiosk outpatient intake and triage platform needs to be ported from its original web-based tech stack (React/Node.js) into a strict Java-based architecture for a university Mini Project, avoiding any traces of the original repository.

## Current State
A greenfield Java project. No code has been written yet.

## Desired Outcome
A fully functioning Java application adhering to the PDF syllabus flowchart:
- Frontend: JavaFX
- Backend: Core Java (OOP, Collections, Data Structures)
- AI & Logic: Rule-based Expert System for Triage
- Database: MySQL via raw JDBC

## Approach
Implement the system as a single monolithic desktop JavaFX application. We will use a standard Java directory structure with `src` and `lib` (or basic Maven). The system will feature Patient Kiosk, Physician Workstation, and Admin Dashboard views, connected to a local MySQL instance. 

## Scope
- **In**: JavaFX UI screens (Patient, Physician, Admin), Core Java backend models, Custom Data Structures (Priority Queue for triage), Rule-based AI Engine, JDBC data access layer.
- **Out**: Web APIs, cloud AI integrations (e.g. Gemini), ORMs like Hibernate, complex deployment pipelines.

## Boundary Candidates
- UI Layer (JavaFX Controllers/Views)
- Application Logic & AI (Services, Expert System)
- Data Access Layer (JDBC DAOs)
- Models & Data Structures

## Out of Boundary
- Third-party API integrations (No external LLM calls).

## Upstream / Downstream
- **Upstream**: Local MySQL Database
- **Downstream**: None.

## Existing Spec Touchpoints
- **Extends**: None (New project).
- **Adjacent**: None.

## Constraints
- Must strictly use Core Java and JavaFX.
- Database interaction must be pure JDBC.
- Must demonstrate OOP, Collections, and Data Structures.
