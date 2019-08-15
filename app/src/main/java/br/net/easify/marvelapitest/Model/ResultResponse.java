package br.net.easify.marvelapitest.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultResponse {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("modified")
    @Expose
    private String modified;

    @SerializedName("thumbnail")
    @Expose
    private ThumbnailResponse thumbnail;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getModified() {
        return modified;
    }

    public ThumbnailResponse getThumbnail() {
        return thumbnail;
    }
}
