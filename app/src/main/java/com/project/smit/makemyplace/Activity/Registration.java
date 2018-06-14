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
import android.text.InputType;
import android.util.Base64;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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

public class Registration extends AppCompatActivity {

    ImageView profilePic;
    RelativeLayout profile;
    EditText fname,lname,email,password,confirm,mobile;
    RadioGroup genderRadio;
    RadioButton male,female;
    Button submit;
    Dialog selectDialog;
    private static final int CAMERA_LOAD_IMAGE = 1888,GALLERY_LOAD_IMAGE=1,RESULT_CROP = 400;
    String gender = "Male",img;
    Bitmap bitmap;
    File file;
    Uri path;

    ConnectionDetector detector;
    UserSessionManager manager;
    HashMap<String,String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            e.printStackTrace();
        }

        profilePic=(ImageView)findViewById(R.id.ivProfile);
        profile=(RelativeLayout)findViewById(R.id.rlProfile);
        fname=(EditText)findViewById(R.id.etFname);
        lname=(EditText)findViewById(R.id.etLname);
        email=(EditText)findViewById(R.id.etEmail);
        password=(EditText)findViewById(R.id.etPassword);
        confirm=(EditText)findViewById(R.id.etConfirm);
        mobile=(EditText)findViewById(R.id.etMobile);
        genderRadio=(RadioGroup)findViewById(R.id.rgGender);
        male=(RadioButton)findViewById(R.id.rbMale);
        female=(RadioButton)findViewById(R.id.rbFemale);
        submit=(Button)findViewById(R.id.btnSubmit);

        detector = new ConnectionDetector(Registration.this);
        manager = new UserSessionManager(Registration.this);
        map = manager.getUserDetails();

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sample);
        profilePic.setImageBitmap(bitmap);

        int permissionCheck = ContextCompat.checkSelfPermission(Registration.this, Manifest.permission.CAMERA);
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            RequestRuntimePermission();
        }

        genderRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fname.getText().toString().length()==0){
                    fname.setError("Firstname cannot be empty!");
                }else if(lname.getText().toString().length()==0){
                    lname.setError("Lastname cannot be empty!");
                }else if(email.getText().toString().length()==0){
                    email.setError("E-mail cannot be empty!");
                }else if(password.getText().toString().length()==0){
                    password.setError("Password cannot be empty!");
                }else if(confirm.getText().toString().length()==0){
                    confirm.setError("Confirm password cannot be empty!");
                }else if(mobile.getText().toString().length()==0){
                    mobile.setError("Mobile cannot be empty!");
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    email.setError("Invalid E-mail address!");
                }else if(password.getText().toString().length()<=5){
                    password.setError("Password should contain atleast 6 character");
                }else if(!confirm.getText().toString().contentEquals(password.getText().toString())){
                    confirm.setError("Confirm password cannot be match, Please retype!");
                }else if(mobile.getText().toString().length()<=9){
                    mobile.setError("Invalid number!");
                }else{
                    if(detector.isConnectingToInternet()){
                        getLogin();
                    }else{
                        Toast.makeText(Registration.this,"Please connect with Internet!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog = new Dialog(Registration.this,R.style.DialogNoActionBar);
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

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    if(event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                        password.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_hide,0);
                        return true;
                    }
                }
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        password.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_view,0);
                        return true;
                    }
                }
                return false;
            }
        });
        confirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    if(event.getRawX() >= (confirm.getRight() - confirm.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                        confirm.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_hide,0);
                        return true;
                    }
                }
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX() >= (confirm.getRight() - confirm.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        confirm.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_view,0);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void RequestRuntimePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(Registration.this,Manifest.permission.CAMERA)){
            Toast.makeText(this,"CAMERA permission allows us to use CAMERA App",Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(Registration.this,new String[]{Manifest.permission.CAMERA},100);
        }
    }

    private void getLogin() {
        img = imageToString();

        final ProgressDialog progressDialog = new ProgressDialog(Registration.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<RegisterClass> call = apiInterface.registerUser(fname.getText().toString(),lname.getText().toString(),email.getText().toString(),
                password.getText().toString(),gender,mobile.getText().toString(),img);
        call.enqueue(new Callback<RegisterClass>() {
            @Override
            public void onResponse(Call<RegisterClass> call, retrofit2.Response<RegisterClass> response) {
                RegisterClass registerClass = response.body();
                String data_code=registerClass.getResponse().getData_code();
                progressDialog.dismiss();
                if(data_code.equals("2000")){
                    Toast.makeText(getApplicationContext(),"This email is already Registered!",Toast.LENGTH_SHORT).show();
                }else if(data_code.equals("2001")){
                    manager.createUserLoginSession(registerClass.getResponse().getId(),fname.getText().toString(),lname.getText().toString(),
                            email.getText().toString(),password.getText().toString(),gender,mobile.getText().toString(),img);
                    startActivity(new Intent(Registration.this,MainActivity.class));
                    finish();
                    //Toast.makeText(getApplicationContext(),"This email is already Registered!",Toast.LENGTH_SHORT).show();
                }else if(data_code.equals("2002")){
                    Toast.makeText(getApplicationContext(),"Registeration Failed! Please try again later",Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
