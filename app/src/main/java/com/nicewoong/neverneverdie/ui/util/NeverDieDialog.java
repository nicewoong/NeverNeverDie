package com.nicewoong.neverneverdie.ui.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
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
     * todo notification 으로 구현할 것을 고려해보자 (앱을 이용하고 있지 않을 때 알림을 주기 위해서 )
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
     * always safe - background service를 turn on 했을 때
     * 확인하는 다이얼로그그     */
    public void showAlwaysSafeOnDialog() {
//        // 다이얼로그 바디
//        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(mContext);
//        // 메세지
//        alert_confirm.setMessage(mContext.getResources().getString(R.string.always_safe_on_dialog_message));
//        // 확인 버튼 리스너
//        alert_confirm.setPositiveButton(mContext.getResources().getString(R.string.always_safe_on_dialog_confirm_button), null);
//        // 다이얼로그 생성
//        AlertDialog alert = alert_confirm.create();
//
//        // 아이콘
//        alert.setIcon(R.drawable.skull_icon);
//        // 다이얼로그 타이틀
//        alert.setTitle(mContext.getResources().getString(R.string.always_safe_on_dialog_title));
//        // 다이얼로그 보기
//        alert.show();

        // 명함 리스트에서 아이템 클릭하면 다이얼로그를 형성해준다
        new MaterialStyledDialog.Builder(mContext)
                .setTitle(mContext.getResources().getString(R.string.always_safe_on_dialog_confirm_button))
                .setDescription(mContext.getResources().getString(R.string.always_safe_on_dialog_message))
                .setHeaderDrawable(R.drawable.always_safe_back) // 배경화면 이미지
//                .withDarkerOverlay(true) // 배경화면 오버레이
                .withDialogAnimation(true, Duration.SLOW) // 다이얼로그 나타나고 사라질 때 애니메이션
                .setCancelable(true) // 바깥 눌렀을 때 다이얼로그 삭제
                .setPositiveText("OK")
                .onPositive( new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }

}
