package com.example.yuki.time;

import android.app.DatePickerDialog;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;


public class InputActivity extends AppCompatActivity {

    InputData inputData = new InputData();
    Calendar calendar = Calendar.getInstance();
    int tmpYear = calendar.get(Calendar.YEAR);
    int tmpDay = calendar.get(Calendar.DATE);
    int tmpMonth = calendar.get(Calendar.MONTH);
    int tmpHour = calendar.get(Calendar.HOUR_OF_DAY);
    int tmpMinute = calendar.get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        inputData.set(tmpYear, tmpMonth + 1, tmpDay, tmpHour, tmpMinute);

        TextView textView = (TextView) findViewById(R.id.DateInput);
        textView.setText(String.format("%s年 %s月%02d日", tmpYear, tmpMonth+1, tmpDay));
        String text = textView.getText().toString();

        if(tmpHour<12) {
            textView = (TextView) findViewById(R.id.TimeInput);
            textView.setText(String.format("午前 %s時%02d分", tmpHour, tmpMinute));
        }else{
            textView = (TextView) findViewById(R.id.TimeInput);
            textView.setText(String.format("午後 %s時%02d分",   tmpHour-12, tmpMinute));
        }

        // Content Activity is a activity that is transferred from a list content.
                Intent resultIntent = new Intent(this, MainActivity.class);

        /*PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                        this,
                                        0,
                                        resultIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                        );

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Event Timer")
                        .setContentText("予定をわかりやすく管理！")
                        .setAutoCancel(true)
                        //.addAction(R.drawable.ic_check_light, "Pause",resultPendingIntent)
                        //.addAction(R.drawable.ic_plus_light, "Next", resultPendingIntent)
                        .setVibrate(new long[]{0, 100, 150, 100})
                        .setSound(notification);


        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;

        int mNotificationId = 001;



        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build()); */

        /*ツールバー上のバックキーを表示*/
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        /*決定ボタンの動作*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData.setTitle(((EditText)findViewById(R.id.TitleInput)).getText().toString());
                inputData.setMemo(((EditText) findViewById(R.id.MemoInput)).getText().toString());

                MainActivity.eventList.add(inputData);
                writeToJson();
                finish();
            }
        });

    }

    public void writeToJson() {
        Gson gson = new Gson();
        Type listtype = new TypeToken<List<InputData>>(){}.getType();
        String jsonstring = gson.toJson(MainActivity.eventList, listtype);

        OutputStream out;
        try {
            out = openFileOutput("events.json",MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out,"UTF-8"));

            //追記する
            writer.write(jsonstring);
            writer.close();
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }

    /*年月日入力の呼び出し*/
    public void showDatePicker (View view){
        DatePickerDialog();
    }

    /*時間入力の呼び出し*/
    public void showTimePicker (View view){
        TimePickerDialog(false);
    }

    /*年月日入力の処理*/
    private void DatePickerDialog(){



        DatePickerDialog dlgDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public  void onDateSet(DatePicker view,int year, int month, int day) {
                TextView textView = (TextView) findViewById(R.id.DateInput);
                inputData.setInputYear(year);
                inputData.setInputMonth(month + 1);
                inputData.setInputDay(day);
                textView.setText(String.format("%s年 %s月%02d日", year, month+1, day));
                tmpYear = year;
                tmpMonth = month;
                tmpDay = day;

                String text = textView.getText().toString();
            }
        },tmpYear,tmpMonth,tmpDay);
        dlgDatePicker.show();
    }

    /*時間入力の処理*/
    private void TimePickerDialog(boolean isUse24hour){
        TimePickerDialog dlgTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                inputData.setInputHour(hour);
                inputData.setInputMinute(minute);
                tmpHour = hour;
                tmpMinute = minute;

                if(hour < 12) {
                    TextView textView = (TextView) findViewById(R.id.TimeInput);
                    textView.setText(String.format("午前 %s時%02d分", hour, minute));
                }else{
                    TextView textView = (TextView) findViewById(R.id.TimeInput);
                    textView.setText(String.format("午後 %s時%02d分", hour-12, minute));
                }

            }
        },tmpHour,tmpMinute,isUse24hour);
        dlgTimePicker.show();
    }

}
