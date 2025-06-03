package com.classy.poolschedule.models;

public class Student {
    private String firstName;
    private String lastName;
    private SwimmingStyle preferredStyle;
    private LessonType preferredType;

    public enum SwimmingStyle {
        FREESTYLE("חתירה"),
        BREASTSTROKE("חזה"),
        BUTTERFLY("פרפר"),
        BACKSTROKE("גב");

        private final String hebrewName;

        SwimmingStyle(String hebrewName) {
            this.hebrewName = hebrewName;
        }

        public String getHebrewName() {
            return hebrewName;
        }
    }

    public enum LessonType {
        PRIVATE_ONLY("פרטי בלבד"),
        GROUP_ONLY("קבוצתי בלבד"),
        BOTH("פרטי או קבוצתי");

        private final String hebrewName;

        LessonType(String hebrewName) {
            this.hebrewName = hebrewName;
        }

        public String getHebrewName() {
            return hebrewName;
        }
    }

    public Student(String firstName, String lastName, SwimmingStyle preferredStyle, LessonType preferredType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.preferredStyle = preferredStyle;
        this.preferredType = preferredType;
    }

    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public SwimmingStyle getPreferredStyle() { return preferredStyle; }
    public void setPreferredStyle(SwimmingStyle preferredStyle) { this.preferredStyle = preferredStyle; }

    public LessonType getPreferredType() { return preferredType; }
    public void setPreferredType(LessonType preferredType) { this.preferredType = preferredType; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return getFullName() + " - " + preferredStyle.getHebrewName() + " (" + preferredType.getHebrewName() + ")";
    }
}