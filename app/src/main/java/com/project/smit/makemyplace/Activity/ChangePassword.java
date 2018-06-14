package com.project.smit.makemyplace.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangePassword extends AppCompatActivity {

    EditText newpwd,current,confirm;
    Button change;
    ConnectionDetector detector;
    UserSessionManager manager;
    HashMap<String,String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            e.printStackTrace();
        }

        detector = new ConnectionDetector(ChangePassword.this);
        manager = new UserSessionManager(ChangePassword.this);
        map = manager.getUserDetails();

        current = (EditText)findViewById(R.id.etCurrent);
        newpwd = (EditText)findViewById(R.id.etNew);
        confirm = (EditText)findViewById(R.id.etConfirm);
        change = (Button)findViewById(R.id.btnChange);

        current.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    if(event.getRawX() >= (current.getRight() - current.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        current.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                        current.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_hide,0);
                        return true;
                    }
                }
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX() >= (current.getRight() - current.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        current.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        current.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_view,0);
                        return true;
                    }
                }
                return false;
            }
        });
        newpwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    if(event.getRawX() >= (newpwd.getRight() - newpwd.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        newpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                        newpwd.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_hide,0);
                        return true;
                    }
                }
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX() >= (newpwd.getRight() - newpwd.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        newpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        newpwd.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_view,0);
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

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getText().toString().length()==0){
                    current.setError("Username cannot be empty!");
                }else if(newpwd.getText().toString().length()==0){
                    newpwd.setError("Password cannot be empty!");
                }else if(confirm.getText().toString().length()==0){
                    confirm.setError("Password cannot be empty!");
                }else if(current.getText().toString().length()<=5){
                    current.setError("Current Password should contain atleast 6 character");
                } else if(newpwd.getText().toString().length()<=5){
                    newpwd.setError("New Password should contain atleast 6 character");
                }else if(!confirm.getText().toString().contentEquals(newpwd.getText().toString())){
                    confirm.setError("Confirm password cannot be match, Please retype!");
                }else{
                    if(detector.isConnectingToInternet()){
                        changePassword();
                    }else{
                        Toast.makeText(ChangePassword.this,"Please connect to Internet",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void changePassword() {
        final ProgressDialog progressDialog = new ProgressDialog(ChangePassword.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Log.e("id",map.get(UserSessionManager.KEY_userId));
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LoginClass> call = apiInterface.changePassword(map.get(UserSessionManager.KEY_userId).toString(),current.getText().toString(),
                newpwd.getText().toString());
        call.enqueue(new Callback<LoginClass>() {
            @Override
            public void onResponse(Call<LoginClass> call, retrofit2.Response<LoginClass> response) {
                LoginClass loginClass = response.body();
                String data_code = loginClass.getResponse().getData_code();
                Log.e("data_code:",data_code);
                progressDialog.dismiss();
                if(data_code.equals("2001")){
                    manager.logoutUser1();
                    manager.createUserLoginSession(map.get(UserSessionManager.KEY_userId),map.get(UserSessionManager.KEY_Fname),
                            map.get(UserSessionManager.KEY_Lname),map.get(UserSessionManager.KEY_email),
                            newpwd.getText().toString(),map.get(UserSessionManager.KEY_Gender),map.get(UserSessionManager.KEY_Mobile),
                            map.get(UserSessionManager.KEY_Image));
                    Toast.makeText(getApplicationContext(),"Your password is successfully changed.",Toast.LENGTH_SHORT).show();
                    NavUtils.navigateUpFromSameTask(ChangePassword.this);
                    finish();
                }else if(data_code.equals("2002")){
                    Toast.makeText(getApplicationContext(),"Failed to update password",Toast.LENGTH_SHORT).show();
                }else if(data_code.equals("2000")){
                    current.setError("Your current password is wrong!");
                }
            }

            @Override
            public void onFailure(Call<LoginClass> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Failed! Please try later",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            NavUtils.navigateUpTo(this,getIntent());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
