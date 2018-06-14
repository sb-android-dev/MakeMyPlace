package com.project.smit.makemyplace.Parsing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Smit on 26-10-2017.
 */

public class ResponseUser {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("data_code")
    @Expose
    private String data_code;

    public ResponseUser(String id, String fname, String lname, String email, String password, String gender, String mobile, String image) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.mobile = mobile;
        this.image = image;
    }

    public String getId() { return id; }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getMobile() {
        return mobile;
    }

    public String getImage() { return image; }

    public String getData_code() {
        return data_code;
    }
}
