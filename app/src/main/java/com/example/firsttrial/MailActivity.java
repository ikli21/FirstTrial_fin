package com.example.firsttrial;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firsttrial.dataClasses.Mail;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MailActivity extends AppCompatActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.text)
    TextView text;

    Mail mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialogue_layout);
    mail = (Mail) getIntent().getExtras().get("mail");
        ButterKnife.bind(this);
        title.setText(mail.getTitle());
        text.setText(mail.getMessage());

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("mail").child(Utils.md5Custom(mail.getRecipient()))
                .child(String.valueOf(Long.MAX_VALUE-mail.getDateInMS())).child("checked").setValue(true);
    }
}
