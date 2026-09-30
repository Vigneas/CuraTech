package com.medikiosk.ui;

import com.medikiosk.dao.PatientDAO;
import com.medikiosk.services.KioskService;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import com.medikiosk.datastructures.QueueEntry;
import java.util.List;

import java.sql.SQLException;

public class AdminController {
    private KioskService service;
    private VBox view;

    private Label redCountLbl = new Label("0");
    private Label yellowCountLbl = new Label("0");
    private Label greenCountLbl = new Label("0");
    private Label completedCountLbl = new Label("0");
    private Label errorLbl = new Label();
    private TableView<QueueEntry> historyTable;

    public AdminController(KioskService service) {
        this.service = service;
        buildView();
    }

    private void buildView() {
        view = new VBox(30);
        view.setPadding(new Insets(40));

        Label title = new Label("Hospital Administrator Dashboard");
        title.getStyleClass().add("title-label");
        
        Label subtitle = new Label("Real-time metrics and triage statistics.");
        subtitle.getStyleClass().add("subtitle-label");

        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setHgap(20);

        // Helper method to create stat cards
        VBox redCard = createStatCard("Emergency (RED)", redCountLbl, "stat-card-red");
        VBox yellowCard = createStatCard("Urgent (YELLOW)", yellowCountLbl, "stat-card-yellow");
        VBox greenCard = createStatCard("Normal (GREEN)", greenCountLbl, "stat-card-green");
        VBox completedCard = createStatCard("Total Completed", completedCountLbl, "");

        grid.add(redCard, 0, 0);
        grid.add(yellowCard, 1, 0);
        grid.add(greenCard, 2, 0);
        grid.add(completedCard, 0, 1, 3, 1);
        
        Label historyTitle = new Label("Recent Completed Consultations");
        historyTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 10px 0 0 0;");
        
        historyTable = new TableView<>();
        historyTable.getStyleClass().add("table-view");
        
        TableColumn<QueueEntry, String> nameCol = createTextColumn("Patient", entry -> entry.getPatient().getName());
        TableColumn<QueueEntry, String> diagnosisCol = createTextColumn("Diagnosis", entry -> entry.getAssessment().getDiagnosis());
        TableColumn<QueueEntry, String> prescriptionCol = createTextColumn("Prescription", entry -> entry.getAssessment().getPrescriptionNotes());
        TableColumn<QueueEntry, String> timeCol = createTextColumn("Arrival Time", entry -> entry.getAssessment().getArrivalTime().toString());

        historyTable.getColumns().add(nameCol);
        historyTable.getColumns().add(diagnosisCol);
        historyTable.getColumns().add(prescriptionCol);
        historyTable.getColumns().add(timeCol);
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        errorLbl.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");

        view.getChildren().addAll(title, subtitle, grid, historyTitle, historyTable, errorLbl);
        refreshDashboard();
    }
    
    private VBox createStatCard(String title, Label valueLabel, String additionalClass) {
        VBox card = new VBox(5);
        card.getStyleClass().add("stat-card");
        if (!additionalClass.isEmpty()) card.getStyleClass().add(additionalClass);
        
        Label titleLbl = new Label(title);
        titleLbl.getStyleClass().add("stat-card-title");
        valueLabel.getStyleClass().add("stat-card-value");
        
        card.getChildren().addAll(titleLbl, valueLabel);
        return card;
    }

    public void refreshDashboard() {
        try {
            PatientDAO.Metrics metrics = service.getAdminMetrics();
            redCountLbl.setText(String.valueOf(metrics.redCount));
            yellowCountLbl.setText(String.valueOf(metrics.yellowCount));
            greenCountLbl.setText(String.valueOf(metrics.greenCount));
            completedCountLbl.setText(String.valueOf(metrics.completedCount));
            
            List<QueueEntry> completedList = service.getCompletedConsultations();
            historyTable.setItems(FXCollections.observableArrayList(completedList));
            
            errorLbl.setText("");
        } catch (SQLException e) {
            errorLbl.setText("Error loading metrics: " + e.getMessage());
        }
    }

    public VBox getView() {
        return view;
    }
    
    private TableColumn<QueueEntry, String> createTextColumn(String title, java.util.function.Function<QueueEntry, String> extractor) {
        TableColumn<QueueEntry, String> col = new TableColumn<>(title);
        col.setCellValueFactory(data -> new ReadOnlyStringWrapper(
            data.getValue() != null ? extractor.apply(data.getValue()) : ""
        ));
        col.setCellFactory(column -> new TableCell<QueueEntry, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
            }
        });
        return col;
    }
}
