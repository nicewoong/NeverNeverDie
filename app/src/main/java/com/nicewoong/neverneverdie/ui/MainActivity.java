package com.nicewoong.neverneverdie.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.nicewoong.neverneverdie.R;
import com.nicewoong.neverneverdie.trafficAccidentDeath.TrafficAccidentDeathDataCom;

public class MainActivity extends AppCompatActivity {

    /*
    Pulbic data API KEY (for "trafficAccidentDeath")
    PYWezzCNSGTHF5aJALsLoxzCziUi5d7B1jEPrU87TacdNZQoVimSQG%2FFBafP1RRBRZa1XVGX5babWe7wwSXr%2FQ%3D%3D
     */

    public static String TRAFFIC_ACCIDENT_DEATH_API_KEY ="PYWezzCNSGTHF5aJALsLoxzCziUi5d7B1jEPrU87TacdNZQoVimSQG%2FFBafP1RRBRZa1XVGX5babWe7wwSXr%2FQ%3D%3D";
//    public static String

    public static String TAG_PROCEDURE_DEBUG  = "TAG_PROCEDURE_DEBUG";
    public static String TAG_REST_API_TEST  = "TAG_REST_API_TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG_PROCEDURE_DEBUG,"Start of onCreate()");

        TrafficAccidentDeathDataCom trafficAccidentDeathDataCom = new TrafficAccidentDeathDataCom(getApplicationContext());
        trafficAccidentDeathDataCom.setTrafficAccidentDeathList(TRAFFIC_ACCIDENT_DEATH_API_KEY,"2014","2200","2204");



    }
}
