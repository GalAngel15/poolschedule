package com.classy.poolschedule.models;

import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private Instructor instructor;
    private List<Student> students;
    private DayOfWeek day;
    private int startHour;
    private int startMinute;

    private Student.SwimmingStyle style;
    private LessonType type;


    public enum LessonType {
        PRIVATE("שיעור פרטי", 45),
        GROUP("שיעור קבוצתי", 60);

        private final String hebrewName;
        private final int durationMinutes;

        LessonType(String hebrewName, int durationMinutes) {
            this.hebrewName = hebrewName;
            this.durationMinutes = durationMinutes;
        }

        public String getHebrewName() { return hebrewName; }
        public int getDurationMinutes() { return durationMinutes; }
    }

    public Lesson(Instructor instructor, DayOfWeek day, int startHour,int startMinute, Student.SwimmingStyle style, LessonType type) {
        this.instructor = instructor;
        this.day = day;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.style = style;
        this.type = type;
        this.students = new ArrayList<>();
    }

    public Lesson(Instructor instructor, DayOfWeek day, int startHour, Student.SwimmingStyle style, LessonType type) {
        this.instructor = instructor;
        this.day = day;
        this.startHour = startHour;
        this.startMinute = 0;
        this.style = style;
        this.type = type;
        this.students = new ArrayList<>();
    }

    public boolean canAddStudent(Student student) {
        if (type == LessonType.PRIVATE && students.size() >= 1) {
            return false;
        }
        if (type == LessonType.GROUP && students.size() >= 6) { // Max group size
            return false;
        }
        return student.getPreferredStyle() == style &&
                (student.getPreferredType() == Student.LessonType.BOTH ||
                        (student.getPreferredType() == Student.LessonType.PRIVATE_ONLY && type == LessonType.PRIVATE) ||
                        (student.getPreferredType() == Student.LessonType.GROUP_ONLY && type == LessonType.GROUP));
    }

    public void addStudent(Student student) {
        if (canAddStudent(student)) {
            students.add(student);
        }
    }

    public String getTimeString() {
        int endHour = startHour * 60 + startMinute + type.getDurationMinutes();
        int startH= startHour;
        int startM= startMinute;
        int endH= endHour / 60;
        int endM= endHour % 60;

        return String.format("%02d:%02d-%02d:%02d", startH, startM, endH, endM);

    }

    public String getStudentsString() {
        if (students.isEmpty()) {
            return "אין תלמידים";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < students.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(students.get(i).getFullName());
        }
        return sb.toString();
    }

    // Getters and Setters
    public Instructor getInstructor() { return instructor; }
    public void setInstructor(Instructor instructor) { this.instructor = instructor; }

    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }

    public DayOfWeek getDay() { return day; }
    public void setDay(DayOfWeek day) { this.day = day; }

    public int getStartHour() { return startHour; }
    public void setStartHour(int startHour) { this.startHour = startHour; }

    public int getStartMinute() { return startMinute; }
    public void setStartMinute(int startMinute) { this.startMinute = startMinute; }

    public Student.SwimmingStyle getStyle() { return style; }
    public void setStyle(Student.SwimmingStyle style) { this.style = style; }

    public LessonType getType() { return type; }
    public void setType(LessonType type) { this.type = type; }

    @Override
    public String toString() {
        return String.format("%s - %s %s (%s) - %s",
                day.getHebrewName(),
                getTimeString(),
                type.getHebrewName(),
                style.getHebrewName(),
                instructor.getName());
    }
}