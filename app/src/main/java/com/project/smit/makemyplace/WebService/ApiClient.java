package com.project.smit.makemyplace.WebService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Smit on 13-10-2017.
 */

public class ApiClient {

    //private static final String BaseUrl = "http://192.168.0.103/API/";
    private static final String BaseUrl = "https://makemyplace.000webhostapp.com/";
    private static Retrofit retrofit;

    public static Retrofit getApiClient(){
        if(retrofit==null){
            retrofit = new Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
