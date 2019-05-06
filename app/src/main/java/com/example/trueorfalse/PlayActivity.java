package com.example.trueorfalse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class PlayActivity extends AppCompatActivity {
    private TextView tvMathematic, tvResult;
    private ImageButton btnTrue, btnFalse, btnSound;
    private ProgressBar progressBar;
    private CountDownTimer countDownTimer;

    private TextView tvScore;

    private int score = 0;
    private boolean answer;

    private MediaPlayer mediaPlayer;
    private boolean isSoundOn = true;
    private boolean backPress = false;
    Runnable backRunnable = () -> backPress = false;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        fetchSettings();
        initViews();
        initMediaPlayer();
        getRandom();

        btnTrue.setOnClickListener(v -> {
            selectAnswer(true);
        });
        btnFalse.setOnClickListener(v->{
            selectAnswer(false);
        });
    }

    private void fetchSettings() {
        sharedPreferences = getSharedPreferences("true_false", MODE_PRIVATE);
        isSoundOn = sharedPreferences.getBoolean("sound_on", true);
    }

    private void initMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);
        mediaPlayer.setLooping(true);
        if (isSoundOn) {
            mediaPlayer.start();
        }
    }

    private void initViews() {
        tvMathematic = findViewById(R.id.mathematics);
        tvResult = findViewById(R.id.result);
        btnTrue = findViewById(R.id.trueButton);
        btnFalse = findViewById(R.id.falseButton);
        progressBar = findViewById(R.id.progressBar);
        tvScore = findViewById(R.id.tvScore);
        btnSound = findViewById(R.id.btnSound);
        btnSound.setOnClickListener(v -> {
            isSoundOn = !isSoundOn;
            if (isSoundOn) {
                mediaPlayer.start();
                btnSound.setImageResource(R.drawable.ic_sound_on);
            } else {
                mediaPlayer.pause();
                btnSound.setImageResource(R.drawable.ic_sound_off);
            }
            sharedPreferences.edit().putBoolean("sound_on", isSoundOn).apply();
        });
        btnSound.setImageResource(isSoundOn ? R.drawable.ic_sound_on : R.drawable.ic_sound_off);
    }

    private void startCountDown() {
        long interval = calculateInterval();
        progressBar.setMax((int) interval);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(interval, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int) millisUntilFinished);
            }

            @Override
            public void onFinish() {
//                showGameOver();
                goGameOverActivity();
            }
        };
        countDownTimer.start();
    }

    private long calculateInterval() {
        if (score < 5) {
            return 10 * 1000;
        } else if (score < 10) {
            return 7 * 1000;
        } else if (score < 15) {
            return 5 * 1000;
        } else {
            return 3 * 1000;
        }
    }

    private void showGameOver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.try_again);
        builder.setTitle(R.string.game_over);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", (dialog, which) -> {
            dialog.cancel();
            score = 0;
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void selectAnswer(boolean ans) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if ((ans && answer) || (!ans && !answer)) {
            //chon dung
            score += 1; //tang diem len 1
            tvScore.setText(String.valueOf(score));
            makeNewGame();
        } else {
            //chon sai
            //showGameOver();
            goGameOverActivity();
        }
    }

    private void makeNewGame() {
        getRandom();
        startCountDown();
    }

    private void getRandom() {
        Random rd = new Random();
        int a = random();
        int b = random();
        int trueResult = a + b;
        int falseResult = random();
        if (falseResult == trueResult) falseResult += 1;
        tvMathematic.setText(String.format("%s + %s", a, b));
        answer = rd.nextBoolean();

        //hien thi kq dung
        if (answer) {
            tvResult.setText(getString(R.string.result, trueResult));
        } else {
            //hien thi kq sai
            tvResult.setText(getString(R.string.result, falseResult));
        }
    }

    private int randomTrueFalse() {
        int number2 = 0;
        Random r = new Random();
        number2 = r.nextInt(2);
        return number2;
    }

    private int random() {
        int number1 = 0;
        Random r = new Random();
        number1 = r.nextInt(20) + 1;
        return number1;
    }

    private void goGameOverActivity() {
        Intent intent = new Intent(this, GameOverActivity.class);
        //dua du lieu vao thung chua bundle
        Bundle bundle = new Bundle();
        bundle.putInt("score", score);//score la ten giao dich
        //dua thung chua bundle cho ng dua tin intent
        intent.putExtra("diem", bundle);//diem la ten cua thung
        startActivity(intent);
        mediaPlayer.stop();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (!backPress) {
            backPress = true;
            Toast.makeText(this, getString(R.string.press_back_again_to_exit), Toast.LENGTH_SHORT).show();
            Handler backHandler = new Handler();
            backHandler.postDelayed(backRunnable, 1000);
            return;
        }
        mediaPlayer.stop();
        finishAffinity();
    }
}
