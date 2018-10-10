package com.aguilartristen.jumpingspider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class PauseMenu extends Activity {

    //Buttons
    private Button mResumeBtn;
    private Button mMenuBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pause_menu);

        //Buttons
        mResumeBtn = (Button) findViewById(R.id.pause_menu_resume_btn);
        mMenuBtn = (Button) findViewById(R.id.pause_menu_main_menu_btn);

        String PAUSE_STATE = getIntent().getStringExtra("PAUSE_STATE");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .7), (int)(height * .7));

        mResumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PauseMenu.this, MainActivity.class));
            }
        });

    }
}
