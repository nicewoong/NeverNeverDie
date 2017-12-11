package com.nicewoong.neverneverdie.ui.uiMap;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.nicewoong.neverneverdie.MyApplication;
import com.nicewoong.neverneverdie.R;
import com.nicewoong.neverneverdie.ui.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.nicewoong.neverneverdie.ui.MainActivity.TAG_PROCEDURE_DEBUG;

public class CheckAroundMeMapActivity extends FragmentActivity implements OnMapReadyCallback, ClusterManager.OnClusterItemClickListener<AccidentDeathClusterItem>,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    public MapFragment mapFragment;
    public Location mLastLocation;
    public static double DEFAULT_LATITUDE = 35.8900521;
    public static double DEFAULT_LONGITUDE = 128.6113282;

    ClusterManager<AccidentDeathClusterItem> mClusterManager; //Clustering Item 을 위한 매니저 객체
    LocationManager locationManager; // 현재 단말기 위치를 관리하기 위한 매니저 객체

    Circle currentCircle; // 현재 단말기 위치 중심을 가리키는 Circle Overlay 객체 다룰 글로벌 변수
    Marker currentMarker; // 현재 단말기 위치 중심을 가리키는 Marker 객체 다룰 글로벌 변수
    GoogleMap googleMap;

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

        //init locationManager instance
        // TODO: 2017. 12. 11. consider Adding permission check
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


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
        googleMap = map;
        Log.d(TAG_PROCEDURE_DEBUG, "맵이 준비되었습니다 ~!");

        setUpClusterManager(map); //Marker 를 cluster item 으로 표시하기 위한 ClusterManager 클래스를 생성하고 리스너를 등록하는 등의 초기화 작업


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE), 15));


        setCurrentLocationMarker(map, new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE));

        // AccidentDeath data 를 저장하고 있는 배열을 통해서 각각의 element에 대하여 maker로 지도위에 표시합니다
        addClusterMarkerByAccidentDeathArray(map, MainActivity.accidentDeathData.getAccidentDeathList());

//        //Alert dialog 띄우기  -> 그냥 바로 띄우기. 보여주기용
//        NeverDieDialog alertDialog = new NeverDieDialog(this);
//        alertDialog.showDangerousAlertDialog();

    }// end of onMapReady()


    public void setCurrentLocationMarker(GoogleMap map, LatLng currentLatLng) {
        if(currentCircle == null || currentMarker == null) {
            // 현재위치 중심 마커를 설정합니다. return 되는 marker 는 나중에 변경가능
            currentMarker = map.addMarker(new MarkerOptions()
                    .position(currentLatLng)
                    .title("Current Location"));

            // 100m 주변으로 원을그려줍니다
            // 우선 서클옵션을 만들고
            CircleOptions circleOptions = new CircleOptions()
                    .center(currentLatLng)
                    .radius(500)
                    .strokeWidth(0)
                    .fillColor(Color.argb(50, 255, 0, 0));

            //옵션을 활용해 써클을 추가합니다
            currentCircle = map.addCircle(circleOptions); // return 되는 currentCircle 은 나중에 변경가능 (Mutable)
        }else {
            currentMarker.setPosition(currentLatLng);
            currentCircle.setCenter(currentLatLng);

        }



    }

    /**
     * Marker 를 cluster item 으로 표시하기 위한 ClusterManager 클래스를 생성하고 리스너를 등록하는 등의 초기화 작업
     * @param googleMap
     */
    public void setUpClusterManager(GoogleMap googleMap) {
        mClusterManager = new ClusterManager<>(this, googleMap);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setRenderer(new AccidentDeathClusterRenderer(this, googleMap,mClusterManager)); // cluster marker 가 그려지는 모습을 설정합니다
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
    }

    /**
     * Json Array 에 있는 데이터를 선별적으로 뽑아 map 에 개별 Marker 를 추가하여 표현합니다
     * @param map
     * @param accidentDeathList
     */
    public void addMarkerByAccidentDeathArray(GoogleMap map, JSONArray accidentDeathList) {
        Log.d(TAG_PROCEDURE_DEBUG, "accidentDeathList.length() = " + accidentDeathList.length() );



        for(int i = 0; i < accidentDeathList.length() ; i++) {
            try {
                JSONObject currentAccidentJsonObject = accidentDeathList.getJSONObject(i);

                Log.d(TAG_PROCEDURE_DEBUG, "currentAccidentJsonObject_ "+i+" = " + currentAccidentJsonObject.toString() );


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
     * Json Array 에 있는 데이터를 하나씩 뽑아 map 에 ClusterMarker 로 추가하여 표현합니다 (클러스터링이 적용됨)
     * @param map
     * @param accidentDeathList
     */
    public void addClusterMarkerByAccidentDeathArray(GoogleMap map, JSONArray accidentDeathList) {
        Log.d(TAG_PROCEDURE_DEBUG, "addClusterMarkerByAccidentDeathArray.length() = " + accidentDeathList.length() );



        for(int i = 0; i < accidentDeathList.length() ; i++) {
            try {
                JSONObject currentAccidentJsonObject = accidentDeathList.getJSONObject(i);
                AccidentDeathClusterItem clusterItem = new AccidentDeathClusterItem(currentAccidentJsonObject);
                mClusterManager.addItem(clusterItem);

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
        Log.d(TAG_PROCEDURE_DEBUG, "onConnected : " );

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
            Log.d(TAG_PROCEDURE_DEBUG, "새로 파악한 Longitude = " + String.valueOf(mLastLocation.getLongitude()));
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

    // Cluster Item 하나가 클릭되었을 때 호출되는 리스너
    @Override
    public boolean onClusterItemClick(AccidentDeathClusterItem accidentDeathClusterItem) {
        Log.d(TAG_PROCEDURE_DEBUG, "onClusterItemClick is invoked. " );


        return false;
    }

    // location 이 변경될 때마다 호출되는 콜백
    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Log.d(TAG_PROCEDURE_DEBUG,  "onLocationChanged() = (" + lat + ",  " + lng  + ")");

        if(googleMap!=null)
            setCurrentLocationMarker(googleMap, new LatLng(lat,lng)); // 지도상 현재 위치 갱신
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
