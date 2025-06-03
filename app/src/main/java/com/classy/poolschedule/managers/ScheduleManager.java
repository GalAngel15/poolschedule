package com.classy.poolschedule.managers;

import android.util.Log;

import com.classy.poolschedule.models.Instructor;
import com.classy.poolschedule.models.Lesson;
import com.classy.poolschedule.models.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleManager {
    private List<Instructor> instructors;
    private List<Lesson> lessons;
    private List<Student> unassignedStudents;

    public ScheduleManager() {
        instructors = new ArrayList<>();
        lessons = new ArrayList<>();
        unassignedStudents = new ArrayList<>();
        initializeInstructors();
    }

    private void initializeInstructors() {
        instructors.add(Instructor.createYotam());
        instructors.add(Instructor.createYoni());
        instructors.add(Instructor.createJonny());
    }

    public ScheduleResult scheduleStudents(List<Student> students) {
        lessons.clear();
        unassignedStudents.clear();

        // Group students by style and lesson type preference
        Map<String, List<Student>> studentGroups = groupStudents(students);

        for (Map.Entry<String, List<Student>> entry : studentGroups.entrySet()) {
            String[] parts = entry.getKey().split("&");
            Log.d("ScheduleManager", "All: " + entry.getKey());
            Log.d("ScheduleManager", "Style: " + parts[0] + ", Type: " + parts[1]);
            Student.SwimmingStyle style = Student.SwimmingStyle.valueOf(parts[0]);
            Student.LessonType preferredType = Student.LessonType.valueOf(parts[1]);

            List<Student> groupStudents = entry.getValue();
            scheduleStudentGroup(groupStudents, style, preferredType);
        }

        return new ScheduleResult(new ArrayList<>(lessons), new ArrayList<>(unassignedStudents));
    }

    private Map<String, List<Student>> groupStudents(List<Student> students) {
        Map<String, List<Student>> groups = new HashMap<>();

        for (Student student : students) {
            String key = student.getPreferredStyle().name() + "&" + student.getPreferredType().name();
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(student);
        }

        return groups;
    }

    private void scheduleStudentGroup(List<Student> students, Student.SwimmingStyle style, Student.LessonType preferredType) {
        // Try to schedule group lessons first if preferred
        if (preferredType == Student.LessonType.GROUP_ONLY || preferredType == Student.LessonType.BOTH) {
            scheduleGroupLessons(students, style);
        }

        // Schedule remaining students in private lessons
        List<Student> remaining = new ArrayList<>();
        for (Student student : students) {
            if (!isStudentScheduled(student)) {
                remaining.add(student);
            }
        }

        if (preferredType == Student.LessonType.PRIVATE_ONLY || preferredType == Student.LessonType.BOTH) {
            schedulePrivateLessons(remaining, style);
        } else {
            unassignedStudents.addAll(remaining);
        }
    }

    private void scheduleGroupLessons(List<Student> students, Student.SwimmingStyle style) {
        // Try to create group lessons with available instructors
        for (Instructor instructor : instructors) {
            if (!instructor.canTeach(style)) continue;

            for (Instructor.DayOfWeek day : Instructor.DayOfWeek.values()) {
                if (day == Instructor.DayOfWeek.FRIDAY) continue; // Pool closed on weekends

                Instructor.TimeSlot timeSlot = instructor.getAvailability().get(day);
                if (timeSlot == null) continue;

                // Try each hour in the time slot
                for (int hour = timeSlot.getStartHour(); hour <= timeSlot.getEndHour() - 1; hour++) {
                    if (isTimeSlotAvailable(instructor, day, hour, Lesson.LessonType.GROUP)) {
                        Lesson lesson = new Lesson(instructor, day, hour, style, Lesson.LessonType.GROUP);

                        // Add up to 6 students to group lesson
                        int added = 0;
                        List<Student> toRemove = new ArrayList<>();
                        for (Student student : students) {
                            if (lesson.canAddStudent(student) && added < 6) {
                                lesson.addStudent(student);
                                toRemove.add(student);
                                added++;
                            }
                        }

                        if (added >= 2) { // Minimum group size
                            lessons.add(lesson);
                            students.removeAll(toRemove);
                        }
                    }
                }
            }
        }
    }

    private void schedulePrivateLessons(List<Student> students, Student.SwimmingStyle style) {
        for (Student student : students) {
            boolean scheduled = false;

            for (Instructor instructor : instructors) {
                if (!instructor.canTeach(style)) continue;

                for (Instructor.DayOfWeek day : Instructor.DayOfWeek.values()) {
                    if (day == Instructor.DayOfWeek.FRIDAY) continue;

                    Instructor.TimeSlot timeSlot = instructor.getAvailability().get(day);
                    if (timeSlot == null) continue;

                    for (int hour = timeSlot.getStartHour(); hour <= timeSlot.getEndHour() - 1; hour++) {
                        if (isTimeSlotAvailable(instructor, day, hour, Lesson.LessonType.PRIVATE)) {
                            Lesson lesson = new Lesson(instructor, day, hour, style, Lesson.LessonType.PRIVATE);
                            lesson.addStudent(student);
                            lessons.add(lesson);
                            scheduled = true;
                            break;
                        }
                    }
                    if (scheduled) break;
                }
                if (scheduled) break;
            }

            if (!scheduled) {
                unassignedStudents.add(student);
            }
        }
    }

    private boolean isTimeSlotAvailable(Instructor instructor, Instructor.DayOfWeek day, int hour, Lesson.LessonType type) {
        for (Lesson lesson : lessons) {
            if (lesson.getInstructor().equals(instructor) &&
                    lesson.getDay() == day) {

                int lessonEndHour = lesson.getStartHour() + (lesson.getType().getDurationMinutes() / 60);
                int newLessonEndHour = hour + (type.getDurationMinutes() / 60);

                // Check for overlap
                if (!(hour >= lessonEndHour || lesson.getStartHour() >= newLessonEndHour)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isStudentScheduled(Student student) {
        for (Lesson lesson : lessons) {
            if (lesson.getStudents().contains(student)) {
                return true;
            }
        }
        return false;
    }

    public static class ScheduleResult {
        private List<Lesson> scheduledLessons;
        private List<Student> unassignedStudents;

        public ScheduleResult(List<Lesson> scheduledLessons, List<Student> unassignedStudents) {
            this.scheduledLessons = scheduledLessons;
            this.unassignedStudents = unassignedStudents;
        }

        public List<Lesson> getScheduledLessons() { return scheduledLessons; }
        public List<Student> getUnassignedStudents() { return unassignedStudents; }

        public boolean hasConflicts() {
            return !unassignedStudents.isEmpty();
        }

        public String getConflictMessage() {
            if (unassignedStudents.isEmpty()) {
                return "כל התלמידים שובצו בהצלחה!";
            }

            StringBuilder sb = new StringBuilder("תלמידים שלא שובצו:\n");
            for (Student student : unassignedStudents) {
                sb.append("• ").append(student.toString()).append("\n");
            }
            sb.append("\nסיבות אפשריות: אין מדריך זמין, התנגשות זמנים, או העדפות לא תואמות.");
            return sb.toString();
        }
    }
}