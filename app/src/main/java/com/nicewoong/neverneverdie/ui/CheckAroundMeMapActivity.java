package com.nicewoong.neverneverdie.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nicewoong.neverneverdie.MyApplication;
import com.nicewoong.neverneverdie.R;
import com.nicewoong.neverneverdie.trafficAccidentDeath.AccidentDataCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.nicewoong.neverneverdie.ui.MainActivity.TAG_PROCEDURE_DEBUG;

public class CheckAroundMeMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    public MapFragment mapFragment;
    public Location mLastLocation;
    public static double DEFAULT_LATITUDE = 35.8900521;
    public static double DEFAULT_LONGITUDE = 128.6113282;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_around_me_map);

        Log.d(TAG_PROCEDURE_DEBUG, "CheckAroundMapActivity 가 열렸습니다");

        //Set GoogleMapFragment
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); // Callback 을 this 로 설정한 것!

        //set mGoogleApiClient
        setGoogleApiClient();

    }

    /**
     * MyApplication 에 static 변수로 선언해놓은 googleApiClient 에 값을 세팅합니다
     *
     */
    private void setGoogleApiClient() {
        if (MyApplication.mGoogleApiClient == null) {
            Log.d(TAG_PROCEDURE_DEBUG,"mGoogleApiClient 를 세팅하고 요청합니다 ");

            MyApplication.mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void onStart() {
        MyApplication.mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        MyApplication.mGoogleApiClient.disconnect();
        super.onStop();
    }


    /**
     * Google Map이 준비가 되었을 때, 지도에 대한 설정을 해줍니다
     *
     * @param map
     */
    @Override
    public void onMapReady(GoogleMap map) {
        Log.d(TAG_PROCEDURE_DEBUG, "맵이 준비되었습니다 ~!");

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE), 15));

        // 현재위치 중심 마커를 설정합니다.
        map.addMarker(new MarkerOptions()
                .position(new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE))
                .title("Current Location"));

        // 100m 주변으로 원을그려줍니다
        // 우선 서클옵션을 만들고
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE))
                .radius(500)
                .strokeWidth(0)
                .fillColor(Color.argb(50, 255, 0, 0));

        //옵션을 활용해 써클을 추가합니다
        Circle circle = map.addCircle(circleOptions); // return 되는 circle 은 나중에 변경가능 (Mutable)

        addDangerousLocationMarker(map);

    }

    /**
     * create Test data!! ( 대구광역시 중구 )
     * @param map
     */
    public void addDangerousLocationMarker(GoogleMap map) {


        /*2014 대구*/
        addTestMarkerByJsonArray(map, AccidentDataCreator.getTestJsonDataList(AccidentDataCreator.Daegu_bukgu_data));
        addTestMarkerByJsonArray(map, AccidentDataCreator.getTestJsonDataList(AccidentDataCreator.Daegu_Jungu_data));
        addTestMarkerByJsonArray(map, AccidentDataCreator.getTestJsonDataList(AccidentDataCreator.Daegu_Dongu_data));
        addTestMarkerByJsonArray(map, AccidentDataCreator.getTestJsonDataList(AccidentDataCreator.Daegu_Namgu_data));
        addTestMarkerByJsonArray(map, AccidentDataCreator.getTestJsonDataList(AccidentDataCreator.Daegu_Seogu_data));
        addTestMarkerByJsonArray(map, AccidentDataCreator.getTestJsonDataList(AccidentDataCreator.Daegu_Dalseogu_data));
        addTestMarkerByJsonArray(map, AccidentDataCreator.getTestJsonDataList(AccidentDataCreator.Daegu_Suseonggu_data));
        addTestMarkerByJsonArray(map, AccidentDataCreator.getTestJsonDataList(AccidentDataCreator.Daegu_Dalsung_data));

    }

    /**
     * Json Array에 있는 데이터를 선별적으로 뽑아 map에 표현합니다
     * @param map
     * @param testArrayList
     */
    public void addTestMarkerByJsonArray(GoogleMap map, JSONArray testArrayList) {


        for(int i = 0; i<testArrayList.length() ; i++) {
            try {
                JSONObject currentAccidentJsonObject = testArrayList.getJSONObject(i);

                map.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble((currentAccidentJsonObject.getString("grd_la"))), Double.parseDouble(currentAccidentJsonObject.getString("grd_lo"))))
                        .title("사망사고발생지")
                        .snippet("year : "+ currentAccidentJsonObject.getString("year")+ ", Death : "+ currentAccidentJsonObject.getString("no_010") + " , injured : " + currentAccidentJsonObject.getString("injpsn_co"))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.skull_icon)));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    /**
     * current Location 을 파악합니다
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            Log.d(TAG_PROCEDURE_DEBUG, "oh.... Permission not granted !!!!");
//            //return;
//        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(MyApplication.mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d(TAG_PROCEDURE_DEBUG, "새로 파악한 latitude = " + String.valueOf(mLastLocation.getLatitude()));
            Log.d(TAG_PROCEDURE_DEBUG, "새로 파악한 latitude = " + String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG_PROCEDURE_DEBUG, "onConnectionSuspended : " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG_PROCEDURE_DEBUG, "onConnectionFailed : " + connectionResult);
    }
}
