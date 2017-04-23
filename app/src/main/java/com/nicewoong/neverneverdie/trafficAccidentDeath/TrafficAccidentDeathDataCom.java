package com.nicewoong.neverneverdie.trafficAccidentDeath;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nicewoong.neverneverdie.ui.MainActivity;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nicewoong on 2017. 4. 22..
 * 공공데이터 TrafficAccidentDeath 데이터를 API서버로 요청 통신합니다
 */

public class TrafficAccidentDeathDataCom {

    private Context mContext;
    private List<AccidentDeath> TrafficAccidentDeathList; //데이터를 담을 리스트


    /**
     * constructor
     */
    public TrafficAccidentDeathDataCom(Context context){
        mContext = context;
    }

    /**
     * get Traffic Accident Death Data from 공공데이터포털
     * and Set the Data list
     * @param serviceKey
     * @param searchYearCode
     * @param sidoCode
     * @param gugunCode
     */
    public void setTrafficAccidentDeathList(String serviceKey, String searchYearCode, String sidoCode, String gugunCode) {

        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                Log.d(MainActivity.TAG_PROCEDURE_DEBUG, "와이파이가 연결되었네요.");
                getTrafficAccidentDeathListWithCallback(serviceKey, searchYearCode, sidoCode, gugunCode);


            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                Log.d(MainActivity.TAG_PROCEDURE_DEBUG, "모바일인터넷이 연결되었네요.");
                getTrafficAccidentDeathListWithCallback(serviceKey, searchYearCode, sidoCode, gugunCode);

            }
        } else {
            // not connected to the internet
            Log.d(MainActivity.TAG_PROCEDURE_DEBUG, "인터넷이 연결되지 않았습니다 .");
        }


    }// end of method setTrafficAccidentDeathList()




    public void getTrafficAccidentDeathListWithCallback(String serviceKey, String searchYearCode, String sidoCode, String gugunCode) {
        //Creating an object of our api interface
//        TrafficAccidentDeathApiService api = ServiceGenerator.getListApiService();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServiceGenerator.ACCIDENT_DEATH_API_ROOT_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        TrafficAccidentDeathApiService api = retrofit.create(TrafficAccidentDeathApiService.class);

        /**
         * Calling JSON
         */
        Call<TrafficAccidentDeath> call = api.getTrafficAccidentDeathList(serviceKey, searchYearCode, sidoCode, gugunCode);

        /**
         * Enqueue Callback will be call when get response...
         */
        call.enqueue(new Callback<TrafficAccidentDeath>() {
            @Override
            public void onResponse(Call<TrafficAccidentDeath> call, Response<TrafficAccidentDeath> response) {

                if (response.isSuccessful()) {
                    /**
                     * Got Successfully
                     */
                    TrafficAccidentDeathList = response.body().getSearchResult().getAccidentDeath();
                    Log.d(MainActivity.TAG_REST_API_TEST, "데이터를받아오는데 성공했습니다 : \n" + TrafficAccidentDeathList.toString());
                    /**
                     * Binding that List to Adapter
                     */
                } else {
                    Log.d(MainActivity.TAG_REST_API_TEST, "문제가 있나봅니다. response is not successful");
                }
            }

            @Override
            public void onFailure(Call<TrafficAccidentDeath> call, Throwable t) {
//                        Log.v("SearchActivity", "onFailure" + linenum);
//                        Toast.makeText(getApplicationContext(), linenum + "onFailure", Toast.LENGTH_SHORT).show();
                Log.d(MainActivity.TAG_REST_API_TEST, "공공데이터를 받아오는데 실패했습니다 : " + t.toString());
            }
        });
    }

}
