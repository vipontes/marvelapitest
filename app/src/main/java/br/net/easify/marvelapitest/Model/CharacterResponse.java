package br.net.easify.marvelapitest.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CharacterResponse {

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("copyright")
    @Expose
    private String copyright;

    @SerializedName("attributionText")
    @Expose
    private String attributionText;

    @SerializedName("attributionHTML")
    @Expose
    private String attributionHTML;

    @SerializedName("etag")
    @Expose
    private String etag;

    @SerializedName("data")
    @Expose
    private DataResponse data;

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getAttributionText() {
        return attributionText;
    }

    public String getAttributionHTML() {
        return attributionHTML;
    }

    public String getEtag() {
        return etag;
    }

    public DataResponse getData() {
        return data;
    }
}
