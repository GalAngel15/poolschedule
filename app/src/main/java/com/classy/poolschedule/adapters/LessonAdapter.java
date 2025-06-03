package com.classy.poolschedule.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.classy.poolschedule.R;
import com.classy.poolschedule.models.Lesson;

import java.util.List;

public class LessonAdapter extends BaseAdapter {
    private Context context;
    private List<Lesson> lessons;
    private LayoutInflater inflater;

    public LessonAdapter(Context context, List<Lesson> lessons) {
        this.context = context;
        this.lessons = lessons;
        this.inflater = LayoutInflater.from(context);
    }

    public void updateLessons(List<Lesson> newLessons) {
        this.lessons = newLessons;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lessons.size();
    }

    @Override
    public Object getItem(int position) {
        return lessons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_lesson, parent, false);
            holder = new ViewHolder();
            holder.tvDay = convertView.findViewById(R.id.tvLessonDay);
            holder.tvTime = convertView.findViewById(R.id.tvLessonTime);
            holder.tvType = convertView.findViewById(R.id.tvLessonType);
            holder.tvInstructor = convertView.findViewById(R.id.tvLessonInstructor);
            holder.tvStyle = convertView.findViewById(R.id.tvLessonStyle);
            holder.tvStudents = convertView.findViewById(R.id.tvLessonStudents);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Lesson lesson = lessons.get(position);
        holder.tvDay.setText(lesson.getDay().getHebrewName());
        holder.tvTime.setText(lesson.getTimeString());
        holder.tvType.setText(lesson.getType().getHebrewName());
        holder.tvInstructor.setText("מדריך: " + lesson.getInstructor().getName());
        holder.tvStyle.setText("סגנון: " + lesson.getStyle().getHebrewName());
        holder.tvStudents.setText("תלמידים: " + lesson.getStudentsString());

        return convertView;
    }

    public String getLessonDetails(int position) {
        Lesson lesson = lessons.get(position);
        StringBuilder details = new StringBuilder();
        details.append("יום: ").append(lesson.getDay().getHebrewName()).append("\n");
        details.append("שעה: ").append(lesson.getTimeString()).append("\n");
        details.append("סוג שיעור: ").append(lesson.getType().getHebrewName()).append("\n");
        details.append("מדריך: ").append(lesson.getInstructor().getName()).append("\n");
        details.append("סגנון שחייה: ").append(lesson.getStyle().getHebrewName()).append("\n");
        details.append("מספר תלמידים: ").append(lesson.getStudents().size()).append("\n");
        details.append("תלמידים:\n");

        for (int i = 0; i < lesson.getStudents().size(); i++) {
            details.append("• ").append(lesson.getStudents().get(i).getFullName());
            if (i < lesson.getStudents().size() - 1) {
                details.append("\n");
            }
        }

        return details.toString();
    }

    private static class ViewHolder {
        TextView tvDay;
        TextView tvTime;
        TextView tvType;
        TextView tvInstructor;
        TextView tvStyle;
        TextView tvStudents;
    }
}