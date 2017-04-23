package com.nicewoong.neverneverdie.trafficAccidentDeath;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nicewoong on 2017. 4. 20..
 * A fundamental class for "Retrofit library"
 * Reference : http://falinrush.tistory.com/5
 *
 * REST 방식의 인증키 활용
 * http://apis.data.go.kr/B552061/trafficAccidentDeath/
 * 오퍼레이션명?servicekey=서비스키&searchYearCd=연도코드&sido=법정동시도코드&gugun=법정동시군구코드
 *
 * -> http://apis.data.go.kr/B552061/trafficAccidentDeath/getRestTrafficAccidentDeath?servicekey=서비스키&searchYear=2014&siDo=1100&guGun=1117
 */
public class ServiceGenerator {

    public static final String ACCIDENT_DEATH_API_ROOT_URL = "http://apis.data.go.kr/B552061/trafficAccidentDeath/";//base Url은 '/'로 끝나야 한다.


    /**
     * Get Retrofit Instance
     */
    private static Retrofit getTrafficAccidentDeathListInstance() {
        return new Retrofit.Builder()
                .baseUrl(ACCIDENT_DEATH_API_ROOT_URL) //base Url은 '/'로 끝나야 한다.
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    /**
     * Get API Service
     *
     * @return API Service
     */
    public static TrafficAccidentDeathApiService getListApiService() {
        return getTrafficAccidentDeathListInstance().create(TrafficAccidentDeathApiService.class);
    }







}
