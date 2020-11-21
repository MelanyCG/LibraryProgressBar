package com.example.libraryprogressbar;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.example.progressbarlibrary.MyProgressBar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private MyProgressBar myProgressBar;

    private Handler mTimedHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int[] progress = myProgressBar.getText();
            progress[0] = progress[0] + 1;
            myProgressBar.setText(progress[0], progress[1]);
            if(progress[0] == progress[1]) {
                myProgressBar.setBottomText("Done!");
            }
            mTimedHandler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myProgressBar = findViewById(R.id.myProgressBar);

        // Random the colors of the arc progress bar and the text of the progress bar.
        Random rnd = new Random();
        int finishColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        int unFinishColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        int textColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        myProgressBar.setText(1,10);
        myProgressBar.setStrokeWidth(30);
        myProgressBar.setTextSize(100);
        myProgressBar.setTextColor(textColor);
        myProgressBar.setUnfinishedStrokeColor(unFinishColor);
        myProgressBar.setFinishedStrokeColor(finishColor);
        myProgressBar.setBottomText("Example");
        myProgressBar.setBottomTextSize(50);

        mTimedHandler.sendEmptyMessageDelayed(0,5);
    }
}