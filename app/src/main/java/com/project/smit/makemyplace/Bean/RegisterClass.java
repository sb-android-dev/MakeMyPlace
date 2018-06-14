package com.project.smit.makemyplace.Bean;

import com.google.gson.annotations.SerializedName;
import com.project.smit.makemyplace.Parsing.ResponseUser;

/**
 * Created by Smit on 13-10-2017.
 */

public class RegisterClass {

    @SerializedName("fname")
    private String Fname;

    @SerializedName("lname")
    private String Lname;

    @SerializedName("email")
    private String Email;

    @SerializedName("password")
    private String Password;

    @SerializedName("gender")
    private String Gender;

    @SerializedName("mobile")
    private String Mobile;

    @SerializedName("image")
    private String Image;

    @SerializedName("response")
    private ResponseUser Response;

    public ResponseUser getResponse() {
        return Response;
    }
}
