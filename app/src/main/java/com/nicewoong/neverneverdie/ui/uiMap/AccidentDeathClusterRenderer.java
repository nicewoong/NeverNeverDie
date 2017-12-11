package com.nicewoong.neverneverdie.ui.uiMap;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.nicewoong.neverneverdie.R;

import org.json.JSONException;

/**
 * Created by nicewoong on 2017. 12. 11..
 */

public class AccidentDeathClusterRenderer extends DefaultClusterRenderer<AccidentDeathClusterItem> {


    /**
     * 생성자
     * @param context
     * @param map
     * @param clusterManager
     */
    public AccidentDeathClusterRenderer(Context context, GoogleMap map, ClusterManager<AccidentDeathClusterItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(AccidentDeathClusterItem item, MarkerOptions markerOptions) {

        if(item.accidentDeathItemObject==null)
            return;

        try {
            markerOptions.snippet("year : "+ item.accidentDeathItemObject.getString("year")+ ", Death : "+ item.accidentDeathItemObject.getString("no_010") + " , injured : " + item.accidentDeathItemObject.getString("injpsn_co"));
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.skull_icon));
            markerOptions.title("사망사고발생지");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onBeforeClusterItemRendered(item, markerOptions);

    }




}
