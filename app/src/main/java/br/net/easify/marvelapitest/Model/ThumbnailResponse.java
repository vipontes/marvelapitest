package br.net.easify.marvelapitest.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ThumbnailResponse {

    @SerializedName("path")
    @Expose
    private String path;

    @SerializedName("extension")
    @Expose
    private String extension;

    public String getPath() {
        return path;
    }

    public String getExtension() {
        return extension;
    }
}
