package com.example.bybocam.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InfluenceData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("Infulenname")
    @Expose
    private String infulenname;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("discription")
    @Expose
    private String discription;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("Infulenlatitude")
    @Expose
    private String infulenlatitude;
    @SerializedName("Infulenlongitude")
    @Expose
    private String infulenlongitude;
    @SerializedName("race")
    @Expose
    private String race;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("industry")
    @Expose
    private String industry;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInfulenname() {
        return infulenname;
    }

    public void setInfulenname(String infulenname) {
        this.infulenname = infulenname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getInfulenlatitude() {
        return infulenlatitude;
    }

    public void setInfulenlatitude(String infulenlatitude) {
        this.infulenlatitude = infulenlatitude;
    }

    public String getInfulenlongitude() {
        return infulenlongitude;
    }

    public void setInfulenlongitude(String infulenlongitude) {
        this.infulenlongitude = infulenlongitude;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
