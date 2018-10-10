package com.aguilartristen.jumpingspider;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class StartGameActivity extends AppCompatActivity {

    private GameView mGameView;
    private Handler mHandler = new Handler();
    private final static long TIME_INTERVAL = 30;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
