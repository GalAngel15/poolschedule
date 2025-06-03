package com.classy.poolschedule.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.classy.poolschedule.R;
import com.classy.poolschedule.adapters.StudentAdapter;
import com.classy.poolschedule.adapters.LessonAdapter;
import com.classy.poolschedule.managers.ScheduleManager;
import com.classy.poolschedule.models.Student;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName;
    private Spinner spinnerStyle, spinnerLessonType;
    private Button btnAddStudent, btnGenerateSchedule, btnClearAll;
    private ListView lvStudents, lvSchedule;
    private TextView tvConflicts;

    private List<Student> students;
    private StudentAdapter studentAdapter;
    private LessonAdapter lessonAdapter;
    private ScheduleManager scheduleManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
        setupSpinners();
        setupListeners();
    }

    private void initViews() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        spinnerStyle = findViewById(R.id.spinnerStyle);
        spinnerLessonType = findViewById(R.id.spinnerLessonType);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        btnGenerateSchedule = findViewById(R.id.btnGenerateSchedule);
        btnClearAll = findViewById(R.id.btnClearAll);
        lvStudents = findViewById(R.id.lvStudents);
        lvSchedule = findViewById(R.id.lvSchedule);
        tvConflicts = findViewById(R.id.tvConflicts);
    }

    private void initData() {
        students = new ArrayList<>();
        scheduleManager = new ScheduleManager();

        studentAdapter = new StudentAdapter(this, students);
        lvStudents.setAdapter(studentAdapter);

        lessonAdapter = new LessonAdapter(this, new ArrayList<>());
        lvSchedule.setAdapter(lessonAdapter);
    }

    private void setupSpinners() {
        // Swimming styles spinner
        String[] styles = new String[Student.SwimmingStyle.values().length];
        for (int i = 0; i < Student.SwimmingStyle.values().length; i++) {
            styles[i] = Student.SwimmingStyle.values()[i].getHebrewName();
        }
        ArrayAdapter<String> styleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, styles);
        styleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStyle.setAdapter(styleAdapter);

        // Lesson types spinner
        String[] lessonTypes = new String[Student.LessonType.values().length];
        for (int i = 0; i < Student.LessonType.values().length; i++) {
            lessonTypes[i] = Student.LessonType.values()[i].getHebrewName();
        }
        ArrayAdapter<String> lessonTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lessonTypes);
        lessonTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLessonType.setAdapter(lessonTypeAdapter);
    }

    private void setupListeners() {
        btnAddStudent.setOnClickListener(v -> addStudent());
        btnGenerateSchedule.setOnClickListener(v -> generateSchedule());
        btnClearAll.setOnClickListener(v -> clearAll());

        // Long click to remove student
        lvStudents.setOnItemLongClickListener((parent, view, position, id) -> {
            showRemoveStudentDialog(position);
            return true;
        });

        // Click on lesson to show details
        lvSchedule.setOnItemClickListener((parent, view, position, id) -> {
            showLessonDetails(position);
        });
    }

    private void addStudent() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "אנא מלא את כל השדות", Toast.LENGTH_SHORT).show();
            return;
        }

        if (students.size() >= 30) {
            Toast.makeText(this, "מקסימום 30 תלמידים", Toast.LENGTH_SHORT).show();
            return;
        }

        Student.SwimmingStyle selectedStyle = Student.SwimmingStyle.values()[spinnerStyle.getSelectedItemPosition()];
        Student.LessonType selectedType = Student.LessonType.values()[spinnerLessonType.getSelectedItemPosition()];

        Log.d("MainActivity", "Selected Style: " + selectedStyle.name());
        Log.d("MainActivity", "Selected Type: " + selectedType.name());
        Student student = new Student(firstName, lastName, selectedStyle, selectedType);
        students.add(student);
        studentAdapter.notifyDataSetChanged();

        // Clear input fields
        etFirstName.setText("");
        etLastName.setText("");

        Toast.makeText(this, "תלמיד נוסף בהצלחה", Toast.LENGTH_SHORT).show();
    }

    private void generateSchedule() {
        if (students.isEmpty()) {
            Toast.makeText(this, "אנא הוסף תלמידים לפני יצירת לוח זמנים", Toast.LENGTH_SHORT).show();
            return;
        }

        ScheduleManager.ScheduleResult result = scheduleManager.scheduleStudents(students);

        lessonAdapter.updateLessons(result.getScheduledLessons());
        tvConflicts.setText(result.getConflictMessage());

        if (result.hasConflicts()) {
            tvConflicts.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            tvConflicts.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }

        Toast.makeText(this, "לוח זמנים נוצר!", Toast.LENGTH_SHORT).show();
    }

    private void clearAll() {
        new AlertDialog.Builder(this)
                .setTitle("מחיקה")
                .setMessage("האם אתה בטוח שברצונך למחוק את כל הנתונים?")
                .setPositiveButton("כן", (dialog, which) -> {
                    students.clear();
                    studentAdapter.notifyDataSetChanged();
                    lessonAdapter.updateLessons(new ArrayList<>());
                    tvConflicts.setText("");
                    Toast.makeText(MainActivity.this, "כל הנתונים נמחקו", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("ביטול", null)
                .show();
    }

    private void showRemoveStudentDialog(int position) {
        Student student = students.get(position);
        new AlertDialog.Builder(this)
                .setTitle("הסרת תלמיד")
                .setMessage("האם להסיר את " + student.getFullName() + "?")
                .setPositiveButton("כן", (dialog, which) -> {
                    students.remove(position);
                    studentAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "תלמיד הוסר", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("ביטול", null)
                .show();
    }

    private void showLessonDetails(int position) {
        String lessonDetails = lessonAdapter.getLessonDetails(position);
        new AlertDialog.Builder(this)
                .setTitle("פרטי השיעור")
                .setMessage(lessonDetails)
                .setPositiveButton("סגור", null)
                .show();
    }
}