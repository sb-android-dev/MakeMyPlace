package com.project.smit.makemyplace.Bean;

import com.google.gson.annotations.SerializedName;
import com.project.smit.makemyplace.Parsing.ResponseUser;

/**
 * Created by Smit on 13-10-2017.
 */

public class LoginClass {

    /*@SerializedName("email")
    private String Email;

    @SerializedName("password")
    private String Password;*/

    @SerializedName("response")
    private ResponseUser Response;

    public ResponseUser getResponse() {
        return Response;
    }
}
