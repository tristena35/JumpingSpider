package com.aguilartristen.jumpingspider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    private TextView mScoreLabel;
    private TextView mHighScoreLabel;

    private Button mRestartButton;
    private Button mMenuButton;

    private int score;

    //Media
    MediaPlayer spiderManThemeDies;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        //Media
        spiderManThemeDies = MediaPlayer.create(this, R.raw.spiderman_themesong_dies_2);
        spiderManThemeDies.setLooping(true);
        spiderManThemeDies.start();

        //TextViews
        mScoreLabel = (TextView) findViewById(R.id.game_over_score);
        mHighScoreLabel = (TextView) findViewById(R.id.game_over_high_score);

        //Buttons
        mRestartButton = (Button) findViewById(R.id.game_over_restart_btn);
        mMenuButton = (Button) findViewById(R.id.game_over_menu_btn);


        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameViewPage = new Intent(GameOverActivity.this, GameView.class);
                startActivity(gameViewPage);
                finish();
            }
        });

        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homePage = new Intent(GameOverActivity.this, MainActivity.class);
                startActivity(homePage);
                finish();
            }
        });




        score = getIntent().getIntExtra("SCORE", 0);
        mScoreLabel.setText(score + "");

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE", 0);

        if(score > highScore){
            mHighScoreLabel.setText("High Score: " + score);

            //Save New Score
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();
        }else{
            mHighScoreLabel.setText("High Score: " + highScore);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        spiderManThemeDies.release();
    }
}
