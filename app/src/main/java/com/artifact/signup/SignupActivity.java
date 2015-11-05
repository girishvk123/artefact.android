package com.artifact.signup;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.artifact.R;
import com.artifact.UXUtils.CircleImageView;
import com.artifact.UXUtils.RoundImage;
import com.artifact.UXUtils.RoundedImageView;
import com.artifact.common.AFUtils;
import com.artifact.common.Const;
import com.artifact.common.UserPreferences;
import com.artifact.user.Profile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SignupActivity extends ActionBarActivity /*implements View.OnClickListener */{

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_IMAGE = 2;
    /*Rounded*//*ImageView*/ CircleImageView picImageView;
    //RoundImage roundedImage;
    private Profile userProfile;
    private EditText userNameEditText;
    private EditText userEmailIdEditText;
    private EditText userMobileEditText;
    private EditText userPasswordEditText;

    Activity activity;
    private Toolbar mToolbar;
    android.support.v7.app.ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        activity = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();
        mActionBar.hide();

        picImageView = (/*Rounded*/CircleImageView) findViewById(R.id.profil_pic);
        picImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        userNameEditText = (EditText) findViewById(R.id.name_field);
        userEmailIdEditText = (EditText) findViewById(R.id.email_field);
        userMobileEditText = (EditText) findViewById(R.id.mobile_field);
        userPasswordEditText = (EditText) findViewById(R.id.password_field);

        //userNameEditText.setOnClickListener(this);
        //userEmailIdEditText.setOnClickListener(this);
        //userMobileEditText.setOnClickListener(this);
        //userPasswordEditText.setOnClickListener(this);

        Button signup = (Button) findViewById(R.id.sign_up);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userProfile == null)
                    userProfile = new Profile();
                userProfile.setUserName(userNameEditText.getText().toString());
                userProfile.setEmailId(userEmailIdEditText.getText().toString());
                userProfile.setPhoneNumber(userMobileEditText.getText().toString());
                userProfile.setPassword(userPasswordEditText.getText().toString());
                if (userProfile != null) {
                    //new ProfileTask().execute();
                    Intent loginIntent = new Intent(activity, OTPActivity.class);
                    loginIntent.putExtra(OTPActivity.KEY_PHONE_NUMBER, userProfile.getPhoneNumber());
                    loginIntent.putExtra(OTPActivity.KEY_PASSWORD, userProfile.getPassword());
                    loginIntent.putExtra(OTPActivity.KEY_EMAIL_ID, userProfile.getEmailId());
                    loginIntent.putExtra(OTPActivity.KEY_USER_NAME, userProfile.getUserName());
                    loginIntent.putExtra(OTPActivity.KEY_PROFILE_PIC, userProfile.getProfilePicture());
                    loginIntent.putExtra(OTPActivity.KEY_FROM_SCREEN, OTPActivity.FROM_SIGNUP_SCREEN);
                    startActivity(loginIntent);
                    //finish();
                }
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = getResources().getStringArray(R.array.pic_options);

        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setTitle(getResources().getString(R.string.please_select));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (item == 1) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, getResources().getString(R.string.please_select)),
                            SELECT_IMAGE);
                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String picPath = null;
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // roundedImage = new RoundImage(thumbnail);
                // picImageView.setImageDrawable(roundedImage);
                picImageView.setImageBitmap(thumbnail);
                picPath = destination.getAbsolutePath();

            } else if (requestCode == SELECT_IMAGE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                        null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);

                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 60;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);

                //roundedImage = new RoundImage(bm);
                //picImageView.setImageDrawable(roundedImage);
                picImageView.setImageBitmap(bm);
                picPath = selectedImagePath;
            }

            if (picPath != null) {
                if (userProfile == null)
                    userProfile = new Profile();
                userProfile.setProfilePicture(picPath);
            }
        }

    }

    /*@Override
    public void onClick(View v) {

    }*/


}
