package com.project.smit.makemyplace.Parsing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Smit on 26-10-2017.
 */

public class Response {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("cat")
    @Expose
    private String cat;
    /*@SerializedName("error")
    @Expose
    private String error;*/

    public Response(String id, String name, String address, String phone, String image, String latitude, String longitude, String cat) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cat = cat;
    }

    public String getId() { return id; }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage() { return image; }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() { return longitude; }

    public String getCat() { return cat; }

    //public String getError() { return error; }
}
