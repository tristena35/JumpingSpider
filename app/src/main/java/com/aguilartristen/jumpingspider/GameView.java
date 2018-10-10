package com.aguilartristen.jumpingspider;

import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Timer;
import java.util.TimerTask;


public class GameView extends AppCompatActivity {

    //Frame Features
    private TextView mScoreLabel;
    private TextView mLevelLabel;
    private TextView mStartLabel;
    private ImageView mSpiderMan;
    private ImageView mEnemyObject;
    private ImageView mEnemyObject2;
    private ImageView mEnemyObject3;
    private ImageView mGoodBomb;
    private ImageView mVillainImage;
    private ImageView mLife1;
    private ImageView mLife2;
    private ImageView mLife3;
    private Button mHowToPlayBtn;

    //Size
    private int frameHeight;
    private int spidermanHeight;
    private int spidermanWidth;
    private int screenWidth;
    private int screenHeight;

    //Position
    private int spiderManY;
    private int enemyObjectX;
    private int enemyObjectY;
    private int enemyObject2X;
    private int enemyObject2Y;
    private int enemyObject3X;
    private int enemyObject3Y;
    private int goodBombX;
    private int goodBombY;

    //Speeds
    private int enemyObjectSpeed = 18;
    private int enemyObjectSpeed2 = 18;
    private int enemyObjectSpeed3 = 18;
    private int goodBombSpeed = 18;

    //Score
    private int score;

    //Lives
    private int lives = 3;

    //Level
    private int level = 1;

    //Handler and Timer
    private Handler mHandler = new Handler();
    private Timer mTimer = new Timer();

    //Status Check
    private boolean action_flg = false;
    private boolean start_flg = false;

    //Media
    private MediaPlayer spiderManFightTheme, spiderManFightThemeDocOck, spiderManFightThemeMysterio,
            spiderManFightThemeScorpian, spiderManFightThemeThanos, spiderManFightThemeVenom, spiderManFightThemeCarnage;
    private MediaPlayer webSound;

    //Pause Button
    private ImageView mPauseBtn;
    private int PAUSE_STATE = 0; //0 = Not Pause, 1 = Pause

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);

        //MediaPlayer
        //---Green Goblin
        spiderManFightTheme = MediaPlayer.create(this, R.raw.main_fight_theme_song);
        spiderManFightTheme.setLooping(true);
        spiderManFightTheme.start();
        //---Doc Ock
        spiderManFightThemeDocOck = MediaPlayer.create(this,R.raw.spiderman_themesong_doc_ock_fight);
        //---Mysterio
        spiderManFightThemeMysterio = MediaPlayer.create(this, R.raw.spiderman_themesong_mysterio);
        //---Scorpian
        spiderManFightThemeScorpian = MediaPlayer.create(this, R.raw.spiderman_themesong_scorpian);
        //---Venom
        spiderManFightThemeVenom = MediaPlayer.create(this, R.raw.spiderman_themesong_venom);
        //---Carnage
        spiderManFightThemeCarnage = MediaPlayer.create(this, R.raw.spiderman_themesong_carnage);
        //---Thanos
        spiderManFightThemeThanos = MediaPlayer.create(this, R.raw.spiderman_themesong_thanos);
        //---Web Shoot Sound
        webSound = MediaPlayer.create(this, R.raw.web_sound_effect);

        //On Screen Features
        mPauseBtn = (ImageView)findViewById(R.id.game_view_pause_btn);
        mScoreLabel = (TextView)findViewById(R.id.game_view_score);
        mLevelLabel = (TextView)findViewById(R.id.game_view_level);
        mStartLabel = (TextView)findViewById(R.id.game_view_start_label);
        mSpiderMan = (ImageView)findViewById(R.id.game_view_spiderman);
        mEnemyObject = (ImageView)findViewById(R.id.game_view_enemy_object);
        mEnemyObject2 = (ImageView)findViewById(R.id.game_view_enemy_object_2);
        mEnemyObject3 = (ImageView)findViewById(R.id.game_view_enemy_object_3);
        mGoodBomb = (ImageView)findViewById(R.id.game_view_good_bomb);
        mVillainImage = (ImageView)findViewById(R.id.game_view_villain_image);
        mLife1 = (ImageView)findViewById(R.id.game_view_life1);
        mLife2 = (ImageView)findViewById(R.id.game_view_life2);
        mLife3 = (ImageView)findViewById(R.id.game_view_life3);
        mHowToPlayBtn = (Button)findViewById(R.id.game_view_how_to_play_btn);

        //Get Screen Size
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        //Move to out of screen
        mEnemyObject.setX(-80);
        mEnemyObject.setY(-80);
        mEnemyObject2.setX(-80);
        mEnemyObject2.setY(-80);
        mEnemyObject3.setX(-80);
        mEnemyObject3.setY(-80);
        mGoodBomb.setX(-80);
        mGoodBomb.setY(-80);

        mScoreLabel.setText("0");

        mHowToPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameView.this, PopUpWindow.class));
            }
        });

    }

    public void changePos(){

        //Always check for contact
        hitCheck();

        //Check if user clicks pause
        mPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimer.cancel();
                PAUSE_STATE = 1; //Paused
                Intent pauseMenu = new Intent(GameView.this, PauseMenu.class);
                pauseMenu.putExtra("PAUSE_STATE", 1);
                startActivity(pauseMenu);
            }
        });

        //Check score for level to switch to Doc Ock | (Level 2)
        if(score == 100){
            enemyObjectX = (screenWidth + 1000);
            level += 1;
            score += 10;
            //Increase speed of bombs
            enemyObjectSpeed = 22;
            mVillainImage.setImageResource(R.drawable.villain_doc_ock);
            mEnemyObject.requestLayout();
            mEnemyObject.getLayoutParams().height = 150;
            mEnemyObject.getLayoutParams().width = 100;
            mEnemyObject.setImageResource(R.drawable.doc_ock_claw_2);
            spiderManFightTheme.release();
            spiderManFightThemeDocOck.setLooping(true);
            spiderManFightThemeDocOck.start();
        }

        //Check score for level to switch to Mysterio | (Level 3)
        if(score == 250){
            enemyObjectX = (screenWidth + 1400);
            level += 1;
            score += 10;
            //Increase speed of bombs
            enemyObjectSpeed = 28;
            mEnemyObject.requestLayout();
            mEnemyObject.getLayoutParams().height = 70;
            mEnemyObject.getLayoutParams().width = 70;
            mVillainImage.setImageResource(R.drawable.villain_mysterio);
            mEnemyObject.setImageResource(R.drawable.mysterio_blast_image);
            spiderManFightThemeDocOck.release();
            spiderManFightThemeMysterio.setLooping(true);
            spiderManFightThemeMysterio.start();
        }

        //Check score for level to switch to Scorpian (Two Orange Balls) | (Level 4)
        if(score == 400){
            enemyObjectX = (screenWidth + 1400);
            enemyObject2X = (screenWidth + 1400);
            level += 1;
            score += 10;
            //Increase speed of first enemy bomb
            enemyObjectSpeed = 32;
            //Make second bomb visible
            mEnemyObject2.setVisibility(View.VISIBLE);
            enemyObjectSpeed2 = 32;
            mVillainImage.setImageResource(R.drawable.villain_scorpion);
            mEnemyObject.setImageResource(R.drawable.scorpian_blast_image);
            mEnemyObject2.setImageResource(R.drawable.scorpian_blast_image);
            spiderManFightThemeMysterio.release();
            spiderManFightThemeScorpian.setLooping(true);
            spiderManFightThemeScorpian.start();

        }

        //After level 4, should continue to check for second enemy object
        if(score >= 400) hitCheck2();

        //Check score for level to switch to Venom | (Level 5)
        if(score == 500){
            enemyObjectX = (screenWidth + 2000);
            enemyObject2X = (screenWidth + 2000);
            level += 1;
            score += 10;
            //Increase speed of bombs
            enemyObjectSpeed = 35;
            enemyObjectSpeed2 = 35;
            mVillainImage.setImageResource(R.drawable.villain_venom);
            mEnemyObject.setImageResource(R.drawable.venom_blast_image);
            mEnemyObject2.setImageResource(R.drawable.venom_blast_image);
            spiderManFightThemeScorpian.release();
            spiderManFightThemeVenom.setLooping(true);
            spiderManFightThemeVenom.start();
        }

        //Check score for level to switch to Carnage | (Level 6)
        if(score == 550){
            enemyObjectX = (screenWidth + 2200);
            enemyObject2X = (screenWidth + 2200);
            level += 1;
            score += 10;
            //Increase speed of bombs
            enemyObjectSpeed = 38;
            enemyObjectSpeed2 = 38;
            mVillainImage.setImageResource(R.drawable.villain_carnage);
            mEnemyObject.setImageResource(R.drawable.carnage_blast_image);
            mEnemyObject2.setImageResource(R.drawable.carnage_blast_image);
            spiderManFightThemeVenom.release();
            spiderManFightThemeCarnage.setLooping(true);
            spiderManFightThemeCarnage.start();
        }

        //Check score for level to switch to Thanos | (Level 7)
        if(score == 600){
            enemyObjectX = (screenWidth + 2500);
            enemyObject2X = (screenWidth + 2500);
            enemyObject3X = (screenWidth + 2500);
            level += 1;
            score += 10;
            //Increase speed of bombs
            enemyObjectSpeed = 40;
            enemyObjectSpeed2 = 40;
            mEnemyObject3.setVisibility(View.VISIBLE);
            enemyObjectSpeed3 = 40;
            mVillainImage.setImageResource(R.drawable.villain_thanos);
            mEnemyObject.setImageResource(R.drawable.thanos_gem_purple);
            mEnemyObject2.setImageResource(R.drawable.thanos_gem_green);
            mEnemyObject3.setImageResource(R.drawable.thanos_gem_red);
            spiderManFightThemeCarnage.release();

            spiderManFightThemeThanos.setLooping(true);
            spiderManFightThemeThanos.start();
        }

        //At Thanos level, should continue to check for third pumpkin
        if(score >= 600) hitCheck3();

        //---Enemy Object 1
        enemyObjectX -= enemyObjectSpeed;
        if(enemyObjectX < -50){
            enemyObjectX = screenWidth + 20;
            enemyObjectY = (int) Math.floor(Math.random() * (frameHeight - mEnemyObject.getHeight()));
        }
        mEnemyObject.setX(enemyObjectX);
        mEnemyObject.setY(enemyObjectY);

        //---Enemy Object 2 (Start off Invisible), At Scorpian level, make this visible
        enemyObject2X -= enemyObjectSpeed2;
        if(enemyObject2X < -50){
            enemyObject2X = screenWidth + 20;
            enemyObject2Y = (int) Math.floor(Math.random() * (frameHeight - mEnemyObject2.getHeight()));
        }
        mEnemyObject2.setX(enemyObject2X);
        mEnemyObject2.setY(enemyObject2Y);

        //---Enemy Object 3 (Start off Invisible), At Thanos level, make this visible
        enemyObject3X -= enemyObjectSpeed3;
        if(enemyObject3X < -50){
            enemyObject3X = screenWidth + 20;
            enemyObject3Y = (int) Math.floor(Math.random() * (frameHeight - mEnemyObject3.getHeight()));
        }
        mEnemyObject3.setX(enemyObject3X);
        mEnemyObject3.setY(enemyObject3Y);

        //---Good Blue ball
        goodBombX -= goodBombSpeed;
        if(goodBombX < -50){
            goodBombX = screenWidth + 20;
            goodBombY = (int) Math.floor(Math.random() * (frameHeight - mGoodBomb.getHeight()));
        }
        mGoodBomb.setX(goodBombX);
        mGoodBomb.setY(goodBombY);

        //Move Spiderman
        if(action_flg){
            //Touching
            mSpiderMan.setImageResource(R.drawable.spiderman_swing_up);
            spiderManY -= 30;
            webSound.start();
        }else{
            //Release
            mSpiderMan.setImageResource(R.drawable.spiderman_dive_down);
            spiderManY += 20;
        }

        //Check SpiderMan Position Constantly
        //TOO LOW
        if(spiderManY < 0){
            spiderManY = 0;
            mSpiderMan.setImageResource(R.drawable.spiderman_hang);
        }
        //TOO LOW
        if(spiderManY > frameHeight - spidermanHeight) {
            spiderManY = frameHeight - spidermanHeight;
            mSpiderMan.setImageResource(R.drawable.spiderman_crouched);
        }
        mSpiderMan.setY(spiderManY);

        //Set Texts in Above Tab
        mScoreLabel.setText("" + score);
        mLevelLabel.setText("Level " + level);

        //Life Images
        //No need to check for 3
        if(lives == 2){
            mLife3.setVisibility(View.INVISIBLE);
        }else if(lives == 1){
            mLife3.setVisibility(View.INVISIBLE);
            mLife2.setVisibility(View.INVISIBLE);
        }else if(lives == 0){
            mLife3.setVisibility(View.INVISIBLE);
            mLife2.setVisibility(View.INVISIBLE);
            mLife1.setVisibility(View.INVISIBLE);
        }

    }

    //Constantly checking for hits
    public void hitCheck(){
        //First thing to check for: Lives
        if(lives == 0){
            /*
            The reason all hit checks have this try and catch is because, sometimes one
            hitcheck will register before the other and if they cancel the timer first and
            then the other hitcheck checks for that Timer, it would have already been
            deleted, thus you must use a try and catch
             */
            try {
                mTimer.cancel();
                mTimer = null;
            }catch (Exception e){
                Log.d("TIMER_ERROR", e.getMessage());
            }

            //GAME OVER
            gameOver();
            //Should have an exploding animation quickly occur
        }

        //Pumpkin bomb
        int enemyObjectCenterX = enemyObjectX + mEnemyObject.getWidth()/2;
        int enemyObjectCenterY = enemyObjectY + mEnemyObject.getHeight()/2;

        if(0 <= enemyObjectCenterX && enemyObjectCenterX <= spidermanHeight &&
                spiderManY <= enemyObjectCenterY && enemyObjectCenterY <= spiderManY + spidermanHeight){
            enemyObjectX = -50; //Goes off the screen;
            lives --;
        }

        //Good Bomb
        int goodBombCenterX = goodBombX + mGoodBomb.getWidth()/2;
        int goodBombCenterY = goodBombY + mGoodBomb.getHeight()/2;

        if(0 <= goodBombCenterX && goodBombCenterX <= spidermanHeight &&
                spiderManY <= goodBombCenterY && goodBombCenterY <= spiderManY + spidermanHeight){
            score += 10;
            goodBombX = -50; //Goes off the screen;
        }

    }

    //Constantly checking for hits for second pumpkin bomb
    public void hitCheck2(){

        //First thing to check for: Lives
        if(lives == 0){
            try {
                mTimer.cancel();
                mTimer = null;
            }catch (Exception e){
                Log.d("TIMER_ERROR", e.getMessage());
            }
            //GAME OVER
            gameOver();
            //Should have an exploding animation quickly occur
        }

        //Pumpkin bomb 2
        int enemyObjectCenter2X = enemyObject2X + mEnemyObject2.getWidth()/2;
        int enemyObjectCenter2Y = enemyObject2Y + mEnemyObject2.getHeight()/2;

        if(0 <= enemyObjectCenter2X && enemyObjectCenter2X <= spidermanHeight &&
                spiderManY <= enemyObjectCenter2Y && enemyObjectCenter2Y <= spiderManY + spidermanHeight){
            enemyObject2X = -50; //Goes off the screen;
            lives --;
        }

    }

    //Constantly checking for hits for second pumpkin bomb
    public void hitCheck3(){

        //First thing to check for: Lives
        if(lives == 0){
            try {
                mTimer.cancel();
                mTimer = null;
            }catch (Exception e){
                Log.d("TIMER_ERROR", e.getMessage());
            }
            //GAME OVER
            gameOver();
            //Should have an exploding animation quickly occur
        }

        //Pumpkin bomb 3
        int enemyObjectCenter3X = enemyObject3X + mEnemyObject3.getWidth()/2;
        int enemyObjectCenter3Y = enemyObject3Y + mEnemyObject3.getHeight()/2;

        if(0 <= enemyObjectCenter3X && enemyObjectCenter3X <= spidermanHeight &&
                spiderManY <= enemyObjectCenter3Y && enemyObjectCenter3Y <= spiderManY + spidermanHeight){
            enemyObject3X = -50; //Goes off the screen;
            lives --;
        }

    }

    public void gameOver(){
        //Go to Game Over Screen
        Intent gameOverIntent = new Intent(getApplicationContext(), GameOverActivity.class);
        gameOverIntent.putExtra("SCORE", score);
        startActivity(gameOverIntent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!start_flg){

            start_flg = true;

            //Why get frame height and spiderman height here?
            //Because the UI has not been set on the screen in onCreate

            FrameLayout gameFrame = (FrameLayout)findViewById(R.id.game_frame);
            frameHeight = gameFrame.getHeight();

            spiderManY = (int) mSpiderMan.getY();

            spidermanHeight = mSpiderMan.getHeight();

            mStartLabel.setVisibility(View.GONE);

            mHowToPlayBtn.setVisibility(View.GONE);

            try {
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                changePos();
                            }
                        });
                    }
                }, 0, 20);
            }catch (Exception e) {
                Toast.makeText(GameView.this, e.toString(), Toast.LENGTH_SHORT).show();
            }

        }else{
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                action_flg = true;
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                action_flg = false;
            }
        }

        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        spiderManFightTheme.release();
        spiderManFightThemeDocOck.release();
        spiderManFightThemeMysterio.release();
        spiderManFightThemeScorpian.release();
        spiderManFightThemeThanos.release();
    }

}

