package com.example.notes.electronicjournal;


public class Record {
    private String lesson_name;
    private String date;
    private String grade;

    public Record(String lesson_name, String date, String grade) {
        this.lesson_name = lesson_name;
        this.date = date;
        this.grade = grade;
    }
    public Record() {
    }

    public String getLesson_name() {
        return lesson_name;
    }

    public void setLesson_name(String lesson_name) {
        this.lesson_name = lesson_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

}
