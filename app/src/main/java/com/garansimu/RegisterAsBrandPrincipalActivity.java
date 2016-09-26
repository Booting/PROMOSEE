package com.garansimu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.garansimu.Utils.callURL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegisterAsBrandPrincipalActivity extends Activity {
    private Typeface fontLatoReguler, fontLatoHeavy;
    private TextView txtTitle, lblDaftarkan, lblSubmit, lblDaftarInfo;
    private EditText txtFullName, txtPhone, txtEmail, txtBrand, txtWebsite;
    private Spinner spinPhoneCode, spinCategory;
    private RequestQueue queue;
    private ProgressDialog pDialog;
    private ArrayList<Kategori> categoriList;
    private ImageView imgLeft;
    private String url = "";

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_register_as_brand_principal);

        // creating connection detector class instance
        cd              = new ConnectionDetector(getApplicationContext());
        queue    	    = Volley.newRequestQueue(this);
        fontLatoReguler = FontCache.get(this, "Lato-Regular");
        fontLatoHeavy 	= FontCache.get(this, "Lato-Heavy");
        txtTitle        = (TextView) findViewById(R.id.txtTitle);
        lblDaftarkan    = (TextView) findViewById(R.id.lblDaftarkan);
        txtFullName     = (EditText) findViewById(R.id.txtFullName);
        spinPhoneCode   = (Spinner) findViewById(R.id.spinPhoneCode);
        txtPhone        = (EditText) findViewById(R.id.txtPhone);
        txtEmail        = (EditText) findViewById(R.id.txtEmail);
        txtBrand        = (EditText) findViewById(R.id.txtBrand);
        txtWebsite      = (EditText) findViewById(R.id.txtWebsite);
        lblSubmit       = (TextView) findViewById(R.id.lblSubmit);
        lblDaftarInfo   = (TextView) findViewById(R.id.lblDaftarInfo);
        spinCategory    = (Spinner) findViewById(R.id.spinCategory);
        imgLeft         = (ImageView) findViewById(R.id.imgLeft);

        txtTitle.setText("REGISTER AS BRAND PRINCIPAL");
        txtTitle.setTypeface(fontLatoHeavy);
        lblDaftarkan.setTypeface(fontLatoHeavy);
        txtFullName.setTypeface(fontLatoReguler);
        txtPhone.setTypeface(fontLatoReguler);
        txtEmail.setTypeface(fontLatoReguler);
        txtBrand.setTypeface(fontLatoReguler);
        txtWebsite.setTypeface(fontLatoReguler);
        lblSubmit.setTypeface(fontLatoHeavy);
        lblDaftarInfo.setTypeface(fontLatoReguler);

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        List<String> list = new ArrayList<>();
        list.add("+62");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(fontLatoReguler);
                ((TextView) v).setTextSize(14);
                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(fontLatoReguler);
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPhoneCode.setAdapter(dataAdapter);

        pDialog = new ProgressDialog(RegisterAsBrandPrincipalActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);

        lblSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtFullName.getText().length()==0) {
                    txtFullName.setError("FullName is Required!");
                } else if (txtPhone.getText().length()==0) {
                    txtPhone.setError("Phone is Required!");
                } else if (txtEmail.getText().length()==0) {
                    txtEmail.setError("Email is Required!");
                } else if (txtBrand.getText().length()==0) {
                    txtBrand.setError("Brand is Required!");
                } else if (txtWebsite.getText().length()==0) {
                    txtWebsite.setError("Website is Required!");
                } else if (!Referensi.isValidEmail(txtEmail.getText().toString())) {
                    txtEmail.setError("Invalid Email");
                } else {
                    // get Internet status
                    isInternetPresent = cd.isConnectingToInternet();

                    // check for Internet status
                    if (isInternetPresent) {
                        // Internet Connection is Present
                        // make HTTP requests
                        new addNewPrincipal().execute();
                    } else {
                        // Internet connection is not present
                        // Ask user to connect to Internet
                        Referensi.showAlertDialog(RegisterAsBrandPrincipalActivity.this, "No Internet Connection", "You don't have internet connection.", false);
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
            getCategori();
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            Referensi.showAlertDialog(RegisterAsBrandPrincipalActivity.this, "No Internet Connection", "You don't have internet connection.", false);
        }
    }

    public void getCategori() {
        pDialog.show();
        String url = Referensi.url+"/getCategori.php";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray categories = response.getJSONArray("categories");
                    categoriList = new ArrayList<Kategori>();
                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject catObj = (JSONObject) categories.get(i);
                        Kategori cat = new Kategori(catObj.getInt("id"), catObj.getString("category"), catObj.getString("logo1"));
                        categoriList.add(cat);
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
        for (int i = 0; i < categoriList.size(); i++) {
            list.add(categoriList.get(i).getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(fontLatoReguler);
                ((TextView) v).setTextSize(14);
                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(fontLatoReguler);
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(dataAdapter);
    }

    private class addNewPrincipal extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String strFullName = null;
            String strBrand    = null;
            String strCategory = null;
            String strWebsite  = null;
            try {
                strFullName = URLEncoder.encode(txtFullName.getText().toString().replace("\"", "'"), "utf-8");
                strBrand    = URLEncoder.encode(txtBrand.getText().toString().replace("\"", "'"), "utf-8");
                strCategory = URLEncoder.encode(spinCategory.getSelectedItem().toString().replace("\"", "'"), "utf-8");
                strWebsite  = URLEncoder.encode(txtWebsite.getText().toString().replace("\"", "'"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Calendar calendar   = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String strDateNow   = df.format(calendar.getTime());

            url = Referensi.url + "/service.php?ct=REGISTERASBRANDPRINCIPAL&nama_pt=" + strFullName +
                    "&telp=" + spinPhoneCode.getSelectedItem().toString() + "" +txtPhone.getText().toString() +
                    "&email=" + txtEmail.getText().toString() +
                    "&brand=" + strBrand +
                    "&kategori=" + strCategory +
                    "&website=" + strWebsite +
                    "&tanggal_regist=" + strDateNow;

            String hasil = callURL.call(url);
            return hasil;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Register as Brand Principal succesfully!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
