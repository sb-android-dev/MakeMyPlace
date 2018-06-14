package com.project.smit.makemyplace.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.project.smit.makemyplace.Bean.LoginClass;
import com.project.smit.makemyplace.MakeMyPlace.ConnectionDetector;
import com.project.smit.makemyplace.MakeMyPlace.UserSessionManager;
import com.project.smit.makemyplace.R;
import com.project.smit.makemyplace.WebService.ApiClient;
import com.project.smit.makemyplace.WebService.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class Login extends AppCompatActivity implements View.OnClickListener{

    EditText username, password;
    Button login,gLogin,fLogin;
    TextView forget, register;
    ConnectionDetector detector;
    UserSessionManager sessionManager;
    HashMap<String,String> map;
    CallbackManager callbackManager;
    Bundle bFacebookData;
    Bitmap bitmap;

    Dialog forgotDialog;
    EditText email;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        username=(EditText)findViewById(R.id.etUsername);
        password=(EditText)findViewById(R.id.etPassword);
        login=(Button)findViewById(R.id.btnLogin);
        forget=(TextView)findViewById(R.id.tvForget);
        register=(TextView)findViewById(R.id.tvRegister);
        gLogin = (Button)findViewById(R.id.btnGLogin);
        fLogin = (Button)findViewById(R.id.btnFLogin);

        detector = new ConnectionDetector(Login.this);
        sessionManager = new UserSessionManager(Login.this);
        map = sessionManager.getUserDetails();

        callbackManager = CallbackManager.Factory.create();

//        printKeyHash(this);

        if(map.get(UserSessionManager.KEY_Fname)!=null){
            Intent intent = new Intent(Login.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    if(event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                        password.selectAll();
                        password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_outline,0,R.drawable.ic_hide,0);
                        return true;
                    }
                }
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        password.selectAll();
                        password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_outline,0,R.drawable.ic_view,0);
                        return true;
                    }
                }
                return false;
            }
        });

        forget.setOnClickListener(this);

        login.setOnClickListener(this);

        fLogin.setOnClickListener(this);

        register.setOnClickListener(this);

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                String accessToken = loginResult.getAccessToken().getToken();
//                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login
                        bFacebookData = getFacebookData(object);
//                        Log.e("id",bFacebookData.toString());
//                        Log.e("name",bFacebookData.getString("name"));
//                        Log.e("email",bFacebookData.getString("email"));

                        new Login.DownloadImage().execute(bFacebookData.getString("profile_pic"));

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

//    private String printKeyHash(Login login) {
//        PackageInfo packageInfo;
//        String key=null;
//        String packageName = login.getApplicationContext().getPackageName();
//        try {
//            packageInfo = login.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
//            Log.e("Package Name= ", login.getApplicationContext().getPackageName());
//
//            for(Signature signature:packageInfo.signatures){
//                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
//                messageDigest.update(signature.toByteArray());
//                key = new String(Base64.encode(messageDigest.digest(),0));
//
//                Log.e("Key = ",key);
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return key;
//    }

    private void getLogin() {
        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LoginClass> call = apiInterface.loginUser(username.getText().toString(),password.getText().toString());
        call.enqueue(new Callback<LoginClass>() {
            @Override
            public void onResponse(Call<LoginClass> call, retrofit2.Response<LoginClass> response) {
                LoginClass loginClass = response.body();
                if (loginClass != null) {
                    String data_code = loginClass.getResponse().getData_code();
                    progressDialog.dismiss();
                    if(data_code.equals("2001")){
                        String id = loginClass.getResponse().getId();
                        String fname = loginClass.getResponse().getFname();
                        String lname = loginClass.getResponse().getLname();
                        String uname = loginClass.getResponse().getEmail();
                        String pword = loginClass.getResponse().getPassword();
                        String gender = loginClass.getResponse().getGender();
                        String mobile = loginClass.getResponse().getMobile();
                        String image = loginClass.getResponse().getImage();
                        sessionManager.createUserLoginSession(id,fname,lname,uname,pword,gender,mobile,image);
                        startActivity(new Intent(Login.this,MainActivity.class));
                        finish();
                    }else if(data_code.equals("2002")){
                        Toast.makeText(getApplicationContext(),"No username found! Make sure you have registered",Toast.LENGTH_SHORT).show();
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

    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=400&height=400");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("name"))
                bundle.putString("name", object.getString("name"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            /*if (object.has("email"))
                bundle.putString("email", object.getString("email"));*/

            return bundle;
        }
        catch(JSONException e) {
            Log.d("TAG","Error parsing JSON");
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvForget:
                forgotDialog = new Dialog(Login.this,R.style.DialogNoActionBar);
                forgotDialog.setContentView(R.layout.dialog_forgot);
                ImageView close;
                email = (EditText)forgotDialog.findViewById(R.id.etForget);
//                captcha = (EditText)forgotDialog.findViewById(R.id.etCaptcha);
                send = (Button)forgotDialog.findViewById(R.id.btnSend);
//                captchaCode = (ImageView)forgotDialog.findViewById(R.id.ivCaptcha);
                close = (ImageView)forgotDialog.findViewById(R.id.ivCancel);
//                resetCaptcha();

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(email.getText().toString().equals("")){
                            email.setError("E-mail cannot be empty!");
                        }else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                            email.setError("Invalid E-mail address!");
                        }
//                        else if(captcha.getText().toString().equals("")){
//                            captcha.setError("Captcha Code cannot be empty!");
//                        }else if(!textCaptcha.checkAnswer(captcha.getText().toString().trim())){
//                            captcha.setError("Captcha Code is not match!");
//                        }
                        else{
                            if(detector.isConnectingToInternet()){
                                sendPassword();
                            }else{
                                Toast.makeText(Login.this,"Please connect to Internet",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
//                captcha.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        final int DRAWABLE_RIGHT = 2;
//
//                        if(event.getAction()==MotionEvent.ACTION_DOWN){
//                            if(event.getRawX() >= (captcha.getRight() - captcha.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                                resetCaptcha();
//                                captcha.selectAll();
//                                captcha.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_refresh,0);
//                                return true;
//                            }
//                        }
//                        return false;
//                    }
//                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        forgotDialog.dismiss();
                    }
                });
                forgotDialog.show();
                break;

            case R.id.btnLogin:
                if(username.getText().toString().length()==0){
                    username.setError("Username cannot be empty!");
                }else if(password.getText().toString().length()==0){
                    password.setError("Password cannot be empty!");
                }else if(!Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches()){
                    username.setError("Invalid Email address!");
                }else if(password.getText().toString().length()<=5){
                    password.setError("Password should contain atleast 6 character");
                }else{
                    if(detector.isConnectingToInternet()){
                        getLogin();
                    }else{
                        Toast.makeText(Login.this,"Please connect to Internet",Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.btnFLogin:
                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("email"));
                break;

            case R.id.tvRegister:
                Intent intent = new Intent(Login.this,Registration.class);
                startActivity(intent);
                break;
        }
    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        final ProgressDialog progressDialog = new ProgressDialog(Login.this);

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        protected Bitmap doInBackground(String... urls){
            String urldisplay = urls[0];

            try{
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            }catch (Exception e){
                Log.e("Error...", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result){
            String image = imageToString(result);
            sessionManager.createUserLoginSession(null,bFacebookData.getString("name"),null,null,null,
                    bFacebookData.getString("gender"),null,image);
            progressDialog.dismiss();
            startActivity(new Intent(Login.this,MainActivity.class));
            finish();
        }
    }

    private String imageToString(Bitmap myBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte,Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void sendPassword() {
        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LoginClass> call = apiInterface.sendPassword(email.getText().toString());
        call.enqueue(new Callback<LoginClass>() {
            @Override
            public void onResponse(Call<LoginClass> call, retrofit2.Response<LoginClass> response) {
                LoginClass loginClass = response.body();
                if (loginClass != null) {
                   String data_code = loginClass.getResponse().getData_code();
                    progressDialog.dismiss();
                    if(data_code.equals("2001")){
                        Toast.makeText(Login.this,"Your Password is sent to your E-mail id",Toast.LENGTH_LONG).show();
                        forgotDialog.dismiss();
                    }else if(data_code.equals("2000")){
                        Toast.makeText(getApplicationContext(),"No such Email-id exist",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginClass> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Failed! Please try later",Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void resetCaptcha(){
//        textCaptcha = new TextCaptcha(250,100,4, TextCaptcha.TextOptions.UPPERCASE_ONLY);
//        captchaCode.setImageBitmap(textCaptcha.getImage());
//    }

}