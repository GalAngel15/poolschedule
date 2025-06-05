package com.classy.poolschedule.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.classy.poolschedule.models.Lesson;
import com.classy.poolschedule.models.Student;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesManager {
    private static SharedPreferencesManager instance;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private static final String SHARED_PREFS_NAME = "PoolSchedulePrefs";
    private static final String KEY_STUDENTS = "students_list";
    private static final String KEY_LESSONS = "lessons_list";

    private SharedPreferencesManager(Context context) {
        sharedPreferences= context.getApplicationContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context);
        }
        return instance;
    }

    public void saveStudents(List<Student> students) {
        SharedPreferences.Editor editor= sharedPreferences.edit();
        String studentsJson = gson.toJson(students);
        editor.putString(KEY_STUDENTS, studentsJson);
        editor.apply();
    }

    public List<Student> loadStudents(){
        String studentsJson = sharedPreferences.getString(KEY_STUDENTS, null);
        if (studentsJson != null) {
            Student[] studentsArray = gson.fromJson(studentsJson, Student[].class);
            if (studentsArray != null) {
                return List.of(studentsArray);
            }else {
                Log.e("SharedPreferencesManager", "Failed to parse students JSON");
            }
        }
        return new ArrayList<>();
    }

    public void saveLessons(List<Lesson> lessons) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String lessonsJson = gson.toJson(lessons);
        editor.putString(KEY_LESSONS, lessonsJson);
        editor.apply();
    }

    public List<Lesson> loadLessons() {
        String lessonsJson = sharedPreferences.getString(KEY_LESSONS, null);
        if (lessonsJson != null) {
            Lesson[] lessonsArray = gson.fromJson(lessonsJson, Lesson[].class);
            if (lessonsArray != null) {
                return List.of(lessonsArray);
            } else {
                Log.e("SharedPreferencesManager", "Failed to parse lessons JSON");
            }
        }
        return new ArrayList<>();
    }

    public void clearData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
