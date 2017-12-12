package com.nicewoong.neverneverdie.backgroundService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.nicewoong.neverneverdie.ui.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import static com.nicewoong.neverneverdie.application.MyApplication.CURRENT_LATITUDE;
import static com.nicewoong.neverneverdie.application.MyApplication.CURRENT_LONGITUDE;
import static com.nicewoong.neverneverdie.ui.MainActivity.TAG_PROCEDURE_DEBUG;

/**
 * Created by nicewoong on 2017. 12. 11..
 * Always Safe On 이 되었을 때 백그라운드에서 단말기 위치를 실시간으로 파악하고, 위험지역과 가까울 경우 notification 을 부르는 작업을 수행한다
 * Current Lat Lng 과 Default Lat Lng 은 application 클래스의 global variable 로 접근하여 값을 관리한다
 */

public class AlwaysSafeService extends Service implements LocationListener {


    LocationManager locationManager; // 현재 단말기 위치를 관리하기 위한 매니저 객체


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        setUpLocationManager();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        locationManager.removeUpdates(this); // location 계속 변하도록 설정해놓은 update 를 종료합니다
        super.onDestroy();
    }


    /**
     * 현재 단말기 위치를 받아오기 위해 location Manager 를 생성하고 지속적인 위치 갱신 작업을 요청합니다
     */
    public void setUpLocationManager() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }



    /**
     * 현재 단말기 위치 주변 100미터 내에 교통사망사고 발생지가 있는지 체크합니다
     * Current Lat, Lng 을 가지고 교통사망사고데이터 반복문을 돌면서 Lat, Lng 을 비교
     * @return 현재 위치 100m 이내에 교통사망사고 발생지가 있으면 True 없으면 False
     */
    public boolean isDangerousAround() {
        JSONArray accidentDataList = MainActivity.accidentDeathData.getAccidentDeathList();
        if (accidentDataList == null) {
            Log.d(TAG_PROCEDURE_DEBUG, "isDangerousAround(), accidentDataList is NULL! " );
            return false;
        }

        double distance ;
        for( int i = 0; i < accidentDataList.length() ; i++) {
            double lat = 0;
            double lng = 0;
            try {
                lng = Double.parseDouble(accidentDataList.getJSONObject(i).getString("grd_lo")); // longitude
                lat = Double.parseDouble(accidentDataList.getJSONObject(i).getString("grd_la")); // latitude
                distance = getDistanceFromCurrent(lat, lng);
                Log.d(TAG_PROCEDURE_DEBUG, "isDangerousAround(), distance = " + distance);

                if (distance < 100) // 100 미터 이내에 있으면 true 반환
                    return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return false;

    }


    /**
     *

     * @return
     */

    /**
     * 특정 사고 지점의 lat lng 을 가지고 현재위치와의 거리를 비교합니다.
     * @param lat 특정 사고 지점의 latitude (위도)
     * @param lng 특정 사고 지점의 longitude (경도)
     * @return param 으로 받은 lat lng 과 현재 단말기 위치와의 거리를 double 로 반환
     */
    public double getDistanceFromCurrent(double lat, double lng) {
        double distance;

        Location locationCurrent = new Location("point CURRENT");

        locationCurrent.setLatitude(CURRENT_LATITUDE);
        locationCurrent.setLongitude(CURRENT_LONGITUDE);

        Location locationAccident = new Location("point ACCIDENT LOCATION");

        locationAccident.setLatitude(lat);
        locationAccident.setLongitude(lng);

        distance = locationCurrent.distanceTo(locationAccident);
        return distance;
    }

    /**
     *  // remove comment -> 현재 단말기 위치 주변 100미터 내에 교통사망사고 발생지가 있다면
     *  즉 현재위치가 위험지역으로 판단된다면
     *  Notification 을 줍니다
     */
    public void sendDangerousAroundNotification(Context context) {

    }

    //============== LocationListener Override Methods ==============//

    // 새로운 location 값 얻었을 때 호출되는 리스너
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG_PROCEDURE_DEBUG,  "AlwaysSafeService.onLocationChanged() = (" + location.getLatitude() + ",  " + location.getLongitude()  + ")");

        CURRENT_LATITUDE = location.getLatitude();
        CURRENT_LONGITUDE = location.getLongitude();

        Log.d(TAG_PROCEDURE_DEBUG,  "isDangerousAround() in AlwaysSafeService.onLocationChanged :  " + isDangerousAround());


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
