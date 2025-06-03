package com.classy.poolschedule.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.classy.poolschedule.R;
import com.classy.poolschedule.models.Student;

import java.util.List;

public class StudentAdapter extends BaseAdapter {
    private Context context;
    private List<Student> students;
    private LayoutInflater inflater;

    public StudentAdapter(Context context, List<Student> students) {
        this.context = context;
        this.students = students;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_student, parent, false);
            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.tvStudentName);
            holder.tvStyle = convertView.findViewById(R.id.tvStudentStyle);
            holder.tvType = convertView.findViewById(R.id.tvStudentType);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Student student = students.get(position);
        holder.tvName.setText(student.getFullName());
        holder.tvStyle.setText("סגנון: " + student.getPreferredStyle().getHebrewName());
        holder.tvType.setText("סוג שיעור: " + student.getPreferredType().getHebrewName());

        return convertView;
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvStyle;
        TextView tvType;
    }
}