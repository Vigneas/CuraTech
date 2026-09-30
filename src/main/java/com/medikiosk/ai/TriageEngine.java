package com.medikiosk.ai;

import java.util.List;

public class TriageEngine {

    // Simple rule-based heuristics
    public int calculateSeverityScore(List<String> symptoms) {
        int score = 0;
        if (symptoms == null || symptoms.isEmpty()) {
            return score;
        }

        for (String symptom : symptoms) {
            String s = symptom.toLowerCase();
            if (s.contains("chest pain") || s.contains("unconscious") || s.contains("severe bleeding")) {
                score += 10;
            } else if (s.contains("difficulty breathing") || s.contains("high fever")) {
                score += 5;
            } else if (s.contains("fracture") || s.contains("vomiting") || s.contains("dizzy")) {
                score += 3;
            } else {
                score += 1;
            }
        }
        return score;
    }

    public String determinePriorityTier(int score) {
        if (score >= 10) {
            return "Red"; // Emergency
        } else if (score >= 4) {
            return "Yellow"; // Urgent
        } else {
            return "Green"; // Normal
        }
    }
}
