package com.classy.poolschedule.models;

public enum DayOfWeek {
    SUNDAY("ראשון"),
    MONDAY("שני"),
    TUESDAY("שלישי"),
    WEDNESDAY("רביעי"),
    THURSDAY("חמישי"),
    FRIDAY("שישי");

    private final String hebrewName;

    DayOfWeek(String hebrewName) {
        this.hebrewName = hebrewName;
    }

    public String getHebrewName() {
        return hebrewName;
    }
}
