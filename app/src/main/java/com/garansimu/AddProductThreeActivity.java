package com.garansimu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
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
import org.json.JSONException;
import org.json.JSONObject;

public class AddProductThreeActivity extends Activity {
    private Typeface fontUbuntuL, fontUbuntuB;
    private TextView txtTitle, lblSave, lblQuestionOne, lblQuestionTwo, lblQuestionThree, lblQuestionFour;
    private ImageView imgLogoRight, imgHeader;
    private DisplayMetrics displaymetrics;
    private EditText txtQuestionOne, txtQuestionTwo, txtQuestionThree, txtQuestionFour;
    private RequestQueue queue;
    private ProgressDialog pDialog;

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_add_product_three);

        // creating connection detector class instance
        cd               = new ConnectionDetector(getApplicationContext());
        queue    	     = Volley.newRequestQueue(this);
        fontUbuntuL      = FontCache.get(this, "Ubuntu-L");
        fontUbuntuB      = FontCache.get(this, "Ubuntu-B");
        txtTitle         = (TextView) findViewById(R.id.txtTitle);
        lblSave          = (TextView) findViewById(R.id.lblSave);
        imgLogoRight     = (ImageView) findViewById(R.id.imgLogoRight);
        imgHeader        = (ImageView) findViewById(R.id.imgHeader);
        lblQuestionOne   = (TextView) findViewById(R.id.lblQuestionOne);
        txtQuestionOne   = (EditText) findViewById(R.id.txtQuestionOne);
        lblQuestionTwo   = (TextView) findViewById(R.id.lblQuestionTwo);
        txtQuestionTwo   = (EditText) findViewById(R.id.txtQuestionTwo);
        lblQuestionThree = (TextView) findViewById(R.id.lblQuestionThree);
        txtQuestionThree = (EditText) findViewById(R.id.txtQuestionThree);
        lblQuestionFour  = (TextView) findViewById(R.id.lblQuestionFour);
        txtQuestionFour  = (EditText) findViewById(R.id.txtQuestionFour);

        txtTitle.setText("PRODUCT VENDOR");
        txtTitle.setTypeface(fontUbuntuB);
        imgLogoRight.setVisibility(View.VISIBLE);
        txtTitle.setTypeface(fontUbuntuB);
        lblSave.setTypeface(fontUbuntuB);
        lblQuestionOne.setTypeface(fontUbuntuL);
        txtQuestionOne.setTypeface(fontUbuntuL);
        lblQuestionTwo.setTypeface(fontUbuntuL);
        txtQuestionTwo.setTypeface(fontUbuntuL);
        lblQuestionThree.setTypeface(fontUbuntuL);
        txtQuestionThree.setTypeface(fontUbuntuL);
        lblQuestionFour.setTypeface(fontUbuntuL);
        txtQuestionFour.setTypeface(fontUbuntuL);

        displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        ViewGroup.LayoutParams paramsHeader  = imgHeader.getLayoutParams();
        paramsHeader.height = (int) (0.05*displaymetrics.heightPixels);

        ViewGroup.LayoutParams paramsSave  = lblSave.getLayoutParams();
        paramsSave.height = (int) (0.07*displaymetrics.heightPixels);

        lblSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                Intent intent = new Intent(AddProductThreeActivity.this, AddProductFourActivity.class);
                intent.putExtra("ProductBarcode", bundle.getString("ProductBarcode"));
                intent.putExtra("JumlahBarang", bundle.getString("JumlahBarang"));
                intent.putExtra("ProductSerialNumber", bundle.getString("ProductSerialNumber"));
                intent.putExtra("ProductSerialNumber2", bundle.getString("ProductSerialNumber2"));
                intent.putExtra("ProductSerialNumber3", bundle.getString("ProductSerialNumber3"));
                intent.putExtra("ProductSerialNumber4", bundle.getString("ProductSerialNumber4"));
                intent.putExtra("ProductSerialNumber5", bundle.getString("ProductSerialNumber5"));
                intent.putExtra("Merk", bundle.getString("Merk"));
                intent.putExtra("Kategori", bundle.getString("Kategori"));
                intent.putExtra("JenisProduk", bundle.getString("JenisProduk"));
                intent.putExtra("PeriodeGaransi", bundle.getString("PeriodeGaransi"));
                intent.putExtra("TanggalPembelian", bundle.getString("TanggalPembelian"));
                intent.putExtra("TempatPembelian", bundle.getString("TempatPembelian"));
                intent.putExtra("Currency", bundle.getString("Currency"));
                intent.putExtra("HargaProduk", bundle.getString("HargaProduk"));
                intent.putExtra("KodeTransaksi", bundle.getString("KodeTransaksi"));
                intent.putExtra("E-commerce", bundle.getBoolean("E-commerce"));
                intent.putExtra("mTakeInvoicePath", bundle.getString("mTakeInvoicePath"));
                intent.putExtra("mTakeProductPath", bundle.getString("mTakeProductPath"));
                intent.putExtra("mTakeWarrantyPath", bundle.getString("mTakeWarrantyPath"));
                intent.putExtra("answer_custom1", txtQuestionOne.getText().toString());
                intent.putExtra("answer_custom2", txtQuestionTwo.getText().toString());
                intent.putExtra("answer_custom3", txtQuestionThree.getText().toString());
                intent.putExtra("answer_custom4", txtQuestionFour.getText().toString());
                startActivity(intent);
            }
        });

        pDialog = new ProgressDialog(AddProductThreeActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);

        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
            getQuestion(getIntent().getExtras().getString("Kategori"));
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            Referensi.showAlertDialog(AddProductThreeActivity.this, "No Internet Connection", "You don't have internet connection.", false);
        }
    }

    public void getQuestion(String strCategori) {
        pDialog.show();
        String url = Referensi.url+"/getSubCategori.php?id_parent='"+strCategori+"'";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getJSONArray("categories").length()!=0) {
                        lblQuestionOne.setText(response.getJSONArray("categories").optJSONObject(0).optString("custom_filed1"));
                        lblQuestionTwo.setText(response.getJSONArray("categories").optJSONObject(0).optString("custom_filed2"));
                        lblQuestionThree.setText(response.getJSONArray("categories").optJSONObject(0).optString("custom_filed3"));
                        lblQuestionFour.setText(response.getJSONArray("categories").optJSONObject(0).optString("custom_filed4"));
                    } else {
                        Toast.makeText(AddProductThreeActivity.this, "No Question", Toast.LENGTH_SHORT).show();
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
