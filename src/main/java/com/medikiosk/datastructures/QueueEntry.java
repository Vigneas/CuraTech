package com.medikiosk.datastructures;

import com.medikiosk.models.Assessment;
import com.medikiosk.models.Patient;

public class QueueEntry {
    private Patient patient;
    private Assessment assessment;

    public QueueEntry(Patient patient, Assessment assessment) {
        this.patient = patient;
        this.assessment = assessment;
    }

    public Patient getPatient() {
        return patient;
    }

    public Assessment getAssessment() {
        return assessment;
    }
}
