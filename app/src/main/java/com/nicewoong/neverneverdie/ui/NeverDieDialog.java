package com.nicewoong.neverneverdie.ui;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.nicewoong.neverneverdie.R;

/**
 * Created by nicewoong on 2017. 4. 23..
 */

public class NeverDieDialog {

    public Context mContext;

    /**
     * Constructor
     */
    public NeverDieDialog(Context context) {
        mContext = context;
    }

    /**
     * 위험지역 주변 500m 근처에 진입하면 Alert 다이얼로그를 띄어줍니다.
     */
    public void showDangerousAlertDialog() {
        // 다이얼로그 바디
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(mContext);
        // 메세지
        alert_confirm.setMessage(mContext.getResources().getString(R.string.dangerous_spot_alert_dialog_message));
        // 확인 버튼 리스너
        alert_confirm.setPositiveButton(mContext.getResources().getString(R.string.dangerous_spot_alert_dialog_confirm_button), null);
        // 다이얼로그 생성
        AlertDialog alert = alert_confirm.create();

        // 아이콘
        alert.setIcon(R.drawable.skull_icon);
        // 다이얼로그 타이틀
        alert.setTitle(mContext.getResources().getString(R.string.dangerous_spot_alert_dialog_title));
        // 다이얼로그 보기
        alert.show();
    }


    /**
     * 위험지역 주변 500m 근처에 진입하면 Alert 다이얼로그를 띄어줍니다.
     */
    public void showAlwaysSafeOnDialog() {
        // 다이얼로그 바디
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(mContext);
        // 메세지
        alert_confirm.setMessage(mContext.getResources().getString(R.string.dangerous_spot_alert_dialog_message));
        // 확인 버튼 리스너
        alert_confirm.setPositiveButton(mContext.getResources().getString(R.string.dangerous_spot_alert_dialog_confirm_button), null);
        // 다이얼로그 생성
        AlertDialog alert = alert_confirm.create();

        // 아이콘
        alert.setIcon(R.drawable.skull_icon);
        // 다이얼로그 타이틀
        alert.setTitle(mContext.getResources().getString(R.string.dangerous_spot_alert_dialog_title));
        // 다이얼로그 보기
        alert.show();
    }

}
