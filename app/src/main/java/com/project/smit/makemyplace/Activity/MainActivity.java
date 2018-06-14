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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.project.smit.makemyplace.Adapter.GridViewAdapter;
import com.project.smit.makemyplace.MakeMyPlace.ConnectionDetector;
import com.project.smit.makemyplace.MakeMyPlace.UserSessionManager;
import com.project.smit.makemyplace.R;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GridView gvMenu;
    String uname;
    String[] name;
    int[] image= {R.drawable.ic_man_user_white,R.drawable.ic_college_white,R.drawable.ic_hostel_white,R.drawable.ic_foods_white,
            R.drawable.ic_feedback_white,R.drawable.ic_about_white};
    TextView username;
    ImageView profilePic;
    Bitmap bitmap;
    Dialog logoutDialog;
    NavigationView navigationView;
    ConnectionDetector detector;
    UserSessionManager sessionManager;
    HashMap<String,String> map;

    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        detector = new ConnectionDetector(MainActivity.this);
        sessionManager = new UserSessionManager(MainActivity.this);
        map = sessionManager.getUserDetails();

        if(map.get(UserSessionManager.KEY_Fname)==null){
            Intent intent = new Intent(MainActivity.this,Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView)findViewById(R.id.nav_view);
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

        MobileAds.initialize(this, "ca-app-pub-6358193704780784~1799129340");

        mAdView = (AdView)findViewById(R.id.adView);
        name=getResources().getStringArray(R.array.nav_list_item_name);
        gvMenu = (GridView)findViewById(R.id.gvMenu);
        GridViewAdapter adapter = new GridViewAdapter(this,name,image);
        gvMenu.setAdapter(adapter);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        gvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent p = new Intent(MainActivity.this,Profile.class);
                        startActivity(p);
                        finish();
                        break;
                    case 2:
                        Intent h = new Intent(MainActivity.this,Hostel.class);
                        startActivity(h);
                        break;
                    case 1:
                        Intent c = new Intent(MainActivity.this,College.class);
                        startActivity(c);
                        break;
                    case 3:
                        Intent d = new Intent(MainActivity.this,Foods.class);
                        startActivity(d);
                        break;
                    case 4:
                        Intent f = new Intent(MainActivity.this,Feedback.class);
                        startActivity(f);
                        break;
                    case 5:
                        Intent a = new Intent(MainActivity.this,About.class);
                        startActivity(a);
                        break;
                }
            }
        });
    }

    private void stringToImage(String s) {
        if(s!=null){
            byte[] imgByte = Base64.decode(s,Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
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
                Intent p = new Intent(MainActivity.this, Profile.class);
                startActivity(p);
                finish();
                break;
            case R.id.nav_hostel:
                Intent h = new Intent(MainActivity.this, Hostel.class);
                startActivity(h);
                break;
            case R.id.nav_college:
                Intent c = new Intent(MainActivity.this,College.class);
                startActivity(c);
                break;
            case R.id.nav_foods:
                Intent d = new Intent(MainActivity.this,Foods.class);
                startActivity(d);
                break;
            case R.id.nav_feedback:
                Intent f = new Intent(MainActivity.this,Feedback.class);
                startActivity(f);
                break;
            case R.id.nav_share:
                Intent s = new Intent(Intent.ACTION_SEND);
                s.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out our app at: https://makemyplace.000webhostapp.com/download.php");
                s.setType("text/plain");
                startActivity(s);
                break;
            case R.id.nav_about:
                Intent a = new Intent(MainActivity.this,About.class);
                startActivity(a);
                break;
            case R.id.nav_logout:
                logoutDialog = new Dialog(MainActivity.this,R.style.DialogNoActionBar);
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
