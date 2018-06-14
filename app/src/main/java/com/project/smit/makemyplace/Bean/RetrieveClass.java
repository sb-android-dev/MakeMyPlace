package com.project.smit.makemyplace.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.smit.makemyplace.Parsing.Response;

/**
 * Created by Smit on 24-10-2017.
 */

public class RetrieveClass {

    /*@SerializedName("act_name")
    private String name;*/

    @SerializedName("response")
    @Expose
    private Response[] response;

    public Response[] getResponse() {
        return response;
    }

}
