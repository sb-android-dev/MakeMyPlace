package com.project.smit.makemyplace.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.smit.makemyplace.Bean.LoginClass;
import com.project.smit.makemyplace.MakeMyPlace.ConnectionDetector;
import com.project.smit.makemyplace.MakeMyPlace.UserSessionManager;
import com.project.smit.makemyplace.R;
import com.project.smit.makemyplace.WebService.ApiClient;
import com.project.smit.makemyplace.WebService.ApiInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class Feedback extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText feedback;
    RatingBar ratingBar;
    Button send;
    TextView username;
    ImageView profilePic;
    Dialog logoutDialog;
    ConnectionDetector detector;
    UserSessionManager sessionManager;
    HashMap<String,String> map;
    String uname,uemail;

//    String SUBJECT = "Feedback about App";
    int RATINGS;
    String FEEDBACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_feedback).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        username = (TextView)header.findViewById(R.id.tvUsername);
        profilePic = (ImageView)header.findViewById(R.id.imageView);

        detector = new ConnectionDetector(Feedback.this);
        sessionManager = new UserSessionManager(Feedback.this);
        map = sessionManager.getUserDetails();

        if(map.get(UserSessionManager.KEY_Fname)!=null && map.get(UserSessionManager.KEY_Lname)!=null){
            uname = map.get(UserSessionManager.KEY_Fname)+" "+map.get(UserSessionManager.KEY_Lname);
        }else if(map.get(UserSessionManager.KEY_Fname)!=null && map.get(UserSessionManager.KEY_Lname)==null){
            uname = map.get(UserSessionManager.KEY_Fname);
        }

        if(map.get(UserSessionManager.KEY_email)!=null){
            uemail = map.get(UserSessionManager.KEY_email);
        }else{
            uemail = "Facebook Login";
        }

        username.setText(uname);
        stringToImage(map.get(UserSessionManager.KEY_Image));

        feedback = (EditText)findViewById(R.id.etFeedback);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        send = (Button)findViewById(R.id.btnSend);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String NAME = name.getText().toString();
                getRating();

//                String MESSAGE = "Name: "+NAME+"\nRatings: "+RATINGS+"\nFeedback: "+FEEDBACK;

                if(feedback.getText().toString().equals("")){
                    feedback.setError("Please enter your feedback");
                }else {
                    getFeedback();
//                    Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
//                            "mailto","makemyplace1@gmail.com", null));
//                email.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
//                email.putExtra(Intent.EXTRA_EMAIL, new String[]{TO});
//                    email.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
//                    email.putExtra(Intent.EXTRA_TEXT, MESSAGE);
//                email.setType("text/plain");

//                    try{
//                        startActivity(Intent.createChooser(email,"Choose email client"));
//                    }catch(ActivityNotFoundException e){
//                        Toast.makeText(getApplicationContext(),"No Email App installed",Toast.LENGTH_SHORT).show();
//                    }finally {
//                        finish();
//                    }
                }
            }
        });
    }

    private void getFeedback() {
        final ProgressDialog progressDialog = new ProgressDialog(Feedback.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LoginClass> call = apiInterface.sendFeedback(uname,uemail,getRating(),feedback.getText().toString());
        call.enqueue(new Callback<LoginClass>() {
            @Override
            public void onResponse(Call<LoginClass> call, retrofit2.Response<LoginClass> response) {
                LoginClass loginClass = response.body();
                if (loginClass != null) {
                    String data_code = loginClass.getResponse().getData_code();
                    progressDialog.dismiss();
                    if(data_code.equals("2001")){
                        Toast.makeText(getApplicationContext(),"Thank you for your feedback",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Feedback.this,MainActivity.class));
                        finish();
                    }else if(data_code.equals("2002")){
                        Toast.makeText(getApplicationContext(),"Sorry, your feedback cannot recorded!",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginClass> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getRating(){
        float rating = ratingBar.getRating();
        if(rating>=0 && rating<1){
            RATINGS = 1;
        }else if(rating>=1 && rating<2){
            RATINGS = 2;
        }else if(rating>=2 && rating<3){
            RATINGS = 3;
        }else if(rating>=3 && rating<4){
            RATINGS = 4;
        }else if(rating>=4 && rating<=5){
            RATINGS = 5;
        }
        return RATINGS;
    }

    private void stringToImage(String s) {
        if(s!=null){
            byte[] imgByte = Base64.decode(s,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
            profilePic.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_profile:
                Intent p = new Intent(Feedback.this, Profile.class);
                startActivity(p);
                finish();
                break;
            case R.id.nav_hostel:
                Intent h = new Intent(Feedback.this, Hostel.class);
                startActivity(h);
                finish();
                break;
            case R.id.nav_college:
                Intent c = new Intent(Feedback.this,College.class);
                startActivity(c);
                finish();
                break;
            case R.id.nav_foods:
                Intent d = new Intent(Feedback.this,Foods.class);
                startActivity(d);
                finish();
                break;
            case R.id.nav_feedback:
                break;
            case R.id.nav_share:
                Intent s = new Intent(Intent.ACTION_SEND);
                s.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out our app at: https://makemyplace.000webhostapp.com/download.php");
                s.setType("text/plain");
                startActivity(s);
                break;
            case R.id.nav_about:
                Intent a = new Intent(Feedback.this,About.class);
                startActivity(a);
                finish();
                break;
            case R.id.nav_logout:
                logoutDialog = new Dialog(Feedback.this,R.style.DialogNoActionBar);
                logoutDialog.setContentView(R.layout.dialog_logout);
                ImageView close;
                Button yes;
                close = (ImageView)logoutDialog.findViewById(R.id.ivCancel);
                yes = (Button)logoutDialog.findViewById(R.id.btnYes);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logoutDialog.dismiss();
                        sessionManager.logoutUser();
                        //LoginManager.getInstance().logOut();
                        //startActivity(new Intent(Feedback.this,Login.class));
                        finish();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logoutDialog.dismiss();
                    }
                });
                logoutDialog.show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
