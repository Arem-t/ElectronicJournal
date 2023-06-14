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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
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
import com.google.firebase.firestore.bundle.BundledQuery;

import org.jetbrains.annotations.NotNull;


import static android.graphics.Color.rgb;

public class JournalActivity extends AppCompatActivity {

    String editRef;
    TextView journal_student;
    DatabaseReference ref,delRef;
    FirebaseDatabase database;
    FirebaseAuth auth;
    private String student_name, personKey, personName;
    private RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    FirebaseRecyclerAdapter<Record, RecordAdapter.ViewHolder> adapter;
    private static long back_pressed;
    Bundle b;
    Boolean teacher;


    public Boolean getTeacher() {
        return teacher;
    }

    public void setTeacher(Boolean teacher) {
        this.teacher = teacher;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        journal_student = findViewById(R.id.journal_student_name);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users").child(auth.getCurrentUser().getUid());
        floatingActionButton = findViewById(R.id.reg_fab);
        floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(25, 82, 108)));
        floatingActionButton.setColorFilter(Color.rgb(252, 247, 248));
        teacher=false;


        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
             personKey = arguments.getString("personKey");
             personName = arguments.getString("personName");
             teacher = arguments.getBoolean("isTeacher");
        }


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JournalActivity.this, AddRecordActivity.class);
                intent.putExtra("personKey",personKey);
                intent.putExtra("personName",personName);
                startActivity(intent);
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                 setTeacher(snapshot.child("teacher").getValue(Boolean.class));
                if(!teacher){
                    student_name = snapshot.child("name").getValue().toString();
                    journal_student.setText(student_name);
                    floatingActionButton.setEnabled(false);
                floatingActionButton.setVisibility(View.GONE);}
                else
                    journal_student.setText(personName);
            }



            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(JournalActivity.this, "Что-то пошло не так...", Toast.LENGTH_LONG).show();
            }
        });


        Query query;
        if(teacher){
                    query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users").child(personKey).child("Lessons")
                    .limitToLast(50);
        }
        else{
                    query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users").child(auth.getCurrentUser().getUid()).child("Lessons")
                    .limitToLast(50);
        }



        FirebaseRecyclerOptions<Record> options =
                new FirebaseRecyclerOptions.Builder<Record>()
                        .setQuery(query, Record.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Record, RecordAdapter.ViewHolder>(options) {
            @NotNull
            @Override
            public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item, parent, false);

                return new RecordAdapter.ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(RecordAdapter.ViewHolder holder, int position, Record model) {

                TextView lesson_name,lesson_date,lesson_grade;
                ImageView edit_btn, delete_btn;
                lesson_name = holder.itemView.findViewById(R.id.lesson_name_txt);
                lesson_date = holder.itemView.findViewById(R.id.date_txt);
                lesson_grade = holder.itemView.findViewById(R.id.grade_txt);
                edit_btn = holder.itemView.findViewById(R.id.edit_record_btn);
                delete_btn = holder.itemView.findViewById(R.id.delete_record_btn);
                lesson_name.setText(model.getLesson_name());
                lesson_date.setText(model.getDate());
                lesson_grade.setText(model.getGrade());

                edit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference itemRef = getRef(position);
                        Intent intent = new Intent(JournalActivity.this,EditRecordActivity.class);
                        intent.putExtra("lesson",model.getLesson_name());
                        intent.putExtra("date",model.getDate());
                        intent.putExtra("grade",model.getGrade());
                        intent.putExtra("position",position);
                        editRef = itemRef.getKey();

                        intent.putExtra("ref",editRef.toString());
                        intent.putExtra("personKey",personKey);
                        intent.putExtra("personName",personName);

                        startActivity(intent);
                    }
                });

                delete_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference itemRef = getRef(position);
                        Bundle bundle = new Bundle();
                        bundle.putString("nameArg",model.getLesson_name());
                         FragmentManager manager = getSupportFragmentManager();
                         DeleteDialogFragment myDialogFragment = new DeleteDialogFragment();
                         myDialogFragment.setArguments(bundle);
                         myDialogFragment.show(manager, "myDialog");
                         delRef = itemRef;
                    }
                });
                if(!teacher){
                    delete_btn.setVisibility(View.GONE);
                    delete_btn.setEnabled(false);
                    edit_btn.setVisibility(View.GONE);
                    edit_btn.setEnabled(false);
                }
            }
        };

        recyclerView = findViewById(R.id.recycle_journal);
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

        ref = database.getReference("Users").child(auth.getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                setTeacher(snapshot.child("teacher").getValue(Boolean.class));
                if(teacher){
                    if (back_pressed + 2000 > System.currentTimeMillis()) {
                        Intent intent = new Intent(JournalActivity.this, PersonsJournalActivity.class);
                        startActivity(intent);
                    }else
                        Toast.makeText(getBaseContext(), "Нажмите еще раз для выхода в список студентов!", Toast.LENGTH_SHORT).show();

                }
                else{
                    if (back_pressed + 2000 > System.currentTimeMillis()) {
                        Intent intent = new Intent(JournalActivity.this, MainActivity.class);
                        auth.signOut();
                        startActivity(intent);
                    }else
                        Toast.makeText(getBaseContext(), "Нажмите еще раз для выхода!", Toast.LENGTH_SHORT).show();

                }
                back_pressed = System.currentTimeMillis();

            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(JournalActivity.this, "Что-то пошло не так...", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void okClicked() {
        delRef.removeValue();
    }

    public void cancelClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку отмены!",
                Toast.LENGTH_LONG).show();
    }
}