package com.team_bona.furymonkey.tensecondsplanktimer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by furymonkey on 2017-03-10.
 */

public class LogoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.team_bona.furymonkey.tensecondsplanktimer.R.layout.activity_logo);

        Handler hd = new Handler();
        hd.postDelayed(new LogoHandler() , 200);
    }

    private class LogoHandler implements Runnable{
        public void run() {
            startActivity(new Intent(getApplication(), MainActivity.class)); // 로딩이 끝난후 이동할 Activity
            LogoActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }
    }
}
