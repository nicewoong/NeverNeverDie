
package com.nicewoong.neverneverdie.trafficAccidentDeath;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResult {

    @SerializedName("accidentDeath")
    @Expose
    private List<AccidentDeath> accidentDeath = null;

    public List<AccidentDeath> getAccidentDeath() {
        return accidentDeath;
    }

    public void setAccidentDeath(List<AccidentDeath> accidentDeath) {
        this.accidentDeath = accidentDeath;
    }

}
