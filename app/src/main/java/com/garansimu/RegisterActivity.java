package com.garansimu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RegisterActivity extends Activity {
    private Typeface fontUbuntuL, fontUbuntuB;
    private TextView txtTitle, lblSignUp, lblAddPhoto;
    private EditText txtFirstName, txtPostCode, txtEmail, txtPassword, txtConfirmPassword, txtCity,
            txtAddress, txtPhone, txtReferealId;
    private RadioButton radioSubscribe, radioUnsubscribe;
    private ImageView imgProfile, imgLeft;
    private String strImage;
    private String mCurrentPhotoPath=null;
    private RequestQueue queue;
    private ProgressDialog pDialog;
    private HttpEntity resEntity;
    private String url = "";
    private SharedPreferences garansimuPref;
    private Spinner spinCountry;
    private ArrayList<Kategori> countryList;

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_register);

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
        spinCountry        = (Spinner) findViewById(R.id.spinCountry);
        txtCity            = (EditText) findViewById(R.id.txtCity);
        txtAddress         = (EditText) findViewById(R.id.txtAddress);
        txtPhone           = (EditText) findViewById(R.id.txtPhone);
        radioSubscribe     = (RadioButton) findViewById(R.id.radioSubscribe);
        radioUnsubscribe   = (RadioButton) findViewById(R.id.radioUnsubscribe);
        lblSignUp          = (TextView) findViewById(R.id.lblSignUp);
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

        txtTitle.setText("REGISTER");
        txtTitle.setTypeface(fontUbuntuB);
        txtFirstName.setTypeface(fontUbuntuL);
        txtPostCode.setTypeface(fontUbuntuL);
        txtEmail.setTypeface(fontUbuntuL);
        txtPassword.setTypeface(fontUbuntuL);
        txtConfirmPassword.setTypeface(fontUbuntuL);
        txtCity.setTypeface(fontUbuntuL);
        txtAddress.setTypeface(fontUbuntuL);
        txtPhone.setTypeface(fontUbuntuL);
        radioSubscribe.setTypeface(fontUbuntuL);
        radioUnsubscribe.setTypeface(fontUbuntuL);
        lblSignUp.setTypeface(fontUbuntuB);
        txtReferealId.setTypeface(fontUbuntuL);
        lblAddPhoto.setTypeface(fontUbuntuL);
        lblAddPhoto.setText(Html.fromHtml("<u>Add Photo</u>"));

        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);

        lblSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPhotoPath==null) {
                    Toast.makeText(getApplicationContext(), "Sorry, photo is required!", Toast.LENGTH_LONG).show();
                } else if (txtFirstName.getText().toString().length()==0) {
                    txtFirstName.setError("Full Name is Required!");
                } else if (txtEmail.getText().toString().length()==0) {
                    txtEmail.setError("Email is Required!");
                } else if (txtPassword.getText().toString().length()==0) {
                    txtPassword.setError("Password is Required!");
                } else if (txtConfirmPassword.getText().toString().length()==0) {
                    txtConfirmPassword.setError("Confirm Password is Required!");
                } else if (txtCity.getText().toString().length()==0) {
                    txtCity.setError("City is Required");
                } else if (txtPostCode.getText().toString().length()==0) {
                    txtPostCode.setError("Post Code is Required!");
                } else if (txtAddress.getText().toString().length()==0) {
                    txtAddress.setError("Address is Required!");
                } else if (txtPhone.getText().toString().length()==0) {
                    txtPhone.setError("Phone is Required!");
                } else if (!txtPassword.getText().toString().equalsIgnoreCase(txtConfirmPassword.getText().toString())) {
                    txtPassword.setError("Password and Confirm Password not match!");
                    txtConfirmPassword.setError("Password and Confirm Password not match!");
                } else if (!Referensi.isValidEmail(txtEmail.getText().toString())) {
                    txtEmail.setError("Invalid Email");
                } else {
                    // get Internet status
                    isInternetPresent = cd.isConnectingToInternet();

                    // check for Internet status
                    if (isInternetPresent) {
                        // Internet Connection is Present
                        // make HTTP requests
                        cekEmail();
                    } else {
                        // Internet connection is not present
                        // Ask user to connect to Internet
                        Referensi.showAlertDialog(RegisterActivity.this, "No Internet Connection", "You don't have internet connection.", false);
                    }
                }
            }
        });

        getCountry();
    }

    public void getCountry() {
        pDialog.show();
        String url = Referensi.url+"/getCountry.php";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray categories = response.getJSONArray("categories");
                    countryList = new ArrayList<Kategori>();
                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject catObj = (JSONObject) categories.get(i);
                        Kategori cat = new Kategori(catObj.getInt("id"), catObj.getString("nama_country"), "");
                        countryList.add(cat);
                    }
                    populateSpinnerCategori();
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void populateSpinnerCategori() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < countryList.size(); i++) {
            list.add(countryList.get(i).getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(fontUbuntuL);
                ((TextView) v).setTextSize(14);
                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(fontUbuntuL);
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCountry.setAdapter(dataAdapter);
    }

    public void cekEmail() {
        pDialog.show();
        String url = Referensi.url+"/cekEmail.php?email="+txtEmail.getText().toString();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equalsIgnoreCase("true")) {
                        Toast.makeText(getApplicationContext(), "Sorry, email was registered!", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                    } else {
                        new UploadImage().execute();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                }
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
            new addNewUser().execute();
        }
    }

    private class addNewUser extends AsyncTask<String, Void, String> {
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

            Calendar calendar   = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String strDateNow   = df.format(calendar.getTime());

            url = Referensi.url + "/service.php?ct=REGISTER&email=" + txtEmail.getText().toString() +
                    "&name=" + strFullName +
                    "&password=" + txtPassword.getText().toString() +
                    "&phone=" + txtPhone.getText().toString() +
                    "&city=" + strCity +
                    "&address=" + strAddress +
                    "&postal_code=" + txtPostCode.getText().toString() +
                    "&date_reg=" + strDateNow +
                    "&image=" + strImage;

            String hasil = callURL.call(url);
            return hasil;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), "Register succesfully!", Toast.LENGTH_LONG).show();
            getLogin();
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
            if (mCurrentPhotoPath!=null) {
                file1    = new File(mCurrentPhotoPath);
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
        final CharSequence[] items = { "Take Photo", "Choose from Library" };
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(takePictureIntent, 1);
                        }
                    }
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent();
                    intent.setType("*/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select File"), 2);
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Glide.with(RegisterActivity.this).load(mCurrentPhotoPath).asBitmap().into(imgProfile);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            String selectedImagePath = ImageFilePath.getPath(getApplicationContext(), selectedImageUri);
            mCurrentPhotoPath = selectedImagePath;
            Glide.with(RegisterActivity.this).load(selectedImagePath).asBitmap().into(imgProfile);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void getLogin() {
        String url = Referensi.url+"/login.php?email="+txtEmail.getText().toString()+"&password="+txtPassword.getText().toString();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jArrLogin  = response.getJSONArray("statuslogin");

                    for (int i = 0; i<jArrLogin.length(); i++){
                        JSONObject jObjLogin = jArrLogin.getJSONObject(i);

                        if (jObjLogin.getString("st").equalsIgnoreCase("ok")) {
                            SharedPreferences.Editor editor = garansimuPref.edit();
                            editor.putString("Id", jObjLogin.optString("pk_id"));
                            editor.putString("Name", jObjLogin.optString("name"));
                            editor.putString("Phone", jObjLogin.optString("phone"));
                            editor.putString("City", jObjLogin.optString("city"));
                            editor.putString("Address", jObjLogin.optString("address"));
                            editor.putString("Image", jObjLogin.optString("image"));
                            editor.putBoolean("IsFirst", true);
                            editor.commit();

                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        Toast.makeText(getApplicationContext(), jObjLogin.getString("hasil"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
