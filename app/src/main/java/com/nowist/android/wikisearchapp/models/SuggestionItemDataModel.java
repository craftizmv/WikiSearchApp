package com.nowist.android.wikisearchapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuggestionItemDataModel {

    @Expose
    @SerializedName("title")
    private String title = "";

    @Expose
    @SerializedName("desc")
    private String desc = "";

    @Expose
    @SerializedName("imageUrl")
    private String imageUrl = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

