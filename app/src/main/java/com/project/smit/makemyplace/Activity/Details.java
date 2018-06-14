package com.project.smit.makemyplace.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.smit.makemyplace.R;

public class Details extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    TextView name,phone,address;
    ImageView image;
    String img,nm,cat;
    String lat,lng;

    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getIntent().getStringExtra("name"));
        setContentView(R.layout.activity_details);
        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            e.printStackTrace();
        }

        mAdView = (AdView)findViewById(R.id.adView);
        name = (TextView)findViewById(R.id.tvName);
        phone = (TextView)findViewById(R.id.tvPhone);
        address = (TextView)findViewById(R.id.tvAddress);
        image = (ImageView)findViewById(R.id.ivImage);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frMap);
        mapFragment.getMapAsync(this);

        img=getIntent().getStringExtra("image");
        byte[] imgByte = Base64.decode(img,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),bitmap);

        //image.setImageBitmap(bitmap);
        image.setBackground(bitmapDrawable);
        nm=getIntent().getStringExtra("name");
        name.setText(nm);
        phone.setText(getIntent().getStringExtra("phone"));
        address.setText(getIntent().getStringExtra("address"));
        lat = getIntent().getStringExtra("latitude");
        lng = getIntent().getStringExtra("longitude");
        cat = getIntent().getStringExtra("cat");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(this, getIntent());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        LatLng nityam = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

        switch(cat){
            case "college":
                mMap.addMarker(new MarkerOptions().title(nm).position(nityam).icon(BitmapDescriptorFactory.fromResource(R.drawable.college_marker)));
                break;
            case "hostel":
                mMap.addMarker(new MarkerOptions().title(nm).position(nityam).icon(BitmapDescriptorFactory.fromResource(R.drawable.hostel_marker)));
                break;
            case "foods":
                mMap.addMarker(new MarkerOptions().title(nm).position(nityam).icon(BitmapDescriptorFactory.fromResource(R.drawable.food_marker)));
                break;
        }
//        if(cat.equals("college")){
//
//        } else if(cat.equals("hostel")){
//
//        } else if(cat.equals("foods")){
//        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nityam, 15));
    }
}
