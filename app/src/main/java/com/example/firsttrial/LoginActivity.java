package com.example.firsttrial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firsttrial.dataClasses.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.signin)
    Button signin;
    @BindView(R.id.email)
    EditText emailTV;
    @BindView(R.id.password)
    EditText passwordTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vhoda);

        Button openRegistrationButton = findViewById(R.id.registrationButton);
        openRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegistrationActivity
                .class);
                startActivityForResult(intent,MainActivity.LOGIN_REQUEST_CODE);
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailTV.getText().toString();
                MainActivity.firebaseDatabase.getReference("user").child(Utils.md5Custom(email))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = (User) dataSnapshot.getValue(User.class);
                                if(user == null){
                                    Toasty.error(getApplicationContext(),"Неправильное имя пользователя или пароль").show();
                                    return;
                                }
                                Intent intent = new Intent();
                                intent.putExtra("user",user);
                                setResult(RESULT_OK, intent);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toasty.error(getApplicationContext(),
                                        "Нет соединения с сервером").show();
                            }
                        });

            }


        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}



