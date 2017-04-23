package com.nicewoong.neverneverdie.ui;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nicewoong.neverneverdie.R;

public class CheckAroundMeMapActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_around_me_map);

        Log.d(MainActivity.TAG_PROCEDURE_DEBUG,"CheckAroundMapActivity 가 열렸습니다");

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d(MainActivity.TAG_PROCEDURE_DEBUG,"맵이 준비되었습니다 ~!");

        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("hello!"));
    }


}
