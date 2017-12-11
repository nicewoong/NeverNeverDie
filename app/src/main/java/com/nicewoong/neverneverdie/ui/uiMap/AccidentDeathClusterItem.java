package com.nicewoong.neverneverdie.ui.uiMap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nicewoong on 2017. 12. 11..
 * GoogleMap Maker 를 클러스터링 하여 AccidentDeath Data 를 표시하기 위한 클래스
 *
 */

public class AccidentDeathClusterItem implements ClusterItem {

    public JSONObject accidentDeathItemObject;
    public AccidentDeathClusterItem(JSONObject accidentDeathItemObject) {
        this.accidentDeathItemObject = accidentDeathItemObject;
    }



    @Override
    public LatLng getPosition() {


        if(accidentDeathItemObject!=null) {
            LatLng latLng = null;
            try {
                latLng = new LatLng(
                        Double.parseDouble(accidentDeathItemObject.getString("grd_la")),
                        Double.parseDouble(accidentDeathItemObject.getString("grd_lo"))
                );
                return latLng;

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return null;
    }
}
