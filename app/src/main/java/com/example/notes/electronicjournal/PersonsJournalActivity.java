package com.example.notes.electronicjournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PersonsJournalActivity extends AppCompatActivity {
    TextView journal_person;
    DatabaseReference ref,delRef,emRef;
    FirebaseDatabase database;
    private String person_name,email;
    FirebaseAuth auth;
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Person, PersonAdapter.ViewHolder> adapter;
    private static long back_pressed;
    String pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons_journal);

        journal_person = findViewById(R.id.journal_persons_user_name);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users").child(auth.getCurrentUser().getUid());


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                person_name = snapshot.child("name").getValue().toString();
                email = snapshot.child("email").getValue().toString();
                journal_person.setText(person_name);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(PersonsJournalActivity.this, "Что-то пошло не так...", Toast.LENGTH_LONG).show();
            }
        });


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .limitToLast(50);

        FirebaseRecyclerOptions<Person> options =
                new FirebaseRecyclerOptions.Builder<Person>()
                        .setQuery(query, Person.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Person, PersonAdapter.ViewHolder>(options) {
            @NotNull
            @Override
            public PersonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_person, parent, false);

                return new PersonAdapter.ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(PersonAdapter.ViewHolder holder, int position, Person model) {

                TextView person_name,teach,class_name;
                ImageView delete_person,edit_person;
                person_name = holder.itemView.findViewById(R.id.person_name);
                teach = holder.itemView.findViewById(R.id.teacher);
                class_name = holder.itemView.findViewById(R.id.class_name);
                delete_person = holder.itemView.findViewById(R.id.delete_person_btn);
                edit_person = holder.itemView.findViewById(R.id.edit_person_btn);
                person_name.setText(model.getName());
                teach.setText(model.getClass_name());
                if(model.isTeacher())
                    teach.setText("Teacher");
                else
                    teach.setText("Student");

                if(email == model.getEmail().toString()){
                    delete_person.setEnabled(false);
                    delete_person.setVisibility(View.INVISIBLE);
                }
                else{
                delete_person.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("nameArg",model.getName());
                        FragmentManager manager = getSupportFragmentManager();
                        DeletePersonFragment myPersonFragment = new DeletePersonFragment();
                        myPersonFragment.setArguments(bundle);
                        myPersonFragment.show(manager, "myPerson");
                        DatabaseReference itemRef = getRef(position);
                        delRef = itemRef;
                    }

                });}
                if(email == model.getEmail().toString()){
                    edit_person.setEnabled(false);
                    edit_person.setVisibility(View.INVISIBLE);
                }
                else{
                edit_person.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference itemRef = getRef(position);
                        Intent intent = new Intent(PersonsJournalActivity.this,JournalActivity.class);
                        intent.putExtra("personKey",itemRef.getKey());
                        intent.putExtra("personName",model.getName());
                        intent.putExtra("isTeacher", true);
                        startActivity(intent);
                    }
                });}

            }
        };

        recyclerView = findViewById(R.id.recycle_journal_person);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            auth.signOut();
            super.onBackPressed();
            Intent intent = new Intent(PersonsJournalActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "Нажмите еще раз для выхода!", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
    public void okClicked() {
        Toast.makeText(getApplicationContext(), "Пользователь удален",
                Toast.LENGTH_SHORT).show();
        delRef.removeValue();
    }

    public void cancelClicked() {
        Toast.makeText(getApplicationContext(), "Отменено!",
                Toast.LENGTH_SHORT).show();
    }
}