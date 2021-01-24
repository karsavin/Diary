package com.example.MyFirstProject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.MyFirstProject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Calendar calendar;
    private RecyclerView rv;
    private FirebaseDatabase dataBase;
    private MyAdapter adapter;
    private List<Note> listOfNotes;
    private Button btnDate;
    private final String DATA_PATTERN = "dd MMMM yyyy";
    private DatePickerDialog dateDialog;
    private Button btnNewNote;
    private TextView titleDate;
    private String[] strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//init()
        init();
        RecyclerView.LayoutManager l_manager = new GridLayoutManager(getApplicationContext(), 2);

        rv.setLayoutManager(l_manager);

        createDatePicker();

        btnDate.setOnClickListener(v -> {
            dateDialog.show();
        });

        btnNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                startActivity(intent);
            }
        });
    }
    //Метод, который возвращает дату в формате dd MMMM yyyy от календаря
    private String getDateFromCalendar() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_PATTERN);
        return sdf.format(calendar.getTime());
    }

    private void getDataFromDB(String date){
        List<Note> list = new ArrayList<>();
        //Получаем базу данных с датой, полученной от DatePickerDialog
        DatabaseReference dataBaseObj = dataBase.getReference(date);

        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Заполняем лист объектами Note из базы данных
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Note note = ds.getValue(Note.class);
                    assert note != null;
                    list.add(note);
                }

                compareTwoLists((ArrayList<Note>) listOfNotes, (ArrayList<Note>)list);

                //Cоздаём адаптер с полученным листом
                adapter = new MyAdapter((ArrayList<Note>) listOfNotes, MainActivity.this);
                //Добавляем адаптер в RecyclerView
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        dataBaseObj.addValueEventListener(vListener);
    }
    //Инициализируем переменные
    private void init(){
        dataBase = FirebaseDatabase.getInstance();
        btnDate = findViewById(R.id.btn_date);
        rv = findViewById(R.id.rv);
        calendar = Calendar.getInstance();
        btnNewNote = findViewById(R.id.btn_new_note);
        titleDate = findViewById(R.id.et_title_date);
        strings = getResources().getStringArray(R.array.hourslist);
    }
    //Создаем календарь для выбора даты
    private void createDatePicker(){
        DatePickerDialog.OnDateSetListener dateListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String date = getDateFromCalendar();
            createList(date);
            titleDate.setText(date);
            getDataFromDB(date);
        };

        dateDialog = new DatePickerDialog(MainActivity.this, dateListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }
    //Заполняем лист пустых элементов, которые будут показаны в RecyclerView
    private void createList(String date){
        listOfNotes = new ArrayList<>();

        for (int i = 0 ; i < 24 ; i++){
            int k = i+1;
            listOfNotes.add(new Note(strings[i], "-", date, " "));
        }
    }
    //Метод по замене пустых элементов на заметки, созданные пользователями
    public static void compareTwoLists(ArrayList<Note> list, ArrayList<Note> list2){
        for(int i = 0; i < list.size(); i++){
            for(int j = 0; j < list2.size(); j++){
                if(list.get(i).getTime().equals(list2.get(j).getTime())){
                    list.set(i, list2.get(j));
                }
            }
        }
    }
}