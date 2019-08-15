package br.net.easify.marvelapitest.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataResponse {

    @SerializedName("offset")
    @Expose
    private int offset;

    @SerializedName("limit")
    @Expose
    private int limit;

    @SerializedName("total")
    @Expose
    private int total;

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("results")
    @Expose
    private List<ResultResponse> results = null;

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public int getTotal() {
        return total;
    }

    public int getCount() {
        return count;
    }

    public List<ResultResponse> getResults() {
        return results;
    }
}
