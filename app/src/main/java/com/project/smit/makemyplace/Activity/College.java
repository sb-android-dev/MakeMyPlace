package com.project.smit.makemyplace.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.smit.makemyplace.Adapter.RecyclerViewAdapter;
import com.project.smit.makemyplace.Bean.RetrieveClass;
import com.project.smit.makemyplace.MakeMyPlace.ConnectionDetector;
import com.project.smit.makemyplace.MakeMyPlace.UserSessionManager;
import com.project.smit.makemyplace.Parsing.Response;
import com.project.smit.makemyplace.R;
import com.project.smit.makemyplace.WebService.ApiClient;
import com.project.smit.makemyplace.WebService.ApiInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class College extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Response> responseList;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recycleAdapter;
    AppBarLayout appBarLayout;
    SwipeRefreshLayout srlCollege;
    TextView username;
    ImageView profilePic;
    Dialog logoutDialog;
    ConnectionDetector detector;
    UserSessionManager sessionManager;
    HashMap<String,String> map;
    String uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout)findViewById(R.id.appBarLayout);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_college).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        username = (TextView)header.findViewById(R.id.tvUsername);
        profilePic = (ImageView)header.findViewById(R.id.imageView);

        detector = new ConnectionDetector(College.this);
        sessionManager = new UserSessionManager(College.this);
        map = sessionManager.getUserDetails();

        if(map.get(UserSessionManager.KEY_Fname)!=null && map.get(UserSessionManager.KEY_Lname)!=null){
            uname = map.get(UserSessionManager.KEY_Fname)+" "+map.get(UserSessionManager.KEY_Lname);
        }else if(map.get(UserSessionManager.KEY_Fname)!=null && map.get(UserSessionManager.KEY_Lname)==null){
            uname = map.get(UserSessionManager.KEY_Fname);
        }

        username.setText(uname);
        stringToImage(map.get(UserSessionManager.KEY_Image));

        srlCollege = (SwipeRefreshLayout)findViewById(R.id.srlCollege);
        srlCollege.setColorSchemeResources(R.color.colorPrimary);

        srlCollege.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(detector.isConnectingToInternet()){
                    getData();
                }else{
                    srlCollege.setRefreshing(false);
                    Toast.makeText(getApplicationContext(),"Please connect with Internet!",Toast.LENGTH_LONG).show();
                }
            }
        });

        initViews();
    }

    private void initViews(){
        recyclerView = (RecyclerView)findViewById(R.id.rvCollege);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        if(detector.isConnectingToInternet()){
            getData();
        }else{
            Toast.makeText(getApplicationContext(),"Please connect with Internet!",Toast.LENGTH_LONG).show();
        }
    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(College.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        srlCollege.setRefreshing(false);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<RetrieveClass> call = apiInterface.retrieveImage("college");
        Log.e("return from","API");
        call.enqueue(new Callback<RetrieveClass>() {
            @Override
            public void onResponse(Call<RetrieveClass> call, retrofit2.Response<RetrieveClass> response) {
                RetrieveClass retrieveClass = response.body();
                responseList = new ArrayList<>(Arrays.asList(retrieveClass.getResponse()));
                recycleAdapter = new RecyclerViewAdapter(responseList,College.this);
                recyclerView.setAdapter(recycleAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<RetrieveClass> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Failed! Check Connection",Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.college, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Response> newList = new ArrayList<>();
                for(Response response : responseList){
                    String name = response.getName().toLowerCase();
                    if(name.contains(newText)){
                        newList.add(response);
                    }
                }
                recycleAdapter.setFilter(newList);
                if(newList.isEmpty()){
                    Toast.makeText(College.this,"Sorry no data found!",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        appBarLayout.setExpanded(false);
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_profile:
                Intent p = new Intent(College.this, Profile.class);
                startActivity(p);
                finish();
                break;
            case R.id.nav_hostel:
                Intent h = new Intent(College.this, Hostel.class);
                startActivity(h);
                finish();
                break;
            case R.id.nav_college:
                break;
            case R.id.nav_foods:
                Intent d = new Intent(College.this,Foods.class);
                startActivity(d);
                finish();
                break;
            case R.id.nav_feedback:
                Intent f = new Intent(College.this,Feedback.class);
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
                Intent a = new Intent(College.this,About.class);
                startActivity(a);
                finish();
                break;
            case R.id.nav_logout:
                logoutDialog = new Dialog(College.this,R.style.DialogNoActionBar);
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
                        //startActivity(new Intent(College.this,Login.class));
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

//    private class MyTimerTask extends TimerTask {
//        @Override
//        public void run() {
//            College.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if(viewPager.getCurrentItem()==0) {
//                        viewPager.setCurrentItem(1);
//                    }else if(viewPager.getCurrentItem()==1) {
//                        viewPager.setCurrentItem(2);
//                    }else if(viewPager.getCurrentItem()==2) {
//                        viewPager.setCurrentItem(0);
//                    }
//                }
//            });
//        }
//    }
}
