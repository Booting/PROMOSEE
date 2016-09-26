package com.garansimu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.garansimu.Utils.ConnectionDetector;
import com.garansimu.Utils.FontCache;
import com.garansimu.Utils.Referensi;
import com.garansimu.Utils.callURL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class LoginRegisterActivity extends Activity {
    private Typeface fontUbuntuL, fontUbuntuB;
    private TextView txtTitle, txtEmail, txtSignUp, txtLogIn, txtTerm1, txtTerm2;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private RelativeLayout linEmail;
    private RequestQueue queue;
    private ProgressDialog pDialog;
    private String url = "";
    private String strId, strEmail, strName;
    private SharedPreferences garansimuPref;

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_login_register);

        // creating connection detector class instance
        cd              = new ConnectionDetector(getApplicationContext());
        garansimuPref   = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        queue    	    = Volley.newRequestQueue(this);
        fontUbuntuL     = FontCache.get(this, "Ubuntu-L");
        fontUbuntuB     = FontCache.get(this, "Ubuntu-B");
        txtTitle        = (TextView) findViewById(R.id.txtTitle);
        loginButton     = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        linEmail        = (RelativeLayout) findViewById(R.id.linEmail);
        txtEmail        = (TextView) findViewById(R.id.txtEmail);
        txtSignUp       = (TextView) findViewById(R.id.txtSignUp);
        txtLogIn        = (TextView) findViewById(R.id.txtLogIn);
        txtTerm1        = (TextView) findViewById(R.id.txtTerm1);
        txtTerm2        = (TextView) findViewById(R.id.txtTerm2);

        txtTitle.setTypeface(fontUbuntuB);
        txtEmail.setTypeface(fontUbuntuB);
        txtSignUp.setTypeface(fontUbuntuB);
        txtLogIn.setTypeface(fontUbuntuB);
        txtTerm1.setTypeface(fontUbuntuL);
        txtTerm2.setTypeface(fontUbuntuL);
        txtTerm2.setPaintFlags(txtTerm2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.garansimu", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        // Callback registration
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject me, GraphResponse response) {
                        if (response.getError() != null) {
                            // handle error
                        } else {
                            strId    = me.optString("id");
                            strName  = me.optString("name");
                            strEmail = me.optString("email");

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
                                Referensi.showAlertDialog(LoginRegisterActivity.this, "No Internet Connection", "You don't have internet connection.", false);
                            }
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        loginButton.setPadding(50, 35, 50, 35);

        pDialog = new ProgressDialog(LoginRegisterActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);
    }

    public void cekEmail() {
        pDialog.show();
        String url = Referensi.url+"/cekEmail.php?email="+strEmail;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equalsIgnoreCase("true")) {
                        pDialog.dismiss();
                        getLogin();
                    } else {
                        new addNewUser().execute();
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

    private class addNewUser extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            String strFullName = null;
            try {
                strFullName = URLEncoder.encode(strName.replace("\"", "'"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Calendar calendar   = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String strDateNow   = df.format(calendar.getTime());

            url = Referensi.url + "/service.php?ct=REGISTER&email=" + strEmail +
                    "&name=" + strFullName +
                    "&password=" + "loginFacebook" +
                    "&phone=" + "" +
                    "&city=" + "" +
                    "&address=" + "" +
                    "&postal_code=" + "" +
                    "&date_reg=" + strDateNow +
                    "&image=" + strId;

            String hasil = callURL.call(url);
            return hasil;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            getLogin();
        }
    }

    public void getLogin() {
        String url = Referensi.url+"/login.php?email="+strEmail+"&password="+"loginFacebook";
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

    public void btnLoginClick(View v) {
        startActivity(new Intent(LoginRegisterActivity.this, LoginActivity.class));
    }

    public void btnRegisterClick(View v) {
        startActivity(new Intent(LoginRegisterActivity.this, RegisterActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
