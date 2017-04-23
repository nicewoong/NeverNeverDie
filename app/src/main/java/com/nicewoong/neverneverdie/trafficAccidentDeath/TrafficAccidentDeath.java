
package com.nicewoong.neverneverdie.trafficAccidentDeath;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrafficAccidentDeath {

    @SerializedName("searchResult")
    @Expose
    private SearchResult searchResult;
    @SerializedName("resultCode")
    @Expose
    private String resultCode;

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

}
