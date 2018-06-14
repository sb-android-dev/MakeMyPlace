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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.project.smit.makemyplace.MakeMyPlace.ConnectionDetector;
import com.project.smit.makemyplace.MakeMyPlace.UserSessionManager;
import com.project.smit.makemyplace.R;

import java.util.HashMap;

public class Profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView fname,name,lname,email,mobile,username;
    RadioGroup gender;
    RadioButton male,female;
    ImageView profile,profilePic;
    Dialog logoutDialog;
    ConnectionDetector detector;
    UserSessionManager sessionManager;
    HashMap<String,String> map;
    String uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        detector = new ConnectionDetector(Profile.this);
        sessionManager = new UserSessionManager(Profile.this);
        map = sessionManager.getUserDetails();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_profile).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        username = (TextView)header.findViewById(R.id.tvUsername);
        profilePic = (ImageView)header.findViewById(R.id.imageView);

        fname = (TextView)findViewById(R.id.tvFirstname);
        name = (TextView)findViewById(R.id.tvName);
        lname = (TextView)findViewById(R.id.tvSurname);
        email = (TextView)findViewById(R.id.tvEmail);
        mobile = (TextView)findViewById(R.id.tvMobile);
        gender = (RadioGroup)findViewById(R.id.rgGender);
        male = (RadioButton)findViewById(R.id.rbMale);
        female = (RadioButton)findViewById(R.id.rbFemale);
        profile = (ImageView)findViewById(R.id.ivImage);

        if(map.get(UserSessionManager.KEY_Fname)!=null && map.get(UserSessionManager.KEY_Lname)!=null){
            uname = map.get(UserSessionManager.KEY_Fname)+" "+map.get(UserSessionManager.KEY_Lname);
        }else if(map.get(UserSessionManager.KEY_Fname)!=null && map.get(UserSessionManager.KEY_Lname)==null){
            uname = map.get(UserSessionManager.KEY_Fname);
        }

        username.setText(uname);
        stringToImage(map.get(UserSessionManager.KEY_Image));

        if(map.get(UserSessionManager.KEY_Fname)!=null){
            name.setText(map.get(UserSessionManager.KEY_Fname));
        }else{
            name.setText("-");
        }
        if(map.get(UserSessionManager.KEY_Lname)!=null) {
            lname.setText(map.get(UserSessionManager.KEY_Lname));
        }else{
            fname.setText(R.string.name);
            lname.setText("-");
        }
        if(map.get(UserSessionManager.KEY_email)!=null) {
            email.setText(map.get(UserSessionManager.KEY_email));
        }else{
            email.setText("-");
        }
        if(map.get(UserSessionManager.KEY_Mobile)!=null) {
            mobile.setText(map.get(UserSessionManager.KEY_Mobile));
        }else{
            mobile.setText("-");
        }

        switch (map.get(UserSessionManager.KEY_Gender)){
            case "Male":
                male.setChecked(true);
                female.setEnabled(false);
                break;
            case "male":
                male.setChecked(true);
                female.setEnabled(false);
                break;
            case "Female":
                female.setChecked(true);
                male.setEnabled(false);
                break;
            case "female":
                female.setChecked(true);
                male.setEnabled(false);
                break;
        }
    }

    public void stringToImage(String s) {
        if(s!=null){
            byte[] imgByte = Base64.decode(s,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
            profilePic.setImageBitmap(bitmap);
            profile.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(Profile.this,MainActivity.class));
            finish();
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_change) {
            if(map.get(UserSessionManager.KEY_pwd)!=null) {
                startActivity(new Intent(Profile.this, ChangePassword.class));
                return true;
            }else{
                Toast.makeText(Profile.this,"You cannot change your password because you are login with facebook!",
                        Toast.LENGTH_LONG).show();
            }
        }
        if(id==R.id.action_edit){
            if(map.get(UserSessionManager.KEY_Lname)!=null){
                Intent intent = new Intent(Profile.this,Edit.class);
                startActivityForResult(intent,12);
                return true;
            }else{
                Toast.makeText(Profile.this,"You cannot edit your data because you are login with facebook!",
                        Toast.LENGTH_LONG).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refresh1 = new Intent(this, Profile.class);
            startActivity(refresh1);
            this.finish();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_profile:
                break;
            case R.id.nav_hostel:
                Intent h = new Intent(Profile.this, Hostel.class);
                startActivity(h);
                finish();
                break;
            case R.id.nav_college:
                Intent c = new Intent(Profile.this,College.class);
                startActivity(c);
                finish();
                break;
            case R.id.nav_foods:
                Intent d = new Intent(Profile.this,Foods.class);
                startActivity(d);
                finish();
                break;
            case R.id.nav_feedback:
                Intent f = new Intent(Profile.this,Feedback.class);
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
                Intent a = new Intent(Profile.this,About.class);
                startActivity(a);
                finish();
                break;
            case R.id.nav_logout:
                logoutDialog = new Dialog(Profile.this,R.style.DialogNoActionBar);
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
                        //startActivity(new Intent(Profile.this,Login.class));
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
