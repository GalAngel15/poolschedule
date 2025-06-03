package com.classy.poolschedule.models;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class Instructor {
    private String name;
    private EnumSet<Student.SwimmingStyle> styles;
    private Map<DayOfWeek, TimeSlot> availability;

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

    public static class TimeSlot {
        private int startHour;
        private int endHour;

        public TimeSlot(int startHour, int endHour) {
            this.startHour = startHour;
            this.endHour = endHour;
        }

        public int getStartHour() { return startHour; }
        public int getEndHour() { return endHour; }

        public boolean isAvailableAt(int hour) {
            return hour >= startHour && hour < endHour;
        }

        @Override
        public String toString() {
            return String.format("%02d:00-%02d:00", startHour, endHour);
        }
    }

    public Instructor(String name) {
        this.name = name;
        this.styles = EnumSet.noneOf(Student.SwimmingStyle.class);
        this.availability = new HashMap<>();
    }

    public void addStyle(Student.SwimmingStyle style) {
        this.styles.add(style);
    }

    public void setAvailability(DayOfWeek day, int startHour, int endHour) {
        availability.put(day, new TimeSlot(startHour, endHour));
    }

    public boolean canTeach(Student.SwimmingStyle style) {
        return styles.contains(style);
    }

    public boolean isAvailableOn(DayOfWeek day, int hour) {
        TimeSlot slot = availability.get(day);
        return slot != null && slot.isAvailableAt(hour);
    }

    // Static factory methods for predefined instructors
    public static Instructor createYotam() {
        Instructor yotam = new Instructor("יותם");
        yotam.addStyle(Student.SwimmingStyle.FREESTYLE);
        yotam.addStyle(Student.SwimmingStyle.BREASTSTROKE);
        yotam.addStyle(Student.SwimmingStyle.BUTTERFLY);
        yotam.addStyle(Student.SwimmingStyle.BACKSTROKE);
        yotam.setAvailability(DayOfWeek.MONDAY, 16, 20);
        yotam.setAvailability(DayOfWeek.THURSDAY, 16, 20);
        return yotam;
    }

    public static Instructor createYoni() {
        Instructor yoni = new Instructor("יוני");
        yoni.addStyle(Student.SwimmingStyle.BREASTSTROKE);
        yoni.addStyle(Student.SwimmingStyle.BUTTERFLY);
        yoni.setAvailability(DayOfWeek.TUESDAY, 8, 15);
        yoni.setAvailability(DayOfWeek.WEDNESDAY, 8, 15);
        yoni.setAvailability(DayOfWeek.THURSDAY, 8, 15);
        yoni.setAvailability(DayOfWeek.FRIDAY, 8, 15);
        return yoni;
    }

    public static Instructor createJonny() {
        Instructor jonny = new Instructor("ג'וני");
        jonny.addStyle(Student.SwimmingStyle.FREESTYLE);
        jonny.addStyle(Student.SwimmingStyle.BREASTSTROKE);
        jonny.addStyle(Student.SwimmingStyle.BUTTERFLY);
        jonny.addStyle(Student.SwimmingStyle.BACKSTROKE);
        jonny.setAvailability(DayOfWeek.SUNDAY, 10, 19);
        jonny.setAvailability(DayOfWeek.TUESDAY, 10, 19);
        jonny.setAvailability(DayOfWeek.THURSDAY, 10, 19);
        return jonny;
    }

    // Getters
    public String getName() { return name; }
    public EnumSet<Student.SwimmingStyle> getStyles() { return styles; }
    public Map<DayOfWeek, TimeSlot> getAvailability() { return availability; }

    @Override
    public String toString() {
        return name;
    }
}