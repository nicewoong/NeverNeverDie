package com.nicewoong.neverneverdie;

import android.app.Application;
import android.content.res.Configuration;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by nicewoong on 2017. 4. 23..
 */

public class MyApplication extends Application {


    public static GoogleApiClient mGoogleApiClient;

    /** onCreate()
     * 액티비티, 리시버, 서비스가 생성되기전 어플리케이션이 시작 중일때
     * Application onCreate() 메서드가 만들어 진다고 나와 있습니다.
     * by. Developer 사이트
     */
    @Override
    public void onCreate() {
        super.onCreate();
        // Create an instance of GoogleAPIClient.
    }

    /**
     * onConfigurationChanged()
     * 컴포넌트가 실행되는 동안 단말의 화면이 바뀌면 시스템이 실행 한다.
    */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}
