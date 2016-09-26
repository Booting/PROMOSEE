package com.garansimu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.garansimu.Utils.ConnectionDetector;
import com.garansimu.Utils.FontCache;
import com.garansimu.Utils.Referensi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {
    private Typeface fontUbuntuL, fontUbuntuB;
    private TextView txtTitle, lblForgotPassword, lblSignUp, lblLoginAsUser, lblLoginAsCompany, lblPromosee;
    private EditText txtEmail, txtPassword;
    private RequestQueue queue;
    private ProgressDialog pDialog;
    private SharedPreferences garansimuPref;
    private ImageView imgLeft;

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_login);

        // creating connection detector class instance
        cd                = new ConnectionDetector(getApplicationContext());
        garansimuPref 	  = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        queue    	      = Volley.newRequestQueue(this);
        fontUbuntuL       = FontCache.get(this, "Ubuntu-L");
        fontUbuntuB       = FontCache.get(this, "Ubuntu-B");
        txtTitle          = (TextView) findViewById(R.id.txtTitle);
        txtEmail          = (EditText) findViewById(R.id.txtEmail);
        txtPassword       = (EditText) findViewById(R.id.txtPassword);
        lblSignUp         = (TextView) findViewById(R.id.lblSignUp);
        lblLoginAsUser    = (TextView) findViewById(R.id.lblLoginAsUser);
        lblLoginAsCompany = (TextView) findViewById(R.id.lblLoginAsCompany);
        lblForgotPassword = (TextView) findViewById(R.id.lblForgotPassword);
        imgLeft           = (ImageView) findViewById(R.id.imgLeft);
        lblPromosee       = (TextView) findViewById(R.id.lblPromosee);

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtTitle.setTypeface(fontUbuntuB);
        txtEmail.setTypeface(fontUbuntuL);
        txtPassword.setTypeface(fontUbuntuL);
        lblSignUp.setTypeface(fontUbuntuB);
        lblLoginAsUser.setTypeface(fontUbuntuB);
        lblLoginAsCompany.setTypeface(fontUbuntuB);
        lblForgotPassword.setTypeface(fontUbuntuL);
        lblPromosee.setTypeface(fontUbuntuL);

        lblLoginAsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (txtEmail.getText().toString().equalsIgnoreCase("") && txtPassword.getText().toString().equalsIgnoreCase("")) {
                    txtEmail.setError("Email is required!");
                    txtPassword.setError("Password is required!");
                } else if (txtEmail.getText().toString().equalsIgnoreCase("")) {
                    txtEmail.setError("Email is required!");
                } else if (txtPassword.getText().toString().equalsIgnoreCase("")) {
                    txtPassword.setError("Password is required!");
                } else if (!Referensi.isValidEmail(txtEmail.getText().toString())) {
                    txtEmail.setError("Invalid Email");
                } else {
                    // get Internet status
                    isInternetPresent = cd.isConnectingToInternet();

                    // check for Internet status
                    if (isInternetPresent) {
                        // Internet Connection is Present
                        // make HTTP requests
                        getLogin();
                    } else {
                        // Internet connection is not present
                        // Ask user to connect to Internet
                        Referensi.showAlertDialog(LoginActivity.this, "No Internet Connection", "You don't have internet connection.", false);
                    }
                }*/
                if (txtEmail.getText().toString().equalsIgnoreCase("test@gmail.com") && txtPassword.getText().toString().equalsIgnoreCase("1234")) {
                    Intent intent = new Intent(LoginActivity.this, OurBrandActivity.class);
                    startActivity(intent);
                }
            }
        });

        lblForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                //startActivity(intent);
            }
        });

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);
    }

    public void getLogin() {
        pDialog.show();
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
                            editor.putString("Email", jObjLogin.optString("email"));
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
