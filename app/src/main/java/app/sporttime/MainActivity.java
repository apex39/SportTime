package app.sporttime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
    @Override
    public void onClick(View v) {
        if(v == buttonChronometer){
            if (isChronometerRunning) {
                chronometer.stop();
            } else {
                chronometer.start();
            }
        }
    }
}
