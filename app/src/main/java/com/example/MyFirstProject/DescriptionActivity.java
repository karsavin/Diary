package com.example.MyFirstProject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.MyFirstProject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DescriptionActivity extends AppCompatActivity {
    private Note note;
    private TextView tvName;
    private TextView tvTime;
    private TextView tvDate;
    private TextView tvDescr;
    private String date;
    private Button btnBack;
    private Button btnDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descr_activity);
        //Инициализируем переменные
        init();

        Bundle args = getIntent().getExtras();
        if (args != null){
            note = (Note) args.getSerializable(Note.class.getSimpleName());
        }

        assert note != null;
        tvName.setText(note.getName());
        tvTime.setText(note.getTime());
        tvDate.setText(note.getDate());
        tvDescr.setText(note.getDescription());
        date = note.getDate();
        DatabaseReference dataBase = FirebaseDatabase.getInstance().getReference(date);


        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dataBase.orderByChild("name").equalTo(note.getName()).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot child: dataSnapshot.getChildren()) {
                                    child.getRef().setValue(null);
                                }
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                            }
                        });

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void init(){
        tvTime = findViewById(R.id.time_tv);
        tvName = findViewById(R.id.name_tv);
        tvDate = findViewById(R.id.date_tv);
        tvDescr = findViewById(R.id.descr_tv);
        btnBack = findViewById(R.id.btn_back2);
        btnDel = findViewById(R.id.btn_del);
    }
}