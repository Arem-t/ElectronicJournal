package com.example.notes.electronicjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    private EditText email_login, email_password;
    private Button login_button;
    private TextView register_txt;
    private FirebaseAuth auth;
    DatabaseReference ref;
    FirebaseDatabase database;
    Boolean teacher;

    public void setTeacher(Boolean teacher) {
        this.teacher = teacher;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_login = findViewById(R.id.email_login);
        email_password = findViewById(R.id.email_password);
        login_button = findViewById(R.id.btn_login);
        register_txt = findViewById(R.id.register_txt);
        database = FirebaseDatabase.getInstance();

        auth = FirebaseAuth.getInstance();
        teacher = false;

        register_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(  email_login.getText().toString().isEmpty()  ||  email_password.getText().toString().isEmpty()  )

                    Toast.makeText(LoginActivity.this, "Field cannot be empty", Toast.LENGTH_LONG).show();
                else
                    auth.signInWithEmailAndPassword(email_login.getText().toString(), email_password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        ref = database.getReference("Users").child(auth.getCurrentUser().getUid());
                                        ref.addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {
                                                setTeacher(snapshot.child("teacher").getValue(Boolean.class));
                                                if(teacher){
                                                    Intent intent;
                                                    intent = new Intent(LoginActivity.this, PersonsJournalActivity.class);
                                                    startActivity(intent);
                                                }
                                                else{
                                                    Intent intent;
                                                    intent = new Intent(LoginActivity.this, JournalActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError error) {
                                                Toast.makeText(LoginActivity.this, "Что-то пошло не так...", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }
                                    else
                                        Toast.makeText(LoginActivity.this,"You have some errors", Toast.LENGTH_SHORT).show();
                                }
                            });



            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}