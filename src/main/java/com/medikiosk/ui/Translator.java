package com.medikiosk.ui;

import java.util.HashMap;
import java.util.Map;

public class Translator {
    private static final Map<String, Map<String, String>> dictionary = new HashMap<>();

    static {
        // English (Default)
        add("English", "title", "Patient Intake Form");
        add("English", "subtitle", "Please fill out your demographic details and select any symptoms you are experiencing.");
        add("English", "name", "Full Name");
        add("English", "age", "Age");
        add("English", "contact", "Contact Info");
        add("English", "gender", "Gender");
        add("English", "blood", "Blood Type");
        add("English", "allergies", "Allergies");
        add("English", "language", "Preferred Language");
        add("English", "symptoms", "Select Symptoms");
        add("English", "chest_pain", "Chest Pain");
        add("English", "fever", "High Fever");
        add("English", "breathing", "Difficulty Breathing");
        add("English", "fracture", "Fracture");
        add("English", "cough", "Cough / Cold");
        add("English", "submit", "Submit Registration");
        add("English", "male", "Male");
        add("English", "female", "Female");
        add("English", "other", "Other");
        add("English", "prefer_not", "Prefer not to say");

        // Hindi
        add("Hindi", "title", "रोगी पंजीकरण फॉर्म");
        add("Hindi", "subtitle", "कृपया अपना जनसांख्यिकीय विवरण भरें और अपने लक्षणों का चयन करें।");
        add("Hindi", "name", "पूरा नाम");
        add("Hindi", "age", "आयु");
        add("Hindi", "contact", "संपर्क जानकारी");
        add("Hindi", "gender", "लिंग");
        add("Hindi", "blood", "रक्त समूह");
        add("Hindi", "allergies", "एलर्जी");
        add("Hindi", "language", "पसंदीदा भाषा");
        add("Hindi", "symptoms", "लक्षण चुनें");
        add("Hindi", "chest_pain", "सीने में दर्द");
        add("Hindi", "fever", "तेज बुखार");
        add("Hindi", "breathing", "सांस लेने में कठिनाई");
        add("Hindi", "fracture", "फ्रैक्चर / हड्डी टूटना");
        add("Hindi", "cough", "खांसी / जुकाम");
        add("Hindi", "submit", "पंजीकरण जमा करें");
        add("Hindi", "male", "पुरुष");
        add("Hindi", "female", "महिला");
        add("Hindi", "other", "अन्य");
        add("Hindi", "prefer_not", "कहना नहीं चाहेंगे");

        // Tamil
        add("Tamil", "title", "நோயாளிகள் பதிவு படிவம்");
        add("Tamil", "subtitle", "தயவுசெய்து உங்கள் விவரங்களை நிரப்பி அறிகுறிகளை தேர்ந்தெடுக்கவும்.");
        add("Tamil", "name", "முழு பெயர்");
        add("Tamil", "age", "வயது");
        add("Tamil", "contact", "தொடர்பு எண்");
        add("Tamil", "gender", "பாலினம்");
        add("Tamil", "blood", "இரத்த வகை");
        add("Tamil", "allergies", "ஒவ்வாமை");
        add("Tamil", "language", "விருப்பமான மொழி");
        add("Tamil", "symptoms", "அறிகுறிகளைத் தேர்ந்தெடுக்கவும்");
        add("Tamil", "chest_pain", "நெஞ்சு வலி");
        add("Tamil", "fever", "அதிக காய்ச்சல்");
        add("Tamil", "breathing", "மூச்சுத் திணறல்");
        add("Tamil", "fracture", "எலும்பு முறிவு");
        add("Tamil", "cough", "இருமல் / சளி");
        add("Tamil", "submit", "பதிவை சமர்ப்பிக்கவும்");
        add("Tamil", "male", "ஆண்");
        add("Tamil", "female", "பெண்");
        add("Tamil", "other", "மற்றவை");
        add("Tamil", "prefer_not", "கூற விரும்பவில்லை");

        // Telugu
        add("Telugu", "title", "రోగి నమోదు ఫారం");
        add("Telugu", "subtitle", "దయచేసి మీ వివరాలను పూరించి, మీ లక్షణాలను ఎంచుకోండి.");
        add("Telugu", "name", "పూర్తి పేరు");
        add("Telugu", "age", "వయస్సు");
        add("Telugu", "contact", "సంప్రదింపు సమాచారం");
        add("Telugu", "gender", "లింగం");
        add("Telugu", "blood", "రక్త వర్గం");
        add("Telugu", "allergies", "అలెర్జీలు");
        add("Telugu", "language", "ప్రాధాన్య భాష");
        add("Telugu", "symptoms", "లక్షణాలను ఎంచుకోండి");
        add("Telugu", "chest_pain", "ఛాతీ నొప్పి");
        add("Telugu", "fever", "తీవ్రమైన జ్వరం");
        add("Telugu", "breathing", "శ్వాస తీసుకోవడంలో ఇబ్బంది");
        add("Telugu", "fracture", "ఫ్రాక్చర్ / ఎముక విరగడం");
        add("Telugu", "cough", "దగ్గు / జలుబు");
        add("Telugu", "submit", "నమోదును సమర్పించండి");
        add("Telugu", "male", "పురుషుడు");
        add("Telugu", "female", "స్త్రీ");
        add("Telugu", "other", "ఇతర");
        add("Telugu", "prefer_not", "చెప్పడానికి ఇష్టపడను");

        // Malayalam
        add("Malayalam", "title", "രോഗി രജിസ്ട്രേഷൻ ഫോം");
        add("Malayalam", "subtitle", "ദയവായി നിങ്ങളുടെ വിവരങ്ങൾ നൽകി ലക്ഷണങ്ങൾ തിരഞ്ഞെടുക്കുക.");
        add("Malayalam", "name", "മുഴുവൻ പേര്");
        add("Malayalam", "age", "പ്രായം");
        add("Malayalam", "contact", "ബന്ധപ്പെടാനുള്ള വിവരങ്ങൾ");
        add("Malayalam", "gender", "ലിംഗഭേദം");
        add("Malayalam", "blood", "രക്തഗ്രൂപ്പ്");
        add("Malayalam", "allergies", "അലർജികൾ");
        add("Malayalam", "language", "ഇഷ്ടപ്പെട്ട ഭാഷ");
        add("Malayalam", "symptoms", "ലക്ഷണങ്ങൾ തിരഞ്ഞെടുക്കുക");
        add("Malayalam", "chest_pain", "നെഞ്ചുവേദന");
        add("Malayalam", "fever", "കടുത്ത പനി");
        add("Malayalam", "breathing", "ശ്വാസതടസ്സം");
        add("Malayalam", "fracture", "ഒടിവ്");
        add("Malayalam", "cough", "ചുമ / ജലദോഷം");
        add("Malayalam", "submit", "രജിസ്ട്രേഷൻ സമർപ്പിക്കുക");
        add("Malayalam", "male", "പുരുഷൻ");
        add("Malayalam", "female", "സ്ത്രീ");
        add("Malayalam", "other", "മറ്റ്");
        add("Malayalam", "prefer_not", "പറയാൻ താല്പര്യമില്ല");
    }

    private static void add(String lang, String key, String value) {
        dictionary.computeIfAbsent(lang, k -> new HashMap<>()).put(key, value);
    }

    public static String get(String key, String language) {
        if (!dictionary.containsKey(language)) {
            language = "English";
        }
        return dictionary.get(language).getOrDefault(key, dictionary.get("English").get(key));
    }

    public static String getEnglishGender(String translatedGender, String language) {
        if (translatedGender == null) return "Unknown";
        if ("English".equals(language)) return translatedGender;
        
        Map<String, String> langMap = dictionary.getOrDefault(language, dictionary.get("English"));
        for (String key : new String[]{"male", "female", "other", "prefer_not"}) {
            if (langMap.get(key).equals(translatedGender)) {
                return dictionary.get("English").get(key);
            }
        }
        return "Unknown";
    }
}
