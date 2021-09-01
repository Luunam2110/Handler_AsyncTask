package com.example.handler_asynctask;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private boolean isruning = false;
    private Handler handler;

    void initHander() {
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 200: {
                        number.setText("" + msg.arg1);
                        Log.v("MyActivity", msg.arg1+"");
                        break;
                    }
                    case 201: {
                        number.setText("Finish");
                        isruning = false;
                        break;
                    }
                    default:
                        break;
                }
            }
        };
    }

    private TextView number;
    private ProgressBar progressBar;
    private Button startHandler, startAsyncTask;
    private boolean progessRuning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mapView();
        initHander();
        startHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isruning) {
                    Snackbar.make(v, "Starting demo Handler...", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    updateNumber();
                    isruning = true;
                }
                else {
                    Snackbar.make(v,"handler is  " +
                            "runing...",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        startAsyncTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!progessRuning){
                    Snackbar.make(v, "Starting demo AsyncTask...", Snackbar.LENGTH_SHORT).show();
                    new DemoAsyncTask().execute();
                    progessRuning= true;
                }
                else{
                    Snackbar.make(v,"AsyncTask is runing...", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void mapView() {
        number = (TextView) findViewById(R.id.textview_first);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        startAsyncTask = (Button) findViewById(R.id.button_start_asynctask);
        startHandler = (Button) findViewById(R.id.button_start_handler);
    }

    void updateNumber() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i += 10) {
                    try {
                        Thread.sleep(300);
                    } catch (Exception e) {
                    }
                    Message message = new Message();
                    message.what = 200;
                    message.arg1 = i;
                    handler.sendMessage(message);
                }
                handler.sendEmptyMessage(201);
            }
        }).start();
    }

    private class DemoAsyncTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onProgressUpdate(Integer... values) {
            int value = values[0];
            progressBar.setProgress(value);
            Log.v("MyActivity", value+"");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progessRuning = false;
            Toast.makeText(MainActivity.this,"Finish progress!",Toast.LENGTH_LONG).show();
            //Snackbar.make(getWindow().getDecorView().getRootView(), "Finish progress!", Snackbar.LENGTH_LONG).show();
            progressBar.setProgress(0);
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i=0;i<=100;i++){
                publishProgress(i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }
    }

}