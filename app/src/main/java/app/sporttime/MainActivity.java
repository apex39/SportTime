package app.sporttime;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private Button buttonChronometer;
    private Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonChronometer = (Button) findViewById(R.id.buttonChronometer);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        buttonChronometer.setOnClickListener(this);
    }

    boolean isChronometerRunning = false;

    public void startChronometer(View v) {

    }

    @Override
    public void onClick(View v) {
        if (v == buttonChronometer) {
            if (isChronometerRunning) {
                isChronometerRunning = false;
                chronometer.stop();
                buttonChronometer.setText("START");
            } else {
                isChronometerRunning = true;
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                buttonChronometer.setText("STOP");
            }
        }
    }
}
