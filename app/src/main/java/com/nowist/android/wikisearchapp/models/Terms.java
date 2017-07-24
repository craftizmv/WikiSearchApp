package com.nowist.android.wikisearchapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Terms {

    @SerializedName("description")
    @Expose
    private List<String> description = null;

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

}