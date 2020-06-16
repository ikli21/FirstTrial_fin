package com.example.firsttrial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    public final static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public final static int LOGIN_REQUEST_CODE = 8392;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!isAuth()){
            Intent intent = new Intent (MainActivity.this,LoginActivity.class );
            startActivityForResult(intent, LOGIN_REQUEST_CODE);
        }
    }

    private boolean isAuth(){
        //TODO обращение к кешированным данным и получение информации о юзере для авторизации
        //todo обращение к бд и получение всех данных о нём

        return false;
    }
}
