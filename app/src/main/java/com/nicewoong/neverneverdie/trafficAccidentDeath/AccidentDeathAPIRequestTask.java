package com.nicewoong.neverneverdie.trafficAccidentDeath;

import android.os.AsyncTask;
import android.util.Log;

import com.nicewoong.neverneverdie.ui.MainActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by nicewoong on 2017. 12. 9..
 *
 * Rest api 를 통해 공공데이터 포털로부터 교통사망사고 정보를 받아오는 작업을 하는 클래스입니다.
 * AsyncTask 를 상속하여 비동기적으로 thread 를 생성하여 Open API 서버와 통신합니다
 * background 작업을 실행시키기 위해서는 class 의 instance 를 생성하고 execute() 메서드를 호출합니다.
 * 특정 클래스에서 사용하기 위해 본 클래스에서 AsyncTask background작업을 하지 않고,
 * getRequestUrl(), publishHttpRequest() 를 활용해서 필요한 부분에서 AsyncTask Anonymous class 를 구현해서 사용하는 것이 유용할 것
 *
 */
public class AccidentDeathAPIRequestTask extends AsyncTask<URL, Void, JSONObject> {

    //공공데이터포털에서 발급받은 인증키
    // See : www.data.go.kr/
    private static String SERVICE_KEY = "PYWezzCNSGTHF5aJALsLoxzCziUi5d7B1jEPrU87TacdNZQoVimSQG%2FFBafP1RRBRZa1XVGX5babWe7wwSXr%2FQ%3D%3D";


    /**
     * background 작업을 하기 전 준비작업을 여기에서 수행합니다.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    /**
     * background 작업을 여기에서 수행합니다.
     * class 객체를 통해서 .execute() 메서드를 통해 아래 메서드를 실행시킬 수 있습니다
     *
     * @param urls HTTP request를 위한 URL 객체를 패러미터로 전달합니다
     * @return HTTP rqeust를 통해서 얻은 결과를 JSONObject 로 반환합니다
     */
    @Override
    protected JSONObject doInBackground(URL... urls) {

        Log.d(MainActivity.TAG_REST_API_TEST, "doInBackground 가 실행됩니다 " ); //화면에 출력해봅시다
        String resultString = "";

        try {
            resultString = publishHttpRequest(urls[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(MainActivity.TAG_REST_API_TEST, "doInBackground resultString = " + resultString); //화면에 출력해봅시다

        try {
            return new JSONObject(resultString);// 실제 리턴값으로 고쳐주자
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }// end of doInBackground()


    /**
     * 백그라운드 작업이 완료되고 후처리 작업을 여기에서.
     * doInBackground 메서드가 return 하는 결과(JSONObject)를 매개변수로 받습니다.
     ** 주의 ** result 로 input 되는 JSONObject 가 null 이 아닌지 확인절차가 필요!
     *
     * @param result doInBackground 메서드가 return 하는 결과(JSONObject)
     *
     */
    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
    }



    /**
     * 인자로 받은 URL 을 가지고 HTTP GET Request 를 수행합니다
     * request 수행 결과는 string 으로 반환합니다.
     *
     * @param url 공공데이터 포털의 교통사망사고 정보 Open API를 이용하기 위한 Request URL
     * @return HTTP GET request 수행 결과 String (JSONObject 형태)
     * @throws IOException
     */
    public String publishHttpRequest(URL url) throws IOException {

        Log.d(MainActivity.TAG_REST_API_TEST, "publishHttpRequest 가 실행됩니다 " );

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + httpURLConnection.getResponseCode());

        BufferedReader bufferedReader;
        if(httpURLConnection.getResponseCode() >= 200 && httpURLConnection.getResponseCode() <= 300) {
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
        } else {
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
        }

        StringBuilder resultStringBuilder = new StringBuilder();
        String lineFromBuffer;

        while ((lineFromBuffer = bufferedReader.readLine()) != null) {
            resultStringBuilder.append(lineFromBuffer);
        }

        bufferedReader.close();
        httpURLConnection.disconnect();

        Log.d(MainActivity.TAG_REST_API_TEST, "publishHttpRequest 가 종료됩니다 resultStringBuilder 값 = \n  " + resultStringBuilder ); //화면에 출력해봅시다

        return resultStringBuilder.toString();

    }




    /**
     * 공공데이터 포털의 교통사망사고 정보 Open API를 이용하기 위한 Request URL을 리턴받습니다.
     * 년도 시도 구군 을 설정하여 HTTP request 를 할 수 있는 URL 인스턴스를 리턴 받습니다
     *
     *
     * @param year 년도 (코드종류 코드값 14년도 2014 13년도 2013 12년도 2012)
     * @param siDoCode 시도 코드 (코드종류 코드값 전체 공백시 전체 선택 서울특별시 1100 부산광역시 1200 대전광역시 2500 대구광역시 2200 광주광역시 2400 인천광역시 2300 울산광역시 2600 세종특별자치시 2700 경기도 1300 강원도 1400 충청남도 1600 충청북도 1500 전라남도 1800 전라북도 1700 경상남도 2000 경상북도 1900 제주특별자치도 2100)
     * @param guGunCode 구군 코드
     * @return param 으로 설정한 변수에 맞추어 HTTP 요청을 위한 URL 인스턴스를 리턴한다
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     */
    public URL getRequestUrl(String year, String siDoCode, String guGunCode) throws UnsupportedEncodingException, MalformedURLException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552061/trafficAccidentDeath/getRestTrafficAccidentDeath"); /*URL*/

        /*Service Key*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + SERVICE_KEY);
        /*코드종류 코드값 14년도 2014 13년도 2013 12년도 2012 */
        urlBuilder.append("&" + URLEncoder.encode("searchYear","UTF-8") + "=" + URLEncoder.encode(year, "UTF-8"));
        /*코드종류 코드값 전체 공백시 전체 선택 서울특별시 1100 부산광역시 1200 대전광역시 2500 대구광역시 2200 광주광역시 2400 인천광역시 2300 울산광역시 2600 세종특별자치시 2700 경기도 1300 강원도 1400 충청남도 1600 충청북도 1500 전라남도 1800 전라북도 1700 경상남도 2000 경상북도 1900 제주특별자치도 2100 */
        urlBuilder.append("&" + URLEncoder.encode("siDo","UTF-8") + "=" + URLEncoder.encode(siDoCode, "UTF-8"));
        /*시도 코드종류 코드값 서울특별시 강남구 1116 강동구 1117 강북구 1124 강서구 1111 관악구 1115 광진구 1123 구로구 1112 금천구 1125 노원구 1122 도봉구 1107 동대문구 1105 동작구 1114 마포구 1110 서대문구 1109 서초구 1119 성동구 1104 성북구 1106 송파구 1118 양천구 1120 영등포구 1113 용산구 1103 은평구 1108 종로구 1101 중구 1*/
        urlBuilder.append("&" + URLEncoder.encode("guGun","UTF-8") + "=" + URLEncoder.encode(guGunCode, "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        return url;
    }

}// end of class




/* 아래와 같은 형식의 데이터를 받습니다
{
    "searchResult":{
        "accidentDeath":[
            {
            "year":"2014",
            "dt_006":"2014033105",
            "dt_006_lv8":"55",
            "cd_008":"2",
            "cd_007":"2",
            "no_010":1,
            "injpsn_co":1,
            "no_011":0,
            "no_012":0,
            "no_013":0,
            "cd_003":"1117",
            "cd_003_lv1":"1100",
            "cd_014_lv1":"02",
            "cd_014_lv2":"22",
            "cd_014":"22",
            "cd_027_1_lv1":"01",
            "cd_027_1_lv2":"02",
            "cd_043_lv1":"01",
            "cd_043":"05",
            "cd_036_1_lv1":"01",
            "cd_036_1":"03",
            "cd_036_1_lv2":"07",
            "cd_036_2":"21",
            "x_coord":"967249  ",
            "y_coord":"1948665 ",
            "grd_lo":"127.12929102",
            "grd_la":"37.53672269"
            },
             ...
            ]
    }
}
*/
