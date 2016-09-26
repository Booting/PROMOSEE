package com.garansimu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.garansimu.Utils.ConnectionDetector;
import com.garansimu.Utils.FontCache;
import com.garansimu.Utils.ImageFilePath;
import com.garansimu.Utils.Referensi;
import com.garansimu.Utils.callURL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ProfileEditActivity extends Activity {
    private Typeface fontUbuntuL, fontUbuntuB;
    private TextView txtTitle, lblSave, lblAddPhoto;
    private EditText txtFirstName, txtPostCode, txtEmail, txtPassword, txtConfirmPassword, txtCountry, txtCity,
            txtAddress, txtPhone, txtReferealId;
    private RadioButton radioSubscribe, radioUnsubscribe;
    private ImageView imgProfile, imgLeft;
    private String strImage;
    private String imagepathOne=null;
    private RequestQueue queue;
    private ProgressDialog pDialog;
    private HttpEntity resEntity;
    private String url="", strUserImage="", strUpdateImage="";
    private SharedPreferences garansimuPref;

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_profile_edit);

        // creating connection detector class instance
        cd                 = new ConnectionDetector(getApplicationContext());
        garansimuPref 	   = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        queue    	       = Volley.newRequestQueue(this);
        fontUbuntuL        = FontCache.get(this, "Ubuntu-L");
        fontUbuntuB        = FontCache.get(this, "Ubuntu-B");
        txtTitle           = (TextView) findViewById(R.id.txtTitle);
        txtFirstName       = (EditText) findViewById(R.id.txtFirstName);
        txtPostCode        = (EditText) findViewById(R.id.txtPostCode);
        txtEmail           = (EditText) findViewById(R.id.txtEmail);
        txtPassword        = (EditText) findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        txtCountry         = (EditText) findViewById(R.id.txtCountry);
        txtCity            = (EditText) findViewById(R.id.txtCity);
        txtAddress         = (EditText) findViewById(R.id.txtAddress);
        txtPhone           = (EditText) findViewById(R.id.txtPhone);
        radioSubscribe     = (RadioButton) findViewById(R.id.radioSubscribe);
        radioUnsubscribe   = (RadioButton) findViewById(R.id.radioUnsubscribe);
        lblSave            = (TextView) findViewById(R.id.lblSave);
        txtReferealId      = (EditText) findViewById(R.id.txtReferealId);
        lblAddPhoto        = (TextView) findViewById(R.id.lblAddPhoto);
        imgProfile         = (ImageView) findViewById(R.id.imgProfile);
        imgLeft            = (ImageView) findViewById(R.id.imgLeft);

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtTitle.setText("MY PROFILE");
        txtTitle.setTypeface(fontUbuntuB);
        txtFirstName.setTypeface(fontUbuntuL);
        txtPostCode.setTypeface(fontUbuntuL);
        txtEmail.setTypeface(fontUbuntuL);
        txtPassword.setTypeface(fontUbuntuL);
        txtConfirmPassword.setTypeface(fontUbuntuL);
        txtCountry.setTypeface(fontUbuntuL);
        txtCity.setTypeface(fontUbuntuL);
        txtAddress.setTypeface(fontUbuntuL);
        txtPhone.setTypeface(fontUbuntuL);
        radioSubscribe.setTypeface(fontUbuntuL);
        radioUnsubscribe.setTypeface(fontUbuntuL);
        lblSave.setTypeface(fontUbuntuB);
        txtReferealId.setTypeface(fontUbuntuL);
        lblAddPhoto.setTypeface(fontUbuntuL);
        lblAddPhoto.setText(Html.fromHtml("<u>Add Photo</u>"));

        pDialog = new ProgressDialog(ProfileEditActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);

        lblSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtFirstName.getText().toString().length()==0) {
                    txtFirstName.setError("Full Name is Required!");
                } else if (txtEmail.getText().toString().length()==0) {
                    txtEmail.setError("Email is Required!");
                } else if (txtCity.getText().toString().length()==0) {
                    txtCity.setError("City is Required");
                } else if (txtPostCode.getText().toString().length()==0) {
                    txtPostCode.setError("Post Code is Required!");
                } else if (txtAddress.getText().toString().length()==0) {
                    txtAddress.setError("Address is Required!");
                } else if (txtPhone.getText().toString().length()==0) {
                    txtPhone.setError("Phone is Required!");
                } else {
                    pDialog.show();
                    if (imagepathOne==null) {
                        new updateProfile().execute();
                    } else {
                        new UploadImage().execute();
                    }
                }
            }
        });

        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
            getUserProfile(garansimuPref.getString("Id", ""));
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            Referensi.showAlertDialog(ProfileEditActivity.this, "No Internet Connection", "You don't have internet connection.", false);
        }
    }

    public void getUserProfile(String strIdCust) {
        pDialog.show();
        String url = Referensi.url+"/getUserProfile.php?pk_id="+strIdCust;
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i<response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(0);
                        txtFirstName.setText(jsonObject.getString("name"));
                        txtEmail.setText(jsonObject.getString("email"));
                        txtEmail.setEnabled(false);
                        txtCity.setText(jsonObject.getString("city"));
                        txtPostCode.setText(jsonObject.getString("postal_code"));
                        txtAddress.setText(jsonObject.getString("address"));
                        txtPhone.setText(jsonObject.getString("phone"));
                        Glide.with(ProfileEditActivity.this).load(Referensi.url_img2 + jsonObject.getString("image"))
                                .placeholder(R.drawable.img_loader).dontAnimate().into(imgProfile);
                        strUserImage = jsonObject.getString("image");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsObjRequest);
    }

    private class UploadImage extends AsyncTask<HttpEntity, Void, HttpEntity> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected HttpEntity doInBackground(HttpEntity... params) {
            return doFileUpload();
        }
        @Override
        protected void onPostExecute(HttpEntity result) {
            super.onPostExecute(result);
            new updateProfile().execute();
        }
    }

    private class updateProfile extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            String strFullName = null;
            String strCity     = null;
            String strAddress  = null;
            try {
                strFullName = URLEncoder.encode(txtFirstName.getText().toString().replace("\"", "'"), "utf-8");
                strCity     = URLEncoder.encode(txtCity.getText().toString().replace("\"", "'"), "utf-8");
                strAddress  = URLEncoder.encode(txtAddress.getText().toString().replace("\"", "'"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (imagepathOne==null) {
                strUpdateImage = strUserImage;
            } else {
                strUpdateImage = strImage;
            }

            url = Referensi.url + "/service.php?ct=UPDATEPROFILE&name=" + strFullName +
                    "&phone=" + txtPhone.getText().toString() +
                    "&city=" + strCity +
                    "&address=" + strAddress +
                    "&postal_code=" + txtPostCode.getText().toString() +
                    "&image=" + strUpdateImage +
                    "&pk_id=" + garansimuPref.getString("Id", "");

            String hasil = callURL.call(url);
            return hasil;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            SharedPreferences.Editor editor = garansimuPref.edit();
            editor.putString("Name", txtFirstName.getText().toString());
            editor.putString("Phone", txtPhone.getText().toString());
            editor.putString("City", txtCity.getText().toString());
            editor.putString("Address", txtAddress.getText().toString());
            editor.putString("Image", strUpdateImage);
            editor.commit();

            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Update Profile succesfully!", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            finish();
        }
    }

    private HttpEntity doFileUpload() {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = null;
            File file1 = null;
            FileBody bin1 = null;

            MultipartEntity reqEntity = new MultipartEntity();
            post = new HttpPost("http://www.linkincube.com/download/UploadOne.php");
            if (imagepathOne!=null) {
                file1    = new File(imagepathOne);
                strImage = file1.getName();
            }

            bin1 = new FileBody(file1);
            reqEntity.addPart("uploadedfile1", bin1);

            reqEntity.addPart("user", new StringBody("User"));
            post.setEntity(reqEntity);

            HttpResponse response = client.execute(post);
            resEntity = response.getEntity();
            @SuppressWarnings("unused")
            final String response_str = EntityUtils.toString(resEntity);
        } catch (Exception ex) {
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        }
        return resEntity;
    }

    public void dispatchTakePictureIntent(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            String selectedImagePath = ImageFilePath.getPath(getApplicationContext(), selectedImageUri);
            imagepathOne = selectedImagePath;
            Glide.with(ProfileEditActivity.this).load(selectedImagePath).asBitmap().into(imgProfile);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
