package com.example.firsttrial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.firsttrial.dataClasses.Mail;
import com.example.firsttrial.dataClasses.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity {
    User user;
    HeaderFragment fragment;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FloatingActionButton newMessageBtn;
    ListView adapterField;
    MailAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        user = (User) Objects.requireNonNull(getIntent().getExtras()).get("user");
        getSupportActionBar().hide();
        fragment = new HeaderFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.socket, fragment);
        transaction.commit();
        newMessageBtn = findViewById(R.id.newMessageBtn);
        newMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewMessageDialog();
            }
        });
        adapterField = findViewById(R.id.field);
        adapter = new MailAdapter(new ArrayList<Mail>(), this);
        adapterField.setAdapter(adapter);
        addListener();
    }
    private void createNewMessageDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        final View root = getLayoutInflater().inflate(R.layout.dialogue_layout,null);
        builder.setTitle("Новое сообщение").setView(root)
                .setPositiveButton("Создать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        TextView recipientTV = root.findViewById(R.id.recipient);
                        TextView titleTV = root.findViewById(R.id.title);
                        TextView textTV = root.findViewById(R.id.text);
                    final String recipient = recipientTV.getText().toString();
                    final String title = titleTV.getText().toString();
                    final String text = textTV.getText().toString();
                    pushNewMessage(title,text,recipient);

                    }

                });

    }
    private void pushNewMessage(final String title, final String text, final String recipient) {
        database.getReference("user")
                .child(Utils.md5Custom(recipient))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue()==null){
                            Toasty.error(getApplicationContext(),"Данного юзера не существует").show();
                            return;
                        }
                        Mail mail = new Mail(title,text,user.getEmail(),recipient);
                        database.getReference().child("mail").child(Utils.md5Custom(recipient))
                                .child(String.valueOf(Long.MAX_VALUE-mail.getDateInMS()))
                                .setValue(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toasty.success(getApplicationContext(),"Отправлено").show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void addListener() {
        database.getReference("mail").child(Utils.md5Custom(user.getEmail()))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int cNew=0;
                        int cOld=0;
                        int cAll=0;
                        List<Mail> mails = new ArrayList<>();
                        for (DataSnapshot elem : dataSnapshot.getChildren()){
                            Mail mail = elem.getValue(Mail.class);
                            mails.add(mail);
                            if(mail.isChecked()){
                                cOld++;
                            } else{cNew++;}
                            cAll++;

                        }
                        adapter.setMails(mails);
                        adapter.notifyDataSetChanged();
                        fragment.setCounterNew(cNew);
                        fragment.setCounterAll(cAll);
                        fragment.setCounterOld(cOld);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    }

