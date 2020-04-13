package com.example.loginwithfacebook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    EditText editTextemail,editTextten,editTextfirname;
    Button Dangxuat;
    LoginButton loginButton;
    Button buttonchucnang;
    ProfilePictureView profilePictureView;
    CallbackManager callbackManager;
    String email,fritname,ten;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager=CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        anhxa();
        buttonchucnang.setVisibility(View.INVISIBLE);
        Dangxuat.setVisibility(View.INVISIBLE);
        editTextten.setVisibility(View.INVISIBLE);
        editTextemail.setVisibility(View.INVISIBLE);
        editTextfirname.setVisibility(View.INVISIBLE);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        setLoginButton();
        dangxuat();
        chuyenmanhinh();
    }

    private void chuyenmanhinh() {
        buttonchucnang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ChucnangActivity.class);
                startActivity(intent);

            }
        });
    }

    private void dangxuat() {
        Dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                loginButton.setVisibility(View.VISIBLE);
                buttonchucnang.setVisibility(View.INVISIBLE);
                Dangxuat.setVisibility(View.INVISIBLE);
                editTextten.setVisibility(View.INVISIBLE);
                editTextemail.setVisibility(View.INVISIBLE);
                editTextfirname.setVisibility(View.INVISIBLE);
                profilePictureView.setProfileId(null);
                email="";
                ten="";
                fritname="";
            }
        });
    }

    private void setLoginButton() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(View.INVISIBLE);
                buttonchucnang.setVisibility(View.VISIBLE);
                Dangxuat.setVisibility(View.VISIBLE);
                editTextten.setVisibility(View.VISIBLE);
                editTextemail.setVisibility(View.VISIBLE);
                editTextfirname.setVisibility(View.VISIBLE);
                result();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void result() {
        GraphRequest graphRequest=GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("JSON ",response.getJSONObject().toString());
                try {
                    email=object.getString("email");
                    ten=object.getString("name");
                    fritname=object.getString("first_name");
                    profilePictureView.setProfileId(Profile.getCurrentProfile().getId());
                    editTextemail.setText(email);
                    editTextten.setText(ten);
                    editTextfirname.setText(fritname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle bundle=new Bundle();
        bundle.putString("fields","name,email,first_name");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
    }

    private void anhxa() {
        loginButton=findViewById(R.id.login_button);
       buttonchucnang =findViewById(R.id.bottomchucnang);
        Dangxuat=findViewById(R.id.bottomdangnhap);
        editTextemail=findViewById(R.id.edittextemail);
        editTextfirname=findViewById(R.id.edittextfirtname);
        editTextten=findViewById(R.id.edittext);
        profilePictureView=findViewById(R.id.friendProfilePicture);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onStart() {
        LoginManager.getInstance().logOut();
        super.onStart();
    }
}
