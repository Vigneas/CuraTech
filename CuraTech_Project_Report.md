# CuraTech Java Port - Project Report

## 1. Introduction
The **CuraTech** project is an outpatient intake and triage platform, originally conceptualized as a web application, and now ported into a strict, monolithic Java-based architecture. This port was executed as a university Mini Project, designed to showcase fundamental and advanced core Java concepts, including Object-Oriented Programming (OOP), Data Structures, rule-based AI logic, and pure JDBC database integration.

## 2. System Architecture
The application adheres to a Model-View-Controller (MVC) architectural pattern, keeping the data handling, user interface, and application logic cleanly separated.
- **Frontend (View):** Built entirely with **JavaFX**, providing a rich desktop graphical user interface. The UI is styled with custom CSS (`styles.css`) for a modern, responsive feel.
- **Backend (Controller & Services):** Written in **Core Java**. It processes user inputs, updates the model, and bridges the gap between the database and the UI.
- **Data Layer (Model & DAO):** Uses **raw JDBC** to communicate with a local **MariaDB** (MySQL drop-in) database instance. The Data Access Objects (DAOs) encapsulate all SQL queries.

## 3. Core Components & Workflow

### 3.1 Patient Intake & Registration (Patient Kiosk)
- **Workflow:** When a patient arrives, they interact with the Patient Kiosk UI to register their demographic details (name, age, contact, etc.) and select their current symptoms from a predefined list.
- **Implementation:** The `PatientController` captures this data. A `Patient` object and an `Assessment` object are instantiated and passed to the `KioskService`. The service then calls `PatientDAO` to insert the records into the database via JDBC.

### 3.2 Automated Triage Engine (AI Logic)
- **Workflow:** Once symptoms are submitted, the system must immediately decide how urgent the patient's condition is.
- **Implementation:** The `TriageEngine` (our rule-based Expert System) analyzes the symptoms. It calculates a severity score based on medical rules (e.g., "Chest Pain" or "Difficulty Breathing" yields a high score). Depending on the score, the engine assigns a Priority Tier: **Red (Emergency)**, **Yellow (Urgent)**, or **Green (Normal)**.

### 3.3 Custom Data Structures (Patient Queue)
- **Workflow:** Assessed patients need to be queued for the physician in a way that respects their triage urgency.
- **Implementation:** A custom `PatientQueue` data structure is utilized. It heavily relies on Java Collections (like a `PriorityQueue` with a custom `Comparator`) to ensure that:
  1. Patients with a higher severity score (Red tier) are always placed at the front.
  2. If two patients have the same priority tier, the one with the earlier arrival time is served first.

### 3.4 Physician Workstation
- **Workflow:** The physician logs into their dashboard and sees a live, auto-sorted list of waiting patients. They can "Call Next Patient", which dequeues the highest-priority individual.
- **Implementation:** The `PhysicianController` manages a JavaFX `TableView` populated by the `PatientQueue`. When the physician marks a consultation as complete, they input diagnosis and prescription notes, which are then saved back to the database, and the patient is removed from the active queue.

### 3.5 Administrative Dashboard
- **Workflow:** Hospital administrators can monitor the live outpatient registry, tracking the total number of waiting patients across all priority tiers and viewing a historical log of completed consultations.
- **Implementation:** The `AdminController` queries the `PatientDAO` for aggregate metrics (using SQL `GROUP BY` clauses) and displays them in real-time KPI cards.

## 4. Codebase Deep-Dive (How the Code Works)

The application is structured into clearly defined packages to maintain separation of concerns:

### 4.1 Models (`com.medikiosk.models`)
- **`Patient`**: A standard Java Object (POJO) that encapsulates the patient's demographic information (name, age, contact info, blood type, etc.).
- **`Assessment`**: Holds the clinical data associated with a patient's visit. It stores the reported symptoms, the calculated severity score, the assigned priority tier, and the final diagnosis provided by the physician.

### 4.2 Application Logic & AI (`com.medikiosk.ai` & `com.medikiosk.services`)
- **`TriageEngine`**: The core AI logic component. It uses a rule-based algorithm (a series of `if/else` checks against specific medical keywords) to evaluate the symptoms in an `Assessment`. It assigns a numeric severity score (e.g., +10 for "Chest Pain", +5 for "Fever") and dictates the Priority Tier based on predefined thresholds.
- **`KioskService`**: The main business logic layer. It acts as the central coordinator between the UI, the AI Engine, the Data Structures, and the Database. For example, when a patient registers, the `KioskService` invokes the `TriageEngine`, adds the patient to the active queue, and calls the database to persist the records.

### 4.3 Custom Data Structures (`com.medikiosk.datastructures`)
- **`PatientQueue` & `QueueEntry`**: Instead of relying on a standard database query to determine who the physician sees next, the application maintains a live, in-memory Priority Queue. 
  - `QueueEntry` combines a `Patient` and their `Assessment` into a single wrapper.
  - `PatientQueue` uses a custom Java `Comparator` that enforces strict medical sorting rules: it first compares the `PriorityTier` (Emergency > Urgent > Normal), then the `SeverityScore` (higher is prioritized), and finally the `ArrivalTime` (first-come, first-served for ties). 

### 4.4 Data Access Layer (`com.medikiosk.dao`)
- **`DatabaseConnection`**: Manages the lifecycle of JDBC Connections using the Singleton pattern. It is responsible for loading the MySQL JDBC driver and supplying active connections to other classes.
- **`PatientDAO`**: The Data Access Object. It contains all the raw SQL statements (`INSERT`, `SELECT`, `UPDATE`) needed to save new patient assessments, mark consultations as complete, and fetch administrative metrics. It strictly uses `PreparedStatement` to prevent SQL injection attacks.

### 4.5 User Interface (`com.medikiosk.ui`)
- **JavaFX Controllers**: `PatientController`, `PhysicianController`, and `AdminController` manage the views. They use JavaFX's layout managers (`VBox`, `HBox`, `GridPane`) and bind data to UI components (like `TableView`) using `FXCollections.observableArrayList`, ensuring the screens automatically reflect the underlying data state.

## 5. Environment & Database Setup
To ensure a smooth, portable experience without requiring manual software installation:
- **Portable Database:** The project includes a bundled, portable MariaDB environment.
- **Launcher Script:** A `run_app.bat` script handles the lifecycle of the application. It automatically spins up the MariaDB server in the background, launches the JavaFX application via Maven, and gracefully shuts down the database when the application exits.

## 5. Conclusion
The MediKiosk Java port successfully implements a robust, end-to-end hospital triage system. By utilizing JavaFX for the UI, a custom-built rule engine for triage, Priority Queues for patient sorting, and raw JDBC for data persistence, the project deeply aligns with core Computer Science and Software Engineering principles.
