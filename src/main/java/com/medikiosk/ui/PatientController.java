package com.medikiosk.ui;

import com.medikiosk.models.Assessment;
import com.medikiosk.models.Patient;
import com.medikiosk.services.KioskService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.sql.SQLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PatientController {
    private KioskService service;
    private VBox view;

    private TextField nameField;
    private TextField ageField;
    private TextField contactField;
    private ComboBox<String> genderBox;
    private ComboBox<String> bloodTypeBox;
    private ComboBox<String> languageBox;
    private TextField allergiesField;
    private CheckBox chestPainBox, feverBox, breathingBox, fractureBox, coughBox;

    // UI Labels for translation
    private Label titleLbl;
    private Label subtitleLbl;
    private Label nameLbl;
    private Label ageLbl;
    private Label contactLbl;
    private Label genderLbl;
    private Label bloodTypeLbl;
    private Label allergiesLbl;
    private Label languageLbl;
    private Label symptomsLabel;
    private Button submitBtn;
    private MediaPlayer mediaPlayer;

    public PatientController(KioskService service) {
        this.service = service;
        buildView();
    }

    private void buildView() {
        view = new VBox(20);
        view.setPadding(new Insets(40));
        view.setStyle("-fx-alignment: center;"); // Center the card on screen

        VBox card = new VBox(20);
        card.getStyleClass().add("card");
        card.setMaxWidth(600);

        titleLbl = new Label("Patient Intake Form");
        titleLbl.getStyleClass().add("title-label");
        
        subtitleLbl = new Label("Please fill out your demographic details and select any symptoms you are experiencing.");
        subtitleLbl.getStyleClass().add("subtitle-label");

        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(15);

        nameField = new TextField();
        nameField.getStyleClass().add("text-field");
        
        ageField = new TextField();
        ageField.getStyleClass().add("text-field");
        
        contactField = new TextField();
        contactField.getStyleClass().add("text-field");

        nameLbl = new Label("Full Name");
        nameLbl.setStyle("-fx-font-weight: bold;");
        ageLbl = new Label("Age");
        ageLbl.setStyle("-fx-font-weight: bold;");
        contactLbl = new Label("Contact Info");
        contactLbl.setStyle("-fx-font-weight: bold;");
        
        genderBox = new ComboBox<>();
        genderBox.setMaxWidth(Double.MAX_VALUE);
        
        bloodTypeBox = new ComboBox<>(FXCollections.observableArrayList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-", "Unknown"));
        bloodTypeBox.setMaxWidth(Double.MAX_VALUE);
        
        allergiesField = new TextField();
        allergiesField.getStyleClass().add("text-field");
        
        allergiesLbl = new Label("Allergies");
        allergiesLbl.setStyle("-fx-font-weight: bold;");

        grid.add(nameLbl, 0, 0);
        grid.add(nameField, 0, 1);
        grid.add(ageLbl, 1, 0);
        grid.add(ageField, 1, 1);
        grid.add(contactLbl, 0, 2);
        grid.add(contactField, 0, 3);
        
        genderLbl = new Label("Gender");
        grid.add(genderLbl, 1, 2);
        grid.add(genderBox, 1, 3);
        
        bloodTypeLbl = new Label("Blood Type");
        grid.add(bloodTypeLbl, 0, 4);
        grid.add(bloodTypeBox, 0, 5);
        
        grid.add(allergiesLbl, 1, 4);
        grid.add(allergiesField, 1, 5);

        languageLbl = new Label("Preferred Language");
        languageLbl.setStyle("-fx-font-weight: bold;");
        languageBox = new ComboBox<>(FXCollections.observableArrayList("English", "Hindi", "Tamil", "Telugu", "Malayalam"));
        languageBox.setMaxWidth(Double.MAX_VALUE);
        languageBox.setOnAction(e -> {
            String lang = languageBox.getValue();
            if (lang != null) updateUIText(lang);
        });
        
        grid.add(languageLbl, 0, 6);
        grid.add(languageBox, 0, 7);

        symptomsLabel = new Label("Select Symptoms");
        symptomsLabel.setStyle("-fx-font-weight: bold;");
        
        chestPainBox = new CheckBox("Chest Pain");
        feverBox = new CheckBox("High Fever");
        breathingBox = new CheckBox("Difficulty Breathing");
        fractureBox = new CheckBox("Fracture");
        coughBox = new CheckBox("Cough / Cold");

        VBox symptomsBox = new VBox(10, chestPainBox, feverBox, breathingBox, fractureBox, coughBox);
        symptomsBox.setStyle("-fx-padding: 10px; -fx-background-color: #f9fafb; -fx-background-radius: 8px; -fx-border-color: #e5e7eb; -fx-border-radius: 8px;");

        submitBtn = new Button("Submit Registration");
        submitBtn.getStyleClass().add("primary-button");
        submitBtn.setMaxWidth(Double.MAX_VALUE);
        submitBtn.setOnAction(e -> handleRegistration());

        card.getChildren().addAll(titleLbl, subtitleLbl, grid, symptomsLabel, symptomsBox, submitBtn);
        view.getChildren().add(card);
        
        setupTTSListeners();
        
        // Initialize with default English
        updateUIText("English");
        languageBox.setValue("English");
    }

    private void updateUIText(String lang) {
        titleLbl.setText(Translator.get("title", lang));
        subtitleLbl.setText(Translator.get("subtitle", lang));
        nameLbl.setText(Translator.get("name", lang));
        ageLbl.setText(Translator.get("age", lang));
        contactLbl.setText(Translator.get("contact", lang));
        genderLbl.setText(Translator.get("gender", lang));
        bloodTypeLbl.setText(Translator.get("blood", lang));
        allergiesLbl.setText(Translator.get("allergies", lang));
        languageLbl.setText(Translator.get("language", lang));
        symptomsLabel.setText(Translator.get("symptoms", lang));
        submitBtn.setText(Translator.get("submit", lang));

        // Dropdown prompts
        genderBox.setPromptText(Translator.get("gender", lang));
        bloodTypeBox.setPromptText(Translator.get("blood", lang));
        languageBox.setPromptText(Translator.get("language", lang));
        
        // Text field prompts
        nameField.setPromptText("E.g. John Doe");
        ageField.setPromptText("E.g. 35");
        contactField.setPromptText("E.g. 555-0199");
        allergiesField.setPromptText(Translator.get("allergies", lang) + " (Optional)");

        // Update gender options
        genderBox.setItems(FXCollections.observableArrayList(
            Translator.get("male", lang),
            Translator.get("female", lang),
            Translator.get("other", lang),
            Translator.get("prefer_not", lang)
        ));

        // Symptoms Checkboxes
        chestPainBox.setText(Translator.get("chest_pain", lang));
        feverBox.setText(Translator.get("fever", lang));
        breathingBox.setText(Translator.get("breathing", lang));
        fractureBox.setText(Translator.get("fracture", lang));
        coughBox.setText(Translator.get("cough", lang));
    }

    private void handleRegistration() {
        if (nameField.getText().isEmpty() || ageField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Name and Age are mandatory.");
            return;
        }

        try {
            int age = Integer.parseInt(ageField.getText());
            
            String lang = languageBox.getValue() != null ? languageBox.getValue() : "English";
            String rawGender = genderBox.getValue();
            String englishGender = Translator.getEnglishGender(rawGender, lang);
            String blood = bloodTypeBox.getValue() != null ? bloodTypeBox.getValue() : "Unknown";
            
            Patient patient = new Patient(nameField.getText(), age, contactField.getText(), englishGender, blood, allergiesField.getText(), lang);
            
            List<String> symptoms = new ArrayList<>();
            // The AI engine requires exact English strings!
            if (chestPainBox.isSelected()) symptoms.add("Chest Pain");
            if (feverBox.isSelected()) symptoms.add("High Fever");
            if (breathingBox.isSelected()) symptoms.add("Difficulty Breathing");
            if (fractureBox.isSelected()) symptoms.add("Fracture");
            if (coughBox.isSelected()) symptoms.add("Cough / Cold");

            Assessment assessment = new Assessment(0, symptoms); // 0 placeholder for patientId
            assessment.setDiagnosis("");
            assessment.setPrescriptionNotes("");

            service.registerPatient(patient, assessment);
            
            showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful. You are now in the queue.");
            clearForm();

        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Age must be a number.");
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to register: " + ex.getMessage());
        }
    }

    private void setupTTSListeners() {
        nameField.focusedProperty().addListener((obs, o, n) -> { if (n) playTTS(formatTTS(nameLbl.getText(), true)); });
        ageField.focusedProperty().addListener((obs, o, n) -> { if (n) playTTS(formatTTS(ageLbl.getText(), true)); });
        contactField.focusedProperty().addListener((obs, o, n) -> { if (n) playTTS(formatTTS(contactLbl.getText(), true)); });
        allergiesField.focusedProperty().addListener((obs, o, n) -> { if (n) playTTS(formatTTS(allergiesLbl.getText(), true)); });

        genderBox.focusedProperty().addListener((obs, o, n) -> { if (n) playTTS(formatTTS(genderLbl.getText(), false)); });
        bloodTypeBox.focusedProperty().addListener((obs, o, n) -> { if (n) playTTS(formatTTS(bloodTypeLbl.getText(), false)); });
        languageBox.focusedProperty().addListener((obs, o, n) -> { if (n) playTTS(formatTTS(languageLbl.getText(), false)); });
        
        chestPainBox.focusedProperty().addListener((obs, o, n) -> { if (n) playTTS(chestPainBox.getText()); });
        feverBox.focusedProperty().addListener((obs, o, n) -> { if (n) playTTS(feverBox.getText()); });
        breathingBox.focusedProperty().addListener((obs, o, n) -> { if (n) playTTS(breathingBox.getText()); });
        fractureBox.focusedProperty().addListener((obs, o, n) -> { if (n) playTTS(fractureBox.getText()); });
        coughBox.focusedProperty().addListener((obs, o, n) -> { if (n) playTTS(coughBox.getText()); });
        
        submitBtn.focusedProperty().addListener((obs, o, n) -> { if (n) playTTS(submitBtn.getText()); });
    }

    private String formatTTS(String labelText, boolean isInput) {
        String lang = languageBox.getValue();
        if (lang == null) lang = "English";
        if ("English".equals(lang)) {
            return isInput ? "Please enter " + labelText : "Please select " + labelText;
        }
        return labelText;
    }

    private void playTTS(String textToRead) {
        if (textToRead == null || textToRead.trim().isEmpty()) return;
        
        String lang = languageBox.getValue();
        if (lang == null) lang = "English";
        
        String langCode = switch (lang) {
            case "Hindi" -> "hi";
            case "Tamil" -> "ta";
            case "Telugu" -> "te";
            case "Malayalam" -> "ml";
            default -> "en";
        };
        
        new Thread(() -> {
            try {
                String urlString = "https://translate.google.com/translate_tts?ie=UTF-8&q=" + 
                             URLEncoder.encode(textToRead, StandardCharsets.UTF_8.toString()) + 
                             "&tl=" + langCode + "&client=tw-ob";
                
                java.net.URL url = new java.net.URL(urlString);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                
                java.io.File tempFile = java.io.File.createTempFile("tts_", ".mp3");
                tempFile.deleteOnExit();
                
                try (java.io.InputStream in = conn.getInputStream();
                     java.io.FileOutputStream out = new java.io.FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
                
                javafx.application.Platform.runLater(() -> {
                    try {
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                        }
                        Media media = new Media(tempFile.toURI().toString());
                        mediaPlayer = new MediaPlayer(media);
                        mediaPlayer.play();
                    } catch (Exception ex) {
                        System.err.println("Error playing audio: " + ex.getMessage());
                    }
                });
            } catch (Exception ex) {
                System.err.println("Error fetching audio: " + ex.getMessage());
            }
        }).start();
    }

    private void clearForm() {
        nameField.clear();
        ageField.clear();
        contactField.clear();
        genderBox.setValue(null);
        bloodTypeBox.setValue(null);
        allergiesField.clear();
        chestPainBox.setSelected(false);
        feverBox.setSelected(false);
        breathingBox.setSelected(false);
        fractureBox.setSelected(false);
        coughBox.setSelected(false);
        
        // Reset to English after submission
        languageBox.setValue("English");
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public VBox getView() {
        return view;
    }
}
