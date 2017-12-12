package com.nicewoong.neverneverdie.trafficAccidentDeath;

import android.util.Log;

import com.nicewoong.neverneverdie.ui.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nicewoong on 2017. 12. 10..
 * 공공데이터 Open API를 이용해 HTTP request로 얻은 result JSON 객체를 효율적으로 다루기 위한 클래스
 */

public class AccidentDeathData {


    private JSONArray entireObjectArray;
    private JSONArray accidentDeathList; // 전체 request result JSON object 에서 실제 accidentDeath 데이터가 포함된 array 부분을 뽑아서 저장

    /**
     * 생성자 constructor
     *
     * @param entireResultArray
     */
    public AccidentDeathData(JSONArray entireResultArray) {
        this.entireObjectArray =  entireResultArray;

        addAccidentDeathListFromEntireObject();

    }

    /**
     * 생성자 constructor
     *
     */
    public AccidentDeathData() {
        this.entireObjectArray = new JSONArray();
        addAccidentDeathListFromEntireObject();
    }


    /**
     * 전체 request result JSON object 에서 실제 accidentDeath 데이터가 포함된 array 부분을 뽑아서 저장
     */
    private void addAccidentDeathListFromEntireObject() {
        JSONArray addedAccidentDeathArray ;
        JSONObject searchResultObject;

        if(entireObjectArray ==null) {
            entireObjectArray = new JSONArray();
            accidentDeathList = new JSONArray();
            return;
        }


        try {

            for(int i = 0 ; i<entireObjectArray.length() ; i++){
                    searchResultObject = entireObjectArray.getJSONObject(i).getJSONObject("searchResult");
                    addedAccidentDeathArray = searchResultObject.getJSONArray("accidentDeath");

                    if(this.accidentDeathList != null) { // 이미 존재하는 것이 있으면 새로 얻은 reuslt 를 추가해줍니다
                        for (int j = 0; j < addedAccidentDeathArray.length(); j++) {
                            this.accidentDeathList.put(addedAccidentDeathArray.get(i));
                        }
                    }else {
                        this.accidentDeathList = addedAccidentDeathArray;
                    }
            }


        } catch (Exception e) {
            e.printStackTrace();
            accidentDeathList = new JSONArray();
        }

    }// end of addAccidentDeathListFromEntireObject();


    /**
     * private 으로 선언되어 값을 저장하고 있는 getAccidentDeathList 를 반환
     * @return accidentDeathList JSONArray
     */
    public JSONArray getAccidentDeathList() {
        if (accidentDeathList == null) {
            accidentDeathList = new JSONArray();
        }
        return accidentDeathList;
    }





}// end of class



/* entireObject 하나 당 아래와 같은 형식을 구성하고 있습니다
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
