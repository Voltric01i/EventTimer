package com.example.yuki.time;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;



public class MainActivity extends AppCompatActivity {

    static ArrayList<InputData> eventList = new ArrayList<>();
    static InputData tmp = null;
    static boolean stopFlag = false;


    private GoogleApiClient client;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    private String readFromJson() {

        InputStream in;
        String lineBuffer;
        StringBuilder sb = new StringBuilder();
        String retval = "";
        try {
            in = openFileInput("events.json"); //LOCAL_FILE = "log.txt";

            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while ((lineBuffer = reader.readLine()) != null) {
                sb.append(lineBuffer + "\n");
            }
            retval = sb.toString();
            Gson gson = new Gson();
            Type listtype = new TypeToken<List<InputData>>() {
            }.getType();
            eventList = gson.fromJson(retval, listtype);
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return retval;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         /*入力画面ボタンの動作*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InputActivity.class);
                startActivity(intent);
            }

        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onResume() {

        super.onResume();
        //TextView eventView = (TextView) findViewById(R.id.EventView);
        //eventView.setText(readFromJson());
        readFromJson();

        mRecyclerView = (RecyclerView) findViewById(R.id.Event_layout);
        // コンテンツの変化でRecyclerViewのサイズが変わらない場合は、
        // パフォーマンスを向上させることができる
        //mRecyclerView.setHasFixedSize(true);

        // LinearLayoutManagerを使用する
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // アダプタを指定する
        mAdapter = new FileAdapter(this, eventList);
        mRecyclerView.setAdapter(mAdapter);

       final Handler _handler = new Handler();

        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {

               // if (!stopFlag) {
                mAdapter.notifyDataSetChanged();

                _handler.postDelayed(this, 1000);
                //}
            }
        }, 1000);


        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(mRecyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (final int position : reverseSortedPositions) {
                                    final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coord);
                                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "削除しました", Snackbar.LENGTH_SHORT);
                                    tmp = eventList.get(position);
                                    snackbar.setAction("取り消し", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(getApplicationContext(), "取り消ししました", Toast.LENGTH_SHORT).show();
                                            if(tmp !=null){
                                                eventList.add(tmp);
                                                mAdapter.notifyDataSetChanged();
                                                writeToJson();
                                            }
                                        }
                                    });
                                    snackbar.show();
                                    eventList.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                    writeToJson();
                                }
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (final int position : reverseSortedPositions) {
                                    tmp = eventList.get(position);
                                    final CoordinatorLayout coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coord);
                                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "削除しました", Snackbar.LENGTH_SHORT);
                                    snackbar.setAction("取り消し", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Toast.makeText(getApplicationContext(), "取り消ししました", Toast.LENGTH_SHORT).show();
                                            if(tmp !=null){
                                                eventList.add(tmp);
                                                mAdapter.notifyDataSetChanged();
                                                writeToJson();
                                            }
                                        }
                                    });
                                    snackbar.show();
                                    eventList.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                    writeToJson();
                                }
                            }
                        }) {
                  /*  @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent motionEvent) {
                        switch (motionEvent.getAction())
                        {
                            case MotionEvent.ACTION_DOWN:
                                stopFlag = true;
                                break;
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_UP:
                                stopFlag = false;
                                break;
                        }
                        super.onTouchEvent(rv, motionEvent);
                    }*/

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                };

        mRecyclerView.addOnItemTouchListener(swipeTouchListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.yuki.time/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.yuki.time/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


}



