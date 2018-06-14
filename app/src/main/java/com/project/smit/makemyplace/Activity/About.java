package com.project.smit.makemyplace.Activity;

import android.app.Dialog;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.smit.makemyplace.MakeMyPlace.ConnectionDetector;
import com.project.smit.makemyplace.MakeMyPlace.UserSessionManager;
import com.project.smit.makemyplace.R;

import java.util.HashMap;

public class About extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Dialog logoutDialog;
    TextView username;
    ImageView profilePic;
    ConnectionDetector detector;
    UserSessionManager sessionManager;
    HashMap<String,String> map;
    String uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        detector = new ConnectionDetector(About.this);
        sessionManager = new UserSessionManager(About.this);
        map = sessionManager.getUserDetails();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_about).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        username = (TextView)header.findViewById(R.id.tvUsername);
        profilePic = (ImageView)header.findViewById(R.id.imageView);

        if(map.get(UserSessionManager.KEY_Fname)!=null && map.get(UserSessionManager.KEY_Lname)!=null){
            uname = map.get(UserSessionManager.KEY_Fname)+" "+map.get(UserSessionManager.KEY_Lname);
        }else if(map.get(UserSessionManager.KEY_Fname)!=null && map.get(UserSessionManager.KEY_Lname)==null){
            uname = map.get(UserSessionManager.KEY_Fname);
        }

        username.setText(uname);
        stringToImage(map.get(UserSessionManager.KEY_Image));

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
                Intent p = new Intent(About.this, Profile.class);
                startActivity(p);
                finish();
                break;
            case R.id.nav_hostel:
                Intent h = new Intent(About.this, Hostel.class);
                startActivity(h);
                finish();
                break;
            case R.id.nav_college:
                Intent c = new Intent(About.this,College.class);
                startActivity(c);
                finish();
                break;
            case R.id.nav_foods:
                Intent d = new Intent(About.this,Foods.class);
                startActivity(d);
                finish();
                break;
            case R.id.nav_feedback:
                Intent f = new Intent(About.this,Feedback.class);
                startActivity(f);
                finish();
                break;
            case R.id.nav_share:
                Intent s = new Intent(Intent.ACTION_SEND);
                s.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out our app at: https://makemyplace.000webhostapp.com/download.php");
                s.setType("text/plain");
                startActivity(s);
                break;
            case R.id.nav_about:
                break;
            case R.id.nav_logout:
                logoutDialog = new Dialog(About.this,R.style.DialogNoActionBar);
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
                        //startActivity(new Intent(About.this,Login.class));
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
