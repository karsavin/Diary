package com.example.MyFirstProject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MyFirstProject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewNoteActivity extends AppCompatActivity {

    private Calendar calendar;
    private DatePickerDialog dateDialog;
    private String dateNote;
    private final String DATA_PATTERN = "dd MMMM yyyy";
    private TextView etDate;
    private EditText etName;
    private DatabaseReference dataBase;

    private Button btnCreateNote;
    private Button btnBack;

    private EditText etHour1;
    private EditText etHour2;
    private TextView tvMin1;
    private TextView tvMin2;
    private EditText etDescr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note_activity);
        init();


        createDatePicker();
        dateDialog.show();

        btnCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hours1 = etHour1.getText().toString();
                String minutes1 = tvMin1.getText().toString();
                String hours2 = etHour2.getText().toString();
                String minutes2 = tvMin2.getText().toString();
                String name = etName.getText().toString();
                String descr = etDescr.getText().toString();

                //Выбрасываем неподходящие значения
                if (TextUtils.isEmpty(hours1) || TextUtils.isEmpty(minutes1) || TextUtils.isEmpty(hours2) || TextUtils.isEmpty(minutes2) || TextUtils.isEmpty(name) || TextUtils.isEmpty(descr)){
                    Toast.makeText(getApplicationContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                }else{
                    if(hours1.length()<2 || hours2.length()<2){
                        Toast.makeText(getApplicationContext(), "Неправильный формат времени. Пример: 01:00 - 02:00", Toast.LENGTH_SHORT).show();
                    }else{
                        String finalTime = hours1+minutes1+hours2+minutes2;
                        int hour1 = Integer.parseInt(hours1);
                        int hour2 = Integer.parseInt(hours2);
                        int h = hour2-hour1;
                        if(h != 1 & h != -23 ){
                            Toast.makeText(getApplicationContext(), "Неправильный формат времени. Пример: 01:00 - 02:00", Toast.LENGTH_SHORT).show();
                        }else{
                            dataBase.push().setValue(new Note(finalTime, name, dateNote, descr));
                            Toast.makeText(getApplicationContext(), "Заметка сохранена", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(NewNoteActivity.this, MainActivity.class));
                        }
                    }
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewNoteActivity.this, MainActivity.class));
            }
        });

    }

    private void createDatePicker(){
        DatePickerDialog.OnDateSetListener dateListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            dateNote = getDateFromCalendar();
            etDate.setText(dateNote);
            //Получаем DatabaseReference, то есть узел в firebase, значение которого равняется нашей дате.
            //Таким образом, получаем узел, который закреплен за определённой датой, в который помещаем наши Notes
            dataBase = FirebaseDatabase.getInstance().getReference(dateNote);
        };

        dateDialog = new DatePickerDialog(this , dateListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }
    //Метод, который возвращает дату в формате dd MMMM yyyy от календаря
    private String getDateFromCalendar() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_PATTERN);
        return sdf.format(calendar.getTime());
    }

    private void init(){
        calendar = Calendar.getInstance();
        btnCreateNote = findViewById(R.id.btn_create_note);
        etDate = findViewById(R.id.et_date);
        etName = findViewById(R.id.et_name);
        btnBack = findViewById(R.id.btn_back);
        etHour1 = findViewById(R.id.et_time1);
        etHour2 = findViewById(R.id.et_time2);
        tvMin1 = findViewById(R.id.minutes1);
        tvMin2 = findViewById(R.id.minutes2);
        etDescr = findViewById(R.id.et_description);
    }
}