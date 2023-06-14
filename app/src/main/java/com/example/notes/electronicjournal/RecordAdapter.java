package com.example.notes.electronicjournal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordAdapter  extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Record> records;


    public LayoutInflater getInflater() {
        return inflater;
    }

    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public RecordAdapter(Context context, List<Record> records) {
        this.records = records;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordAdapter.ViewHolder holder, int position) {
        Record record = records.get(position);
        holder.lessonName.setText(record.getLesson_name());
        holder.date.setText(record.getDate());
        holder.grade.setText(record.getGrade());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView lessonName,date,grade;
        ImageView edit_btn,delete_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            lessonName = itemView.findViewById(R.id.lesson_name_txt);
            date = itemView.findViewById(R.id.date_txt);
            grade = itemView.findViewById(R.id.grade_txt);
            edit_btn =itemView.findViewById(R.id.edit_record_btn);
            delete_btn = itemView.findViewById(R.id.delete_record_btn);

        }
    }
}
