package com.medikiosk.datastructures;

import com.medikiosk.models.Assessment;
import com.medikiosk.models.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatientQueueTest {

    private PatientQueue patientQueue;

    @BeforeEach
    public void setup() {
        patientQueue = new PatientQueue();
    }

    @Test
    public void testPrioritySorting() throws InterruptedException {
        Patient p1 = new Patient(1, "John", 30, "123", "Unknown", "Unknown", "None", "English");
        Assessment a1 = new Assessment(1, Arrays.asList("Cough"));
        a1.setPriorityTier("Green");

        Patient p2 = new Patient(2, "Jane", 25, "123", "Unknown", "Unknown", "None", "English");
        Assessment a2 = new Assessment(2, Arrays.asList("Chest Pain"));
        a2.setPriorityTier("Red");

        Patient p3 = new Patient(3, "Bob", 40, "123", "Unknown", "Unknown", "None", "English");
        Assessment a3 = new Assessment(3, Arrays.asList("Fever"));
        a3.setPriorityTier("Yellow");

        // Enqueue out of order
        patientQueue.enqueue(new QueueEntry(p1, a1));
        Thread.sleep(10); // Ensure time difference
        patientQueue.enqueue(new QueueEntry(p2, a2));
        Thread.sleep(10);
        patientQueue.enqueue(new QueueEntry(p3, a3));

        // Dequeue should be Red, Yellow, Green
        QueueEntry first = patientQueue.dequeue();
        assertEquals("Red", first.getAssessment().getPriorityTier());
        assertEquals("Jane", first.getPatient().getName());

        QueueEntry second = patientQueue.dequeue();
        assertEquals("Yellow", second.getAssessment().getPriorityTier());
        assertEquals("Bob", second.getPatient().getName());

        QueueEntry third = patientQueue.dequeue();
        assertEquals("Green", third.getAssessment().getPriorityTier());
        assertEquals("John", third.getPatient().getName());
    }
    
    @Test
    public void testArrivalTimeSortingSameTier() throws InterruptedException {
        Patient p1 = new Patient(1, "John", 30, "123", "Unknown", "Unknown", "None", "English");
        Assessment a1 = new Assessment(1, Arrays.asList("Cough"));
        a1.setPriorityTier("Yellow");

        Patient p2 = new Patient(2, "Jane", 25, "123", "Unknown", "Unknown", "None", "English");
        Assessment a2 = new Assessment(2, Arrays.asList("Fever"));
        a2.setPriorityTier("Yellow");

        patientQueue.enqueue(new QueueEntry(p1, a1));
        Thread.sleep(10); // John arrived first
        patientQueue.enqueue(new QueueEntry(p2, a2));

        QueueEntry first = patientQueue.dequeue();
        assertEquals("John", first.getPatient().getName());

        QueueEntry second = patientQueue.dequeue();
        assertEquals("Jane", second.getPatient().getName());
    }
}
