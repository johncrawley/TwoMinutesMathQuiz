package com.jcrawley.tmmq;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity {

    private TextView questionTextView;
    private boolean bound = false;
    private GameService gameService;
    private AtomicBoolean isGameStarted = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionTextView = findViewById(R.id.questionText);
        setupStartButton();
     }


    public void setQuestionText(String questionText){
       questionTextView.setText(questionText);
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService.
        Intent intent = new Intent(this, GameService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private void setupStartButton(){
        Button button = findViewById(R.id.startGameButton);
        button.setOnClickListener(v -> {
            if(isGameStarted.get()){
                return;
            }
            gameService.startGame();
            isGameStarted.set(true);
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        bound = false;
    }


    private final ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            GameService.LocalBinder binder = (GameService.LocalBinder) service;
            gameService = binder.getService();
            gameService.setActivity(MainActivity.this);
            bound = true;
        }


        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };


}