package com.example.notes.electronicjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddRecordActivity extends AppCompatActivity {

    private Button addRecordbtn;
    private EditText addLesson,addDate,addGrade;
    private FirebaseAuth rauth;
    private DatabaseReference referencedatabase;
    private FirebaseDatabase database;
    private int recordsSize;
    DatePickerDialog datePickerDialog;
    String personKey,personName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        addLesson = findViewById(R.id.add_lesson_name);
        addDate = findViewById(R.id.add_date);
        addGrade = findViewById(R.id.add_grade);
        addRecordbtn = findViewById(R.id.add_record_btn);

        rauth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        referencedatabase = database.getReference();

        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            personKey = arguments.get("personKey").toString();
            personName = arguments.get("personName").toString();

        }

        addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(AddRecordActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                addDate.setText(dayOfMonth + "."
                                        + (monthOfYear + 1) + "." + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        addRecordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addLesson.getText().toString().isEmpty() || addDate.getText().toString().isEmpty())
                    Toast.makeText(AddRecordActivity.this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
                else {
                    referencedatabase.child("Users").child(personKey).child("Lessons").push().setValue(
                            new Record(addLesson.getText().toString(),addDate.getText().toString(),addGrade.getText().toString())
                    );

                    Intent intent = new Intent(AddRecordActivity.this,JournalActivity.class);
                    intent.putExtra("personKey",personKey);
                    intent.putExtra("personName",personName);
                    intent.putExtra("isTeacher", true);
                    startActivity(intent);
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddRecordActivity.this,JournalActivity.class);
        intent.putExtra("personKey",personKey);
        intent.putExtra("personName",personName);
        intent.putExtra("isTeacher", true);
        startActivity(intent);
        finish();
    }
}