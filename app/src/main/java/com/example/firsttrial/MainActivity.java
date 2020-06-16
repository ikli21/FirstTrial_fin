package com.example.firsttrial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.firsttrial.dataClasses.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public final static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public final static int LOGIN_REQUEST_CODE = 8392;
    @BindView(R.id.dateNow)
    TextView dateNowTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("appdata", MODE_PRIVATE);
        ButterKnife.bind(this);

        if (!isAuth()){
            Intent intent = new Intent (MainActivity.this,LoginActivity.class );
            startActivityForResult(intent, LOGIN_REQUEST_CODE);
        }

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        dateNowTV.setText(dateText);
    }
    User user;
    SharedPreferences sharedPreferences;
    private boolean isAuth(){
        String email = sharedPreferences.getString("email",null);
        if(email == null)
            return false;
        else{
            getUserData(email);
            return true;
        }


    }
    private void getUserData(String email){
        firebaseDatabase.getReference("user").child(Utils.md5Custom(email))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user=dataSnapshot.getValue(User.class);
                        finishLoadActivity(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if (requestCode==LOGIN_REQUEST_CODE){
                if (data==null)
                    return;
                user=(User) data.getExtras().get("user");
                if(user==null)
                    return;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", user.getEmail());
                editor.apply();
                finishLoadActivity(user);
            }
        }
    }

    private void finishLoadActivity(User user){
        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
        finish();
    }
}

