package com.example.firsttrial;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firsttrial.dataClasses.Mail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

public class MailAdapter extends BaseAdapter {
    List<Mail> mails = new ArrayList<>();
    Context  context;
    LayoutInflater inflater;
    public MailAdapter(List<Mail> mails, Context context){
        this.mails = mails;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mails.size();
    }

    @Override
    public Object getItem(int position) {
        return mails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    Calendar calendar = Calendar.getInstance();

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Mail mail = mails.get(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.mail_adapter,parent,false);

        }

        TextView isNewMessageText = convertView.findViewById(R.id.isNewMessage);
        ImageView isNewMessageImage = convertView.findViewById(R.id.image);
        TextView title = convertView.findViewById(R.id.title);
        TextView date = convertView.findViewById(R.id.dateMessage);
        if(mail.isChecked()){
            isNewMessageImage.setImageResource(R.drawable.ic_drafts_black_24dp);
            isNewMessageText.setText("Прочитано");
            isNewMessageText.setTextColor(Color.GRAY);
        }else{
            isNewMessageImage.setImageResource(R.drawable.ic_fiber_new_black_24dp);

        }
        title.setText(mail.getTitle());
        calendar.setTimeInMillis(mail.getDateInMS());
        date.setText(calendar.getTime().toString());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MailActivity.class);
                intent.putExtra("mail",mail);
                context.startActivity(intent);
            }
        });


        return convertView;
    }
    void setMails(List<Mail> mails){this.mails = mails;}
}
