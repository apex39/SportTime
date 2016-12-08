package app.sporttime;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import app.sporttime.model.Record;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_VALUE = "value";
    private static final String KEY_SPORT = "sport_id";
    private static final String ADD_RECORD_URL = "http://102657.panda5.pl/raw/add_record.php";
    private static final String GET_RECORDS_URL = "http://102657.panda5.pl/raw/get_records.php";
    private Button buttonChronometer;
    private Chronometer chronometer;
    private String login;
    private RadioGroup sportsRadioGroup;
    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sportsRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        chart = (LineChart) findViewById(R.id.chart);
        buttonChronometer = (Button) findViewById(R.id.buttonChronometer);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        buttonChronometer.setOnClickListener(this);

        Intent intent = getIntent();
        login = intent.getStringExtra("USER_ID");
    }

    boolean isChronometerRunning = false;
    @Override
    public void onClick(View v) {
        if (v == buttonChronometer) {
            if (isChronometerRunning) {
                isChronometerRunning = false;
                chronometer.stop();
                buttonChronometer.setText("START");

                long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedMillis);

                int sportId = 0;
                switch (sportsRadioGroup.getCheckedRadioButtonId()){
                    case (R.id.radioButton1) :
                        sportId = 1;
                        break;
                    case (R.id.radioButton2) :
                        sportId = 2;
                        break;
                    case (R.id.radioButton3) :
                        sportId = 3;
                        break;
                    case (R.id.radioButton4) :
                        sportId = 4;
                        break;
                }

                sendRecord(login, seconds, sportId);
                getUserRecords(login);
            } else {
                isChronometerRunning = true;
                chronometer.setBase(SystemClock.elapsedRealtime()); //Liczymy od czasu startu systemu
                chronometer.start();
                buttonChronometer.setText("STOP");
            }
        }
    }

    private void sendRecord(final String userId, final long seconds, final int sportId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_RECORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("1")) {
                            Toast.makeText(MainActivity.this,"Record Added!",Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MainActivity.this,"Adding record failed" ,Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put(KEY_USER_ID,userId);
                params.put(KEY_VALUE,String.valueOf(seconds));
                params.put(KEY_SPORT,String.valueOf(sportId));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private Record[] records;
    private void getUserRecords(final String userId){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_RECORDS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("Null")) {
                            Gson gson = new Gson() ;
                            records = gson.fromJson(response,Record[].class);
                            addRecordsToChart(records);
                        } else {
                            Toast.makeText(MainActivity.this,"Cannot reach records" ,Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put(KEY_USER_ID,userId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addRecordsToChart(Record[] records) {
        List<Entry> entries1 = new ArrayList<Entry>();
        List<Entry> entries2 = new ArrayList<Entry>();
        List<Entry> entries3 = new ArrayList<Entry>();
        List<Entry> entries4 = new ArrayList<Entry>();



        int reference_timestamp = records[0].getIntDatetime();

        for (Record record : records) {
            switch (record.getSports_sport_id()) {
                case 1:
                    entries1.add(new Entry(record.getFloatDatetime() - reference_timestamp, record.getValue()));
                    break;
                case 2:
                    entries2.add(new Entry(record.getFloatDatetime() - reference_timestamp, record.getValue()));
                    break;
                case 3:
                    entries3.add(new Entry(record.getFloatDatetime() - reference_timestamp, record.getValue()));
                    break;
                case 4:
                    entries4.add(new Entry(record.getFloatDatetime() - reference_timestamp, record.getValue()));
                    break;
            }
        }
        Map<String, List<Entry>> entries = new HashMap<>(4);
        entries.put("Pływanie", entries1);
        entries.put("Bieganie", entries2);
        entries.put("Rower", entries3);
        entries.put("Siłownia", entries4);


        List<ILineDataSet> lineDataSets = new ArrayList<ILineDataSet>();
        for(Iterator<Map.Entry<String, List<Entry>>> it = entries.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, List<Entry>> entry = it.next();
            if(entry.getValue().isEmpty()) {
                it.remove();
            } else {
                lineDataSets.add(new LineDataSet(entry.getValue(), entry.getKey()));
            }
        }

            LineData lineData = new LineData(lineDataSets);
            chart.setData(lineData);
            chart.invalidate();
    }
}
