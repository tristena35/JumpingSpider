package com.aguilartristen.jumpingspider;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.VideoView;


public class MainActivity extends AppCompatActivity {

    //Buttons
    private Button mStartButton;
    private Button mOptionsButton;
    //private ImageButton mMusicButton;

    //Media
    MediaPlayer spiderManTheme;

    //VideoView
    private VideoView videoViewBackground;
    private String videoPath;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This was not working because its a .mov file
        /*Setting up Background Video Animation
        videoViewBackground = findViewById(R.id.main_page_background_animation);
        videoPath = "android.resource://"+getPackageName()+"/"+R.raw.spiderman_background_video;
        uri = Uri.parse(videoPath);
        videoViewBackground.setVideoURI(uri);
        videoViewBackground.start();

        videoViewBackground.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoViewBackground.start();
            }
        });*/

        //Media
        spiderManTheme = MediaPlayer.create(this, R.raw.spider_man_themesong2);
        spiderManTheme.setLooping(true);
        spiderManTheme.start();

        //Buttons
        mStartButton = (Button)findViewById(R.id.game_over_restart_btn);
        mOptionsButton = (Button)findViewById(R.id.main_options_btn);
        //mMusicButton = (ImageButton)findViewById(R.id.main_music_on_off_btn);

        //Start Game
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //spiderManTheme.release();
                startGame();
            }
        });

        //Options
        mOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //spiderManTheme.release();
                optionsPage();
            }
        });

        /*mMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Stop Music
                spiderManTheme.release();
            }
        });*/
    }

    public void startGame(){
        Intent gameViewIntent = new Intent(this, GameView.class);
        startActivity(gameViewIntent);
        //finish();
        Log.d("startGame()", "Executed");
    }

    public void optionsPage(){
        Intent optionsIntent = new Intent(this, OptionsActivity.class);
        startActivity(optionsIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();

        spiderManTheme.release();
    }
}
