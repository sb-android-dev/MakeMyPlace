package com.project.smit.makemyplace.MakeMyPlace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.login.LoginManager;
import com.project.smit.makemyplace.Activity.Login;

import java.util.HashMap;


public class UserSessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;


    private static final String PREFER_NAME ="RegistrationForm";
    public static final String KEY_userId = "id";
    public static final String KEY_Fname = "fname";
    public static final String KEY_Lname = "lname";
    public static final String KEY_email = "email";
    public static final String KEY_pwd = "password";
    public static final String KEY_Gender = "gender";
    public static final String KEY_Mobile = "mobile";
    public static final String KEY_Image = "image";


    public UserSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
        //editor1=pref.edit();
    }

    public void createUserLoginSession(String userId, String fname, String lname, String email, String pwd, String gender, String mobile,String image) {
        Log.e("createUserLoginSession","createUserLoginSession");
        editor.putString(KEY_userId, userId);
        editor.putString(KEY_Fname, fname);
        editor.putString(KEY_Lname, lname);
        editor.putString(KEY_email, email);
        editor.putString(KEY_pwd, pwd);
        editor.putString(KEY_Gender, gender);
        editor.putString(KEY_Mobile, mobile);
        editor.putString(KEY_Image, image);

        editor.commit();
    }

    public void UserLoginSession(String accId){
        Log.e("UserLoginSession","UserLoginSession");
        editor.putString(KEY_userId, accId);

        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_userId, pref.getString(KEY_userId, null));
        user.put(KEY_Fname, pref.getString(KEY_Fname, null));
        user.put(KEY_Lname, pref.getString(KEY_Lname, null));
        user.put(KEY_email, pref.getString(KEY_email, null));
        user.put(KEY_pwd, pref.getString(KEY_pwd, null));
        user.put(KEY_Gender, pref.getString(KEY_Gender, null));
        user.put(KEY_Mobile, pref.getString(KEY_Mobile, null));
        user.put(KEY_Image, pref.getString(KEY_Image, null));
        return user;
    }

    public void logoutUser1(){
        editor.clear();
        editor.commit();
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        LoginManager.getInstance().logOut();
        Intent i = new Intent(_context, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);
    }

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

}
