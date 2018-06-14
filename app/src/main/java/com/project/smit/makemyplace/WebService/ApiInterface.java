package com.project.smit.makemyplace.WebService;

import com.project.smit.makemyplace.Bean.LoginClass;
import com.project.smit.makemyplace.Bean.RegisterClass;
import com.project.smit.makemyplace.Bean.RetrieveClass;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Smit on 13-10-2017.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("registration.php")
    Call<RegisterClass> registerUser(@Field("fname") String fname, @Field("lname") String lname, @Field("email") String email,
                                     @Field("password") String password, @Field("gender") String gender, @Field("mobile") String mobile,
                                     @Field("image") String image);

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginClass> loginUser(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("edit.php")
    Call<RegisterClass> editUser(@Field("id") String id, @Field("fname") String fname, @Field("lname") String lname, @Field("email") String email,
                                 @Field("gender") String gender, @Field("mobile") String mobile, @Field("image") String image);

    @FormUrlEncoded
    @POST("retrieve.php")
    Call<RetrieveClass> retrieveImage(@Field("act_name") String name);

    @FormUrlEncoded
    @POST("send_mail.php")
    Call<LoginClass> sendPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("change_pwd.php")
    Call<LoginClass> changePassword(@Field("id") String id, @Field("password") String password, @Field("new_pwd") String new_pwd);

    @FormUrlEncoded
    @POST("feedback.php")
    Call<LoginClass> sendFeedback(@Field("uname") String uname, @Field("uemail") String uemail, @Field("rating") int rating, @Field("feedback") String feedback);

}
