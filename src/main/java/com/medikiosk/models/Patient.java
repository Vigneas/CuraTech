package com.medikiosk.models;

public class Patient {
    private int id;
    private String name;
    private int age;
    private String contactInfo;
    private String gender;
    private String bloodType;
    private String allergies;
    private String preferredLanguage;

    public Patient() {}

    public Patient(String name, int age, String contactInfo, String gender, String bloodType, String allergies, String preferredLanguage) {
        this.name = name;
        this.age = age;
        this.contactInfo = contactInfo;
        this.gender = gender;
        this.bloodType = bloodType;
        this.allergies = allergies;
        this.preferredLanguage = preferredLanguage;
    }

    // For loading from DB
    public Patient(int id, String name, int age, String contactInfo, String gender, String bloodType, String allergies, String preferredLanguage) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.contactInfo = contactInfo;
        this.gender = gender;
        this.bloodType = bloodType;
        this.allergies = allergies;
        this.preferredLanguage = preferredLanguage;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }

    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
}
