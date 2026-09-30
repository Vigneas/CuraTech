package com.medikiosk.datastructures;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;

public class PatientQueue {
    private PriorityQueue<QueueEntry> queue;

    public PatientQueue() {
        // Comparator: priority tier first, then arrival time
        // Priority mapped: Red = 3, Yellow = 2, Green = 1
        queue = new PriorityQueue<>(new Comparator<QueueEntry>() {
            @Override
            public int compare(QueueEntry e1, QueueEntry e2) {
                int p1 = getTierValue(e1.getAssessment().getPriorityTier());
                int p2 = getTierValue(e2.getAssessment().getPriorityTier());
                
                if (p1 != p2) {
                    return Integer.compare(p2, p1); // Higher tier first (descending)
                } else {
                    // Same tier, sort by arrival time (ascending)
                    return e1.getAssessment().getArrivalTime().compareTo(e2.getAssessment().getArrivalTime());
                }
            }
            
            private int getTierValue(String tier) {
                if (tier == null) return 1;
                switch (tier.toUpperCase()) {
                    case "RED": return 3;
                    case "YELLOW": return 2;
                    case "GREEN": return 1;
                    default: return 1;
                }
            }
        });
    }

    public void enqueue(QueueEntry entry) {
        queue.offer(entry);
    }

    public QueueEntry dequeue() {
        return queue.poll();
    }

    public QueueEntry peek() {
        return queue.peek();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    public int size() {
        return queue.size();
    }

    // For the UI to display the queue
    public List<QueueEntry> getSnapshot() {
        // PriorityQueue doesn't guarantee iteration order, so we must drain or sort
        List<QueueEntry> snapshot = new ArrayList<>(queue);
        snapshot.sort(queue.comparator());
        return snapshot;
    }
}
