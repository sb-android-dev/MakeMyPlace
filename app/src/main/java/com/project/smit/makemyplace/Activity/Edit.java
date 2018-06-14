package com.project.smit.makemyplace.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.project.smit.makemyplace.Bean.RegisterClass;
import com.project.smit.makemyplace.MakeMyPlace.ConnectionDetector;
import com.project.smit.makemyplace.MakeMyPlace.UserSessionManager;
import com.project.smit.makemyplace.R;
import com.project.smit.makemyplace.WebService.ApiClient;
import com.project.smit.makemyplace.WebService.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class Edit extends AppCompatActivity {

    ImageView profilePic;
    RelativeLayout editProfile;
    private static final int CAMERA_LOAD_IMAGE = 1888,GALLERY_LOAD_IMAGE=1,RESULT_CROP = 400;
    EditText name,surname,email,mobile;
    RadioGroup genderGroup;
    RadioButton male, female;
    String fname,lname,emailId,mobileNo,gender,img;
    Bitmap bitmap;
    File file;
    Uri path;
    Dialog selectDialog;

    ConnectionDetector detector;
    UserSessionManager sessionManager;
    HashMap<String,String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            e.printStackTrace();
        }
        editProfile = (RelativeLayout)findViewById(R.id.rlImage);
        profilePic =(ImageView)findViewById(R.id.ivImage);
        name = (EditText)findViewById(R.id.etName);
        surname = (EditText)findViewById(R.id.etSurname);
        email = (EditText)findViewById(R.id.etEmail);
        mobile = (EditText)findViewById(R.id.etMobile);
        genderGroup = (RadioGroup)findViewById(R.id.rgGender);
        male = (RadioButton)findViewById(R.id.rbMale);
        female = (RadioButton)findViewById(R.id.rbFemale);

        detector = new ConnectionDetector(Edit.this);
        sessionManager = new UserSessionManager(Edit.this);
        map = sessionManager.getUserDetails();

        name.setHint(map.get(UserSessionManager.KEY_Fname));
        surname.setHint(map.get(UserSessionManager.KEY_Lname));
        email.setHint(map.get(UserSessionManager.KEY_email));
        mobile.setHint(map.get(UserSessionManager.KEY_Mobile));
        stringToImage(map.get(UserSessionManager.KEY_Image));

        switch(map.get(UserSessionManager.KEY_Gender)){
            case "Male":
                male.setChecked(true);
                gender="Male";
                break;
            case "Female":
                female.setChecked(true);
                gender="Female";
                break;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(Edit.this, Manifest.permission.CAMERA);
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            RequestRuntimePermission();
        }

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
                    case R.id.rbMale:
                        gender = "Male";
                        break;
                    case R.id.rbFemale:
                        gender = "Female";
                        break;
                }
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog = new Dialog(Edit.this,R.style.DialogNoActionBar);
                selectDialog.setContentView(R.layout.dialog_profile);
                ImageView close;
                LinearLayout gallery,camera;
                gallery=(LinearLayout)selectDialog.findViewById(R.id.llGallery);
                camera=(LinearLayout)selectDialog.findViewById(R.id.llCamera);
                close = (ImageView)selectDialog.findViewById(R.id.ivCancel);
                selectDialog.show();

                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, GALLERY_LOAD_IMAGE);

                        selectDialog.dismiss();
                    }
                });
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        file = new File(Environment.getExternalStorageDirectory(),"file"+String.valueOf(System.currentTimeMillis())
                                +".jpg");
                        path = Uri.fromFile(file);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,path);
                        intent.putExtra("return-data",true);
                        startActivityForResult(intent, CAMERA_LOAD_IMAGE);

                        selectDialog.dismiss();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectDialog.dismiss();
                    }
                });
            }
        });
    }

    private void RequestRuntimePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(Edit.this,Manifest.permission.CAMERA)){
            Toast.makeText(this,"CAMERA permission allows us to use CAMERA App",Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(Edit.this,new String[]{Manifest.permission.CAMERA},100);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.action_save){
            conditionCheck();
            doEdit();
        }
        else if(id==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void conditionCheck() {
        if(name.getText().toString().equals("")){
            fname = map.get(UserSessionManager.KEY_Fname);
        }else{
            fname = name.getText().toString();
        }
        if(surname.getText().toString().equals("")){
            lname = map.get(UserSessionManager.KEY_Lname);
        }else{
            lname = surname.getText().toString();
        }
        if(email.getText().toString().equals("")){
            emailId = map.get(UserSessionManager.KEY_email);
        }else{
            emailId = email.getText().toString();
        }
        if(mobile.getText().toString().equals("")){
            mobileNo = map.get(UserSessionManager.KEY_Mobile);
        }else{
            mobileNo = mobile.getText().toString();
        }
    }

    private void doEdit() {
        img = imageToString();

        final ProgressDialog progressDialog = new ProgressDialog(Edit.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<RegisterClass> call = apiInterface.editUser(map.get(UserSessionManager.KEY_userId),fname,lname,emailId,
                gender,mobileNo,img);
        call.enqueue(new Callback<RegisterClass>() {
            @Override
            public void onResponse(Call<RegisterClass> call, retrofit2.Response<RegisterClass> response) {
                RegisterClass registerClass = response.body();
                if (registerClass != null) {
                    String data_code = registerClass.getResponse().getData_code();
                    progressDialog.dismiss();
                    if(data_code.equals("2001")){
                        sessionManager.logoutUser1();
                        sessionManager.createUserLoginSession(map.get(UserSessionManager.KEY_userId),fname,lname,
                                emailId,map.get(UserSessionManager.KEY_pwd),gender,mobileNo,img);

                        Toast.makeText(getApplicationContext(),"Your data is edited",Toast.LENGTH_SHORT).show();
                    }else if(data_code.equals("2002")){
                        Toast.makeText(getApplicationContext(),"Update Failed! Please try again later",Toast.LENGTH_SHORT).show();
                    }
                    setResult(RESULT_OK, null);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<RegisterClass> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte,Base64.DEFAULT);
    }

    public void stringToImage(String s) {
        if(s!=null){
            byte[] imgByte = Base64.decode(s,Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
            profilePic.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_LOAD_IMAGE && resultCode== RESULT_OK && data!=null){
            path = data.getData();
            performCrop();

        }
        if(requestCode==CAMERA_LOAD_IMAGE && resultCode== Activity.RESULT_OK){
//            path = data.getData();
            performCrop();
        }
        if (requestCode == RESULT_CROP && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                bitmap = extras.getParcelable("data");
            }
            // Set The Bitmap Data To ImageView
            profilePic.setImageBitmap(bitmap);
            profilePic.setScaleType(ImageView.ScaleType.FIT_XY);

        }

    }

    private void performCrop() {
        try {
            //Start Crop Activity
            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            // indicate profilePic type and Uri
            cropIntent.setDataAndType(path, "image/*");

            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 400);
            cropIntent.putExtra("outputY", 400);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
