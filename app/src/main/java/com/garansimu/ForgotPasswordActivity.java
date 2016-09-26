package com.garansimu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.garansimu.Utils.Mail;
import com.garansimu.Utils.Referensi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends Activity {
    private Typeface fontUbuntuL, fontUbuntuB;
    private TextView txtTitle, lblForgotPassword;
    private EditText txtEmail;
    private RequestQueue queue;
    private ProgressDialog pDialog;
    private SharedPreferences garansimuPref;
    private ImageView imgLeft;
    private Mail m;

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_forgot_password);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        m = new Mail("garansee.info@gmail.com", "g4r4nsee");

        // creating connection detector class instance
        cd                = new ConnectionDetector(getApplicationContext());
        garansimuPref 	  = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        queue    	      = Volley.newRequestQueue(this);
        fontUbuntuL       = FontCache.get(this, "Ubuntu-L");
        fontUbuntuB       = FontCache.get(this, "Ubuntu-B");
        txtTitle          = (TextView) findViewById(R.id.txtTitle);
        txtEmail          = (EditText) findViewById(R.id.txtEmail);
        lblForgotPassword = (TextView) findViewById(R.id.lblForgotPassword);
        imgLeft           = (ImageView) findViewById(R.id.imgLeft);

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtTitle.setText("FORGOT PASSWORD");
        txtTitle.setTypeface(fontUbuntuB);
        txtEmail.setTypeface(fontUbuntuL);
        lblForgotPassword.setTypeface(fontUbuntuB);

        lblForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtEmail.getText().length()==0) {
                    txtEmail.setError("Email is required!");
                } else if (!Referensi.isValidEmail(txtEmail.getText().toString())) {
                    txtEmail.setError("Invalid Email");
                } else {
                    // get Internet status
                    isInternetPresent = cd.isConnectingToInternet();

                    // check for Internet status
                    if (isInternetPresent) {
                        // Internet Connection is Present
                        // make HTTP requests
                        getForgotPassword();
                    } else {
                        // Internet connection is not present
                        // Ask user to connect to Internet
                        Referensi.showAlertDialog(ForgotPasswordActivity.this, "No Internet Connection", "You don't have internet connection.", false);
                    }
                }
            }
        });

        pDialog = new ProgressDialog(ForgotPasswordActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);
    }

    public void getForgotPassword() {
        pDialog.show();
        String url = Referensi.url+"/getForgotPassword.php?email="+txtEmail.getText().toString();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jArrLogin  = response.getJSONArray("statuslogin");

                    for (int i = 0; i<jArrLogin.length(); i++){
                        JSONObject jObjLogin = jArrLogin.getJSONObject(i);

                        if (jObjLogin.getString("st").equalsIgnoreCase("ok")) {
                            sendEmail(jObjLogin.getString("email"), jObjLogin.getString("name"), jObjLogin.getString("password"));
                        } else {
                            Toast.makeText(getApplicationContext(), jObjLogin.getString("hasil"), Toast.LENGTH_LONG).show();
                        }
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

    public void sendEmail(String strEmail, String strName, String strPassword) {
        String[] toArr = { strEmail };
        m.setTo(toArr);
        m.setFrom("garansee.info@gmail.com");
        m.setSubject("Garansee - Membership Information");
        m.setBody("Hi " + strName + ", \n\n" +
                "Jika Anda menerima Email ini, namun tidak melakukan permintaan atas informasi ini, pastikan orang yang melakukan transaksi ini tidak dapat mengakses informasi Anda.\n\n" +
                "Anda dapat mengakses halaman profil Anda dengan menggunakan password baru di bawah ini:\n\n" +
                "Member Information\n" +
                "Nama "+strName+ "\n" +
                "Email Login " +strEmail+ "\n" +
                "New Password " +strPassword+ "\n\n" +
                "- Garansee Team -");

        try {
            if(m.send()) {
                Toast.makeText(ForgotPasswordActivity.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Email was not sent.", Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(ForgotPasswordActivity.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
