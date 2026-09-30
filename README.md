# MediKiosk Java — AI-Powered Clinical Intake & Triage

**MediKiosk** is an enterprise-grade hospital outpatient intake and triage platform. Originally conceptualized as a web application, this repository contains the strict, monolithic Java-based port. 

This system was designed to handle patient registration, automatically assess symptom urgency via a rule-based AI engine, and queue patients for physicians based on medical priority.

## 🚀 Features

- **Patient Intake Kiosk:** A streamlined GUI for patients to register their demographics and submit symptoms.
- **Automated Triage Engine (AI):** A rule-based Expert System that evaluates symptoms to calculate a severity score and assign a priority tier: **Red (Emergency)**, **Yellow (Urgent)**, or **Green (Normal)**.
- **Priority Patient Queueing:** Uses custom Java data structures (Priority Queues & custom Comparators) to strictly enforce medical sorting rules, ensuring critical patients are seen first.
- **Physician Workstation:** A dashboard for doctors to dequeue patients, log consultations, and record diagnoses and prescriptions.
- **Administrative Dashboard:** Real-time KPIs and live outpatient registry monitoring for hospital administrators.

## 🛠️ Tech Stack

This project strictly avoids heavy frameworks to showcase fundamental Computer Science and Core Java concepts:
* **Frontend GUI:** JavaFX (styled with custom CSS)
* **Backend:** Core Java 17
* **Architecture:** MVC (Model-View-Controller)
* **Database:** MariaDB (Local portable instance)
* **Data Access:** Pure JDBC (Data Access Objects & Prepared Statements)
* **Build Tool:** Maven

## 📂 Project Structure

```text
src/main/java/com/medikiosk/
├── ai/                 # TriageEngine (Rule-based severity scoring)
├── dao/                # Data Access Objects (JDBC DatabaseConnection & PatientDAO)
├── datastructures/     # Custom PatientQueue & QueueEntry implementations
├── models/             # POJOs (Patient, Assessment)
├── services/           # KioskService (Main Business Logic layer)
├── ui/                 # JavaFX Controllers (Patient, Physician, Admin)
└── Main.java           # Application entry point
```

## ⚙️ Setup & Installation

This project is built to be portable. You do not need to install MySQL/MariaDB on your host machine as it downloads and runs a bundled, portable database environment.

### 1. Prerequisites
- **Java Development Kit (JDK) 17** or higher installed and on your system `PATH`.
- **PowerShell** (for running the setup scripts on Windows).

### 2. Initial Setup
Run the environment setup script. This will download and extract the portable Maven and MariaDB distributions into the project folder.
```powershell
.\setup_env.ps1
```

### 3. Running the Application
Use the provided batch script to launch the application. This script will automatically start the MariaDB database in the background, run the Maven build, launch the JavaFX application, and gracefully shut down the database when you exit the app.
```cmd
run_app.bat
```

## 🧠 How the Code Works (Deep Dive)

1. **The Triage Engine (`com.medikiosk.ai.TriageEngine`)**
   Uses a rule-based algorithm (a series of `if/else` keyword checks) against the symptoms in an `Assessment`. It assigns a numeric severity score (e.g., +10 for "Chest Pain") and dictates the Priority Tier.

2. **The Priority Queue (`com.medikiosk.datastructures.PatientQueue`)**
   Instead of relying on database queries to determine who the physician sees next, we maintain an in-memory Priority Queue. A custom Java `Comparator` enforces sorting: `PriorityTier` first (Emergency > Urgent > Normal), then `SeverityScore`, and finally `ArrivalTime` for ties.

3. **Data Access Layer (`com.medikiosk.dao`)**
   Manages JDBC Connections via the Singleton pattern. All SQL queries (`INSERT`, `SELECT`, `UPDATE`) use `PreparedStatement` to prevent SQL injection attacks.

---
*Built as a showcase for advanced Core Java concepts, Object-Oriented Programming, and Data Structures.*
