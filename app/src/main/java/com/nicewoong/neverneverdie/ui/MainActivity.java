package com.nicewoong.neverneverdie.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nicewoong.neverneverdie.R;
import com.nicewoong.neverneverdie.trafficAccidentDeath.AccidentDataCreator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    /*
    Pulbic data API KEY (for "trafficAccidentDeath")
    PYWezzCNSGTHF5aJALsLoxzCziUi5d7B1jEPrU87TacdNZQoVimSQG%2FFBafP1RRBRZa1XVGX5babWe7wwSXr%2FQ%3D%3D
     */

    public static String TRAFFIC_ACCIDENT_DEATH_API_KEY ="PYWezzCNSGTHF5aJALsLoxzCziUi5d7B1jEPrU87TacdNZQoVimSQG%2FFBafP1RRBRZa1XVGX5babWe7wwSXr%2FQ%3D%3D";
//    public static String

    public static String TAG_PROCEDURE_DEBUG  = "TAG_PROCEDURE_DEBUG";
    public static String TAG_REST_API_TEST  = "TAG_REST_API_TEST";

    /*
    button
     */
    public Button checkAroundMeButton;
    public Button alwaysSafeCheckingButton;
    private boolean alwaysSafeCheckingButtonFlag = false; // Button Toggle flag


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG_PROCEDURE_DEBUG,"Start of onCreate()");

//        TrafficAccidentDeathDataCom trafficAccidentDeathDataCom = new TrafficAccidentDeathDataCom(getApplicationContext());
//        trafficAccidentDeathDataCom.setTrafficAccidentDeathList(TRAFFIC_ACCIDENT_DEATH_API_KEY,"2014","2200","2204");
        // Button Register
        registerButtons();

    }

    /**
     *  find Button View by ID
     *  And Register Button onClickListener
     */
    public void registerButtons() {
        checkAroundMeButton = (Button)findViewById(R.id.button_check_around_me);
        checkAroundMeButton.setOnClickListener(this); // Register onClickListener
        alwaysSafeCheckingButton = (Button)findViewById(R.id.button_always_safe_checking);
        alwaysSafeCheckingButton.setOnClickListener(this); // Register onClickListener
    }


    /**
     * Called when the user clicks the <Check around me> button
     *
     */
    public void openCheckAroundMeMapActivity() {
        Intent intent = new Intent(this, CheckAroundMeMapActivity.class);
        startActivity(intent);
    }

    /**
     * Click Listener
     * Called when user click the buttons and other items..
     * @param v
     */
    @Override
    public void onClick(View v) {
        Log.d(MainActivity.TAG_PROCEDURE_DEBUG,"onClick() 호출되었습니다 ");
        int id = v.getId();

        switch (id) {
            case R.id.button_check_around_me:
                Log.d(MainActivity.TAG_PROCEDURE_DEBUG,"Check around me 버튼을 클릭했습니다");
                openCheckAroundMeMapActivity();
                break;

            case R.id.button_always_safe_checking:
                if(alwaysSafeCheckingButtonFlag==true) {
                    alwaysSafeCheckingButton.setText("Always-Safe OFF");
                    alwaysSafeCheckingButton.setBackgroundColor(getResources().getColor(R.color.colorButtonAlwaysSafeOFF));

                    alwaysSafeCheckingButtonFlag = false;
                }else {
                    alwaysSafeCheckingButton.setText("Always-Safe ON");
                    alwaysSafeCheckingButton.setBackgroundColor(getResources().getColor(R.color.colorButtonAlwaysSafeOn));
                    //dialog로 확인하기
                    NeverDieDialog confirmDialog = new NeverDieDialog(this);
                    confirmDialog.showAlwaysSafeOnDialog();
                    alwaysSafeCheckingButtonFlag = true;
                }
                break;

            default:

        }
    }
}
