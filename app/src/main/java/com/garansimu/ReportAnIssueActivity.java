package com.garansimu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.garansimu.Utils.ConnectionDetector;
import com.garansimu.Utils.FontCache;
import com.garansimu.Utils.Mail;
import com.garansimu.Utils.Referensi;
import com.garansimu.Utils.callURL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReportAnIssueActivity extends Activity {
    private Typeface fontUbuntuL, fontUbuntuB;
    private TextView txtTitle, txtBack, lblKodeBarang, lblIssue, lblMasaGaransi, lblEmailPrincipal;
    private ImageView imgLogo, imgLeft;
    private EditText txtKodeBarang, txtIssue, txtMasaGaransi, txtEmailPrincipal;
    private Button btnCancel, btnSend;
    private SharedPreferences garansimuPref;
    private String url = "", strNamaPt;
    private ProgressDialog pDialog;
    private RequestQueue queue;
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
        setContentView(R.layout.activity_report_an_issue);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        m = new Mail("garansee.info@gmail.com", "g4r4nsee");

        // creating connection detector class instance
        queue    	   = Volley.newRequestQueue(this);
        cd             = new ConnectionDetector(getApplicationContext());
        garansimuPref  = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        fontUbuntuL    = FontCache.get(this, "Ubuntu-L");
        fontUbuntuB    = FontCache.get(this, "Ubuntu-B");
        txtTitle       = (TextView) findViewById(R.id.txtTitle);
        txtTitle       = (TextView) findViewById(R.id.txtTitle);
        txtBack        = (TextView) findViewById(R.id.txtBack);
        imgLogo        = (ImageView) findViewById(R.id.imgLogo);
        imgLeft        = (ImageView) findViewById(R.id.imgLeft);
        lblKodeBarang  = (TextView) findViewById(R.id.lblKodeBarang);
        txtKodeBarang  = (EditText) findViewById(R.id.txtKodeBarang);
        lblIssue       = (TextView) findViewById(R.id.lblIssue);
        txtIssue       = (EditText) findViewById(R.id.txtIssue);
        lblMasaGaransi = (TextView) findViewById(R.id.lblMasaGaransi);
        txtMasaGaransi = (EditText) findViewById(R.id.txtMasaGaransi);
        btnCancel      = (Button) findViewById(R.id.btnCancel);
        btnSend        = (Button) findViewById(R.id.btnSend);
        lblEmailPrincipal = (TextView) findViewById(R.id.lblEmailPrincipal);
        txtEmailPrincipal = (EditText) findViewById(R.id.txtEmailPrincipal);

        imgLeft.setImageResource(R.drawable.dots_vertical);
        imgLeft.setVisibility(View.VISIBLE);
        imgLogo.setVisibility(View.GONE);
        txtBack.setVisibility(View.VISIBLE);
        txtTitle.setTypeface(fontUbuntuB);
        txtTitle.setText("REPORT AN ISSUE");
        txtBack.setTypeface(fontUbuntuL);

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lblKodeBarang.setTypeface(fontUbuntuL);
        txtKodeBarang.setTypeface(fontUbuntuL);
        lblIssue.setTypeface(fontUbuntuL);
        txtIssue.setTypeface(fontUbuntuL);
        lblMasaGaransi.setTypeface(fontUbuntuL);
        txtMasaGaransi.setTypeface(fontUbuntuL);
        lblEmailPrincipal.setTypeface(fontUbuntuL);
        txtEmailPrincipal.setTypeface(fontUbuntuL);
        btnCancel.setTypeface(fontUbuntuB);
        btnSend.setTypeface(fontUbuntuB);

        Calendar calendar   = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String strDateNow   = df.format(calendar.getTime());
        txtKodeBarang.setText(strDateNow);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtKodeBarang.getText().length()==0) {
                    txtKodeBarang.setError("Date is Required!");
                } else if (txtIssue.getText().length()==0) {
                    txtIssue.setError("Issue is Required!");
                } else if (txtEmailPrincipal.getText().length()==0) {
                    txtEmailPrincipal.setError("Email Principal is Required!");
                } else {
                    // get Internet status
                    isInternetPresent = cd.isConnectingToInternet();

                    // check for Internet status
                    if (isInternetPresent) {
                        // Internet Connection is Present
                        // make HTTP requests
                        new addNewIssue().execute();
                    } else {
                        // Internet connection is not present
                        // Ask user to connect to Internet
                        Referensi.showAlertDialog(ReportAnIssueActivity.this, "No Internet Connection", "You don't have internet connection.", false);
                    }
                }
            }
        });

        pDialog = new ProgressDialog(ReportAnIssueActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);

        getPrincipalEmail();
    }

    public void getPrincipalEmail() {
        pDialog.show();
        String url = Referensi.url+"/getPrincipalEmail.php?brand='"+getIntent().getExtras().getString("strBrand")+"'";
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length()!=0) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(0);
                        txtEmailPrincipal.setText(jsonObject.getString("email"));
                        strNamaPt = jsonObject.getString("nama_pt");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ReportAnIssueActivity.this, "No email principal!", Toast.LENGTH_SHORT).show();
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

    private class addNewIssue extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String strReason = null;
            try {
                strReason = URLEncoder.encode(txtIssue.getText().toString().replace("\"", "'"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            url = Referensi.url + "/service.php?ct=REPORTANISSUE&date=" + txtKodeBarang.getText().toString() +
                    "&id_cust=" + garansimuPref.getString("Id", "") +
                    "&reason=" + strReason;

            String hasil = callURL.call(url);
            return hasil;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            sendEmail();
        }
    }

    public void sendEmail() {
        String[] toArr = { txtEmailPrincipal.getText().toString() };
        m.setTo(toArr);
        m.setFrom("garansee.info@gmail.com");
        m.setSubject("Garansee - Issue Report");
        m.setBody("Hi " + strNamaPt + ", \n\n" +
                "Customer ada melaporkan issue, mohon segera ditindaklanjuti, dengan data dibawah ini\n\n" +
                "Member Information\n" +
                "Nama "+garansimuPref.getString("Name", "")+ "\n" +
                "Email " +garansimuPref.getString("Email", "")+ "\n" +
                "HP " +garansimuPref.getString("Phone", "")+ "\n" +
                "Reason : " +txtIssue.getText().toString()+ "\n\n" +
                "- Garansee Team -");

        try {
            if(m.send()) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Report telah berhasil dikirimkan, dan akan ditindaklanjuti segera.", Toast.LENGTH_LONG).show();
                txtIssue.setText("");
            } else {
                Toast.makeText(ReportAnIssueActivity.this, "Email was not sent.", Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(ReportAnIssueActivity.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
