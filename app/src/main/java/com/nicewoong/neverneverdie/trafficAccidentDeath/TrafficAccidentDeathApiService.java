package com.nicewoong.neverneverdie.trafficAccidentDeath;

import com.nicewoong.neverneverdie.trafficAccidentDeath.TrafficAccidentDeath;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by nicewoong on 2017. 4. 22..
 *
 *
 * REST 방식의 인증키 활용
 * http://apis.data.go.kr/B552061/trafficAccidentDeath/
 * getRestTrafficAccidentDeath?servicekey=서비스키&searchYearCd=연도코드&sido=법정동시도코드&gugun=법정동시군구코드
 *
 * -> http://apis.data.go.kr/B552061/trafficAccidentDeath/getRestTrafficAccidentDeath?servicekey=서비스키&searchYear=2014&siDo=1100&guGun=1117
 *
 */

/*
    Pulbic data API KEY (for "trafficAccidentDeath")
    PYWezzCNSGTHF5aJALsLoxzCziUi5d7B1jEPrU87TacdNZQoVimSQG%2FFBafP1RRBRZa1XVGX5babWe7wwSXr%2FQ%3D%3D
*/


public interface TrafficAccidentDeathApiService {

    //@GET의 내용은 공공데이터 페이지를 조금만 살펴보면 알 수 있다.
//    @GET("api/subway/{apikey}/json/stationByLine/1/100/{linenum}")
//    @GET("getRestTrafficAccidentDeath?servicekey={serviceKey}&searchYearCd={searchYearCode}&sido={sidoCode}&gugun={gugunCode}")
//    Call<TrafficAccidentDeath> getTrafficAccidentDeathList(
//            @Path("serviceKey") String serviceKey,
//            @Path("searchYearCode") String searchYearCode,
//            @Path("sidoCode") String sidoCode,
//            @Path("gugunCode") String gugunCode
//    );

    @GET("getRestTrafficAccidentDeath")
    Call<TrafficAccidentDeath> getTrafficAccidentDeathList(
            @Query("servicekey") String servicekey,
            @Query("searchYear") String searchYear,
            @Query("siDo") String siDo,
            @Query("guGun") String guGun
    );




}
