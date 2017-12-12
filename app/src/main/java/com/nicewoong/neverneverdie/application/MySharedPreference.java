package com.nicewoong.neverneverdie.application;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nicewoong on 2017. 12. 12..
 * 스위치 on/off 상태 등의 설정을 저장할 shared preference 를 관리하는 클래스
 * Singleton pattern 으로 관리
 */

public class MySharedPreference {

    private static MySharedPreference mInstance;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private static String SHARED_PREFERENCE_FILE_NAME = "setting";
    private static String ALWAYS_SWITCH= "always_switch";



    public static MySharedPreference getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new MySharedPreference(context);
        }
        return mInstance;

    }

    public MySharedPreference(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public void setAlwaysSafeSwitch(boolean bool) {
        mEditor.putBoolean(ALWAYS_SWITCH, bool);
        mEditor.commit();
    }

    public boolean getAlwaysSafeSwitch() {
        return mSharedPreferences.getBoolean(ALWAYS_SWITCH, false); // default false
    }


}
