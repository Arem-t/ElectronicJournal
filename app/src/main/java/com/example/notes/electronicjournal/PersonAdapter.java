package com.example.notes.electronicjournal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Person> persons;


    public LayoutInflater getInflater() {
        return inflater;
    }

    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public PersonAdapter(Context context, List<Person> persons) {
        this.persons = persons;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public PersonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return new PersonAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonAdapter.ViewHolder holder, int position) {
        Person person = persons.get(position);
        holder.personName.setText(person.getName());
        holder.className.setText(person.getClass_name());
        holder.teacher.setText(String.valueOf(person.isTeacher()));

    }

    @Override
    public int getItemCount() {
        return persons.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView personName,className,teacher;
        ImageView delete_person;

        public ViewHolder(View itemView) {
            super(itemView);
            personName = itemView.findViewById(R.id.person_name);
            className = itemView.findViewById(R.id.class_name);
            teacher = itemView.findViewById(R.id.teacher);
            delete_person = itemView.findViewById(R.id.delete_person_btn);
        }
    }
}
