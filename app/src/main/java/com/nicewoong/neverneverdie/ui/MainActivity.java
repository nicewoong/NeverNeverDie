package com.nicewoong.neverneverdie.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nicewoong.neverneverdie.R;
import com.nicewoong.neverneverdie.application.MySharedPreference;
import com.nicewoong.neverneverdie.backgroundService.AlwaysSafeService;
import com.nicewoong.neverneverdie.trafficAccidentDeath.AccidentDeathAPIRequestTask;
import com.nicewoong.neverneverdie.trafficAccidentDeath.AccidentDeathData;
import com.nicewoong.neverneverdie.ui.uiMap.CheckAroundMeMapActivity;
import com.nicewoong.neverneverdie.ui.util.NeverDieDialog;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    /*
    Tags for Log
     */
    public static String TAG_PROCEDURE_DEBUG  = "TAG_PROCEDURE_DEBUG";
    public static String TAG_REST_API_TEST  = "TAG_REST_API_TEST";

    /*
    UI button
     */
    public Button checkAroundMeButton;
    public Button alwaysSafeCheckingButton;
    private boolean alwaysSafeCheckingButtonFlag = false; // Button Toggle flag

    /*
    Static variable that can be accessed anywhere in the application
     */
    public static AccidentDeathData accidentDeathData; // HTTP request 를 통해 얻은 교통사망정보 전체 데이터 셋 (AccidentDeathAPIRequestTask 의 수행결과로 데이터 저장)

    /*
    shared preferences
     */
    public MySharedPreference sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG_PROCEDURE_DEBUG,"Start of onCreate()");

        sharedPreferences = MySharedPreference.getInstance(getApplicationContext());
        registerButtonsUI();// Button Register

        // Publish updating AccidentDeathData through HTTP request
        try {
            publishAsyncRequestForAccidentDeathData();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }



    /**
     *  find Button View by ID
     *  And Register Button onClickListener
     */
    public void registerButtonsUI() {
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

    public void publishAsyncRequestForAccidentDeathData() throws UnsupportedEncodingException, MalformedURLException {
        AccidentDeathAPIRequestTask requestTask = new AccidentDeathAPIRequestTask(MainActivity.this);
        requestTask.execute();

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

                if(sharedPreferences.getAlwaysSafeSwitch()) { // 백그라운드 서비스 스위치 ON->OFF
                    alwaysSafeCheckingButton.setText("Always-Safe OFF");
                    alwaysSafeCheckingButton.setBackgroundColor(getResources().getColor(R.color.colorButtonAlwaysSafeOFF));

                    Intent intent = new Intent(getApplicationContext(), AlwaysSafeService.class);
                    stopService(intent); // 서비스 종료

                    sharedPreferences.setAlwaysSafeSwitch(false); // 스위치 설정을 off 로 저장

                }else { // 백그라운드 서비스 스위치 On -> OFF
                    alwaysSafeCheckingButton.setText("Always-Safe ON");
                    alwaysSafeCheckingButton.setBackgroundColor(getResources().getColor(R.color.colorButtonAlwaysSafeOn));
                    //dialog로 확인하기
                    NeverDieDialog confirmDialog = new NeverDieDialog(this);
                    confirmDialog.showAlwaysSafeOnDialog();

                    Intent intent = new Intent(getApplicationContext(), AlwaysSafeService.class);
                    startService(intent); // 서비스 시작

                    sharedPreferences.setAlwaysSafeSwitch(false); // 스위치 설정을 on 으로 저장
                }
                break;

            default:

        }// end of switch


    }// end of onClick()


    public


}// end of class

