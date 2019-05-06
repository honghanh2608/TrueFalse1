package com.example.trueorfalse;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private ImageButton ibPlay;
    private ImageButton btnRate;
    private ImageButton btnMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        ibPlay = findViewById(R.id.playButton);
        btnRate = findViewById(R.id.rateButton);
        btnMore = findViewById(R.id.moreButton);

        //
        ibPlay.setOnClickListener(v -> doOpenPlayActivity());

        //
        btnRate.setOnClickListener(v -> rate_alertDialong());

        //
        btnMore.setOnClickListener(v -> more_alertDialong());
    }
    public void doOpenPlayActivity(){
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
        finish();
    }
    public void rate_alertDialong(){
        AlertDialog.Builder dialong1 = new AlertDialog.Builder(this);
        dialong1.setTitle("THANK YOU!");
        dialong1.setMessage("Vui lòng đánh giá ứng dụng của chúng tôi ngay sau khi nó xuất hiện trên cửa hàng");
        dialong1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialong1.create().show();
    }
    public void more_alertDialong(){
        AlertDialog.Builder dialong2 = new AlertDialog.Builder(this);
        dialong2.setTitle("MORE GAME");
        dialong2.setMessage("Bạn có thể tìm thấy những game khác ở bản cập nhật tiếp theo.Cảm ơn!");
        dialong2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialong2.create().show();
    }
}
