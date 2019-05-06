package com.example.trueorfalse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class GameOverActivity extends AppCompatActivity {
    TextView tvScore,tvHighScore;
    ImageButton ibPlayContinue, ibRestart;
    private int score = 0;
    private int highScore = 0;
    private boolean backPress = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        //
        tvScore = findViewById(R.id.score);
        tvHighScore = findViewById(R.id.highScore);
        ibPlayContinue = findViewById(R.id.playContinue);
        ibRestart = findViewById(R.id.restart);

        //lấy intent của activity này
        Intent intent = getIntent();
        //lấy thùng chứa bundle với tên giao dịch la "diem"
        Bundle bundle = intent.getBundleExtra("diem");
        //lấy giá trị kiểu nguyên với tên giao dịch là "score"
        score = bundle.getInt("score");
        tvScore.setText(String.valueOf(score));

        //lưu điểm cao nhất
        SharedPreferences sharedPreferences = getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        highScore = sharedPreferences.getInt("highScore",0);
        if(score > highScore){
            editor.putInt("highScore",score);
            editor.apply();
            tvHighScore.setText(String.valueOf(score));
        }
        else {
            tvHighScore.setText(String.valueOf(highScore));
        }

        //
        ibRestart.setOnClickListener(v -> goMainActivity());

        ibPlayContinue.setOnClickListener(v -> goPlayActivity());
    }
    public void goMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void goPlayActivity(){
        Intent i = new Intent(this, PlayActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (!backPress) {
            backPress = true;
            Toast.makeText(this, getString(R.string.press_back_again_to_exit), Toast.LENGTH_SHORT).show();
            Handler backHandler = new Handler();
            backHandler.postDelayed(() -> backPress = false, 1000);
            return;
        }
        finishAffinity();
    }
}
