package com.artifact.login;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.artifact.R;
import com.artifact.common.Const;
import com.artifact.common.DialogUtils;
import com.artifact.common.UserPreferences;
import com.artifact.signup.OTPActivity;
import com.artifact.signup.SignupActivity;
import com.artifact.user.Profile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoginActivity extends ActionBarActivity implements View.OnClickListener{
    Activity activity;
    String phoneNumber;
    String password;
    EditText phoneEditText;
    EditText passwordEditText;

    private Toolbar mToolbar;
    android.support.v7.app.ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();
        mActionBar .hide();
        activity = this;

        phoneEditText = (EditText)findViewById(R.id.phone_field);
        passwordEditText = (EditText)findViewById(R.id.password_field);

        //phoneEditText.setOnClickListener(this);
        //passwordEditText.setOnClickListener(this);

        LinearLayout loginLayout = (LinearLayout)findViewById(R.id.login_layout);
        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = phoneEditText.getText().toString();
                password = passwordEditText.getText().toString();
                if (!TextUtils.isEmpty(phoneNumber)) {
                    if (!TextUtils.isEmpty(password)) {
                        //new LoginTask().execute();
                        Intent loginIntent = new Intent(LoginActivity.this, OTPActivity.class);
                        loginIntent.putExtra(OTPActivity.KEY_PHONE_NUMBER, phoneNumber);
                        loginIntent.putExtra(OTPActivity.KEY_PASSWORD, password);
                        loginIntent.putExtra(OTPActivity.KEY_FROM_SCREEN, OTPActivity.FROM_LOGIN_SCREEN);
                        startActivity(loginIntent);
                        finish();
                    } else {
                        DialogUtils.showPutBackgroundAlerDialog(activity, getResources().getString(R.string.error_login_passowrd));
                    }
                } else {
                    DialogUtils.showPutBackgroundAlerDialog(activity, getResources().getString(R.string.error_login_phone));
                }

            }
        });

        TextView signupTextView = (TextView)findViewById(R.id.register);
        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
    }



}
