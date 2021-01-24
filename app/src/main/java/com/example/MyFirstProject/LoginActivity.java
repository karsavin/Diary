package com.example.MyFirstProject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.MyFirstProject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnEnter;
    private Button btnRegister;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(etEmail.getText().toString()) && !TextUtils.isEmpty(etPassword.getText().toString())){
                    auth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(LoginActivity.this, "Пользователь зарегистрирован", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Пользователь уже зарегистрирован", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(LoginActivity.this, "Пожалуйста, заполните поля", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(etEmail.getText().toString()) && !TextUtils.isEmpty(etPassword.getText().toString())){
                    auth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Пользователь вошёл успешно", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }else{
                                Toast.makeText(LoginActivity.this, "Пользователь не вошёл", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(LoginActivity.this, "Пожалуйста, заполните поля", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void init(){
        btnEnter = findViewById(R.id.enter_btn);
        btnRegister = findViewById(R.id.register_btn);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null){
            Toast.makeText(this, "Пользователь найден", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
        }
    }
}