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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EditRecordActivity extends AppCompatActivity {
    private FirebaseAuth rauth;
    private String editRef,personKey,personName;
    private FirebaseDatabase database;
    private Button editRecordbtn;
    private EditText editLesson,editDate,editGrade;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);

        editLesson = findViewById(R.id.edit_lesson_name);
        editDate = findViewById(R.id.edit_date);
        editGrade = findViewById(R.id.edit_grade);
        editRecordbtn = findViewById(R.id.edit_record_btn);

        rauth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();

        Bundle arguments = getIntent().getExtras();
         if(arguments!=null){
             editLesson.setText(arguments.get("lesson").toString());
             editDate.setText(arguments.get("date").toString());
             editGrade.setText(arguments.get("grade").toString());
             editRef = arguments.get("ref").toString();
             personKey = arguments.get("personKey").toString();
             personName = arguments.get("personName").toString();
         }
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(EditRecordActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                editDate.setText(dayOfMonth + "."
                                        + (monthOfYear + 1) + "." + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        editRecordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editLesson.getText().toString().isEmpty() || editDate.getText().toString().isEmpty())
                    Toast.makeText(EditRecordActivity.this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
                else {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(personKey)
                            .child("Lessons").child(arguments.get("ref").toString()).child("lesson_name").setValue(editLesson.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("Users").child(personKey)
                            .child("Lessons").child(arguments.get("ref").toString()).child("date").setValue(editDate.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("Users").child(personKey)
                            .child("Lessons").child(arguments.get("ref").toString()).child("grade").setValue(editGrade.getText().toString());
                    Intent intent = new Intent(EditRecordActivity.this,JournalActivity.class);
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
        Intent intent = new Intent(EditRecordActivity.this,JournalActivity.class);
        intent.putExtra("personKey",personKey);
        intent.putExtra("personName",personName);
        intent.putExtra("isTeacher", true);
        startActivity(intent);
    }

}