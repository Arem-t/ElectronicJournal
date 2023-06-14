package com.example.notes.electronicjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText register_login, register_password,reg_name;
    private Button register_button;
    private FirebaseAuth rauth;
    private DatabaseReference referencedatabase;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        rauth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        referencedatabase = database.getReference();

        reg_name = findViewById(R.id.student_name);
        register_button = findViewById(R.id.btn_register);
        register_login = findViewById(R.id.register_login);
        register_password = findViewById(R.id.register_password);



        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(register_login.getText().toString().isEmpty() || register_password.getText().toString().isEmpty() || reg_name.getText().toString().isEmpty())
                    Toast.makeText(RegisterActivity.this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
                else {
                    rauth.createUserWithEmailAndPassword(register_login.getText().toString(),register_password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Successful", Toast.LENGTH_LONG).show();
                                        referencedatabase.child("Users").child(rauth.getCurrentUser().getUid()).child("email").setValue(register_login.getText().toString());
                                        referencedatabase.child("Users").child(rauth.getCurrentUser().getUid()).child("password").setValue(register_password.getText().toString());
                                        referencedatabase.child("Users").child(rauth.getCurrentUser().getUid()).child("name").setValue(reg_name.getText().toString());
                                        referencedatabase.child("Users").child(rauth.getCurrentUser().getUid()).child("class_name").setValue("ДЖТХ-01-16");
                                        referencedatabase.child("Users").child(rauth.getCurrentUser().getUid()).child("teacher").setValue(Boolean.FALSE);


                                        Intent intent = new Intent(RegisterActivity.this, JournalActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(RegisterActivity.this,"You have some errors", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}