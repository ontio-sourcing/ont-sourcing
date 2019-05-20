package com.ontology.sourcing.model.contract.input;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metadata {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Tags")
    @Expose
    private String tags;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Timestamp")
    @Expose
    private String timestamp;
    @SerializedName("Location")
    @Expose
    private String location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}