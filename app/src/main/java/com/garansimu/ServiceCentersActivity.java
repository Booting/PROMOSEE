package com.garansimu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.garansimu.Utils.ConnectionDetector;
import com.garansimu.Utils.FontCache;
import com.garansimu.Utils.GoogleMapV2Direction;
import com.garansimu.Utils.Referensi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ServiceCentersActivity extends FragmentActivity {
    private Typeface fontUbuntuL, fontUbuntuB;
    private TextView txtTitle, txtBack, lblPlaceName, lblAdress, lblDay, lblTime, lblCallUs, lblDirection;
    private ImageView imgLogo, imgLeft;
    private EditText txtSearch;
    private GoogleMap mMap;
    private ArrayList<Marker> markers = new ArrayList<>();
    private Double newLatitude, newLongitude;
    private JSONArray str_login  = null;
    private SupportMapFragment fragment;
    private GoogleMapV2Direction md;
    private LinearLayout linCall, linDirection;
    private String strBrand, strTelpNumber="";
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
        setContentView(R.layout.activity_service_centers);

        // creating connection detector class instance
        cd           = new ConnectionDetector(getApplicationContext());
        queue    	 = Volley.newRequestQueue(this);
        fontUbuntuL  = FontCache.get(this, "Ubuntu-L");
        fontUbuntuB  = FontCache.get(this, "Ubuntu-B");
        txtTitle     = (TextView) findViewById(R.id.txtTitle);
        txtTitle     = (TextView) findViewById(R.id.txtTitle);
        txtBack      = (TextView) findViewById(R.id.txtBack);
        imgLogo      = (ImageView) findViewById(R.id.imgLogo);
        imgLeft      = (ImageView) findViewById(R.id.imgLeft);
        txtSearch    = (EditText) findViewById(R.id.txtSearch);
        fragment 	 = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        mMap 	     = fragment.getMap();
        lblPlaceName = (TextView) findViewById(R.id.lblPlaceName);
        lblAdress    = (TextView) findViewById(R.id.lblAdress);
        lblDay       = (TextView) findViewById(R.id.lblDay);
        lblTime      = (TextView) findViewById(R.id.lblTime);
        lblCallUs    = (TextView) findViewById(R.id.lblCallUs);
        lblDirection = (TextView) findViewById(R.id.lblDirection);
        linCall      = (LinearLayout) findViewById(R.id.linCall);
        linDirection = (LinearLayout) findViewById(R.id.linDirection);
        strBrand     = getIntent().getStringExtra("brand");

        imgLeft.setImageResource(R.drawable.dots_vertical);
        imgLeft.setVisibility(View.VISIBLE);
        imgLogo.setVisibility(View.GONE);
        txtBack.setVisibility(View.VISIBLE);
        txtTitle.setTypeface(fontUbuntuB);
        txtTitle.setText("SERVICE CENTERS");
        txtBack.setTypeface(fontUbuntuL);

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtSearch.setTypeface(fontUbuntuL);
        lblPlaceName.setTypeface(fontUbuntuB);
        lblAdress.setTypeface(fontUbuntuL);
        lblDay.setTypeface(fontUbuntuB);
        lblTime.setTypeface(fontUbuntuL);
        lblCallUs.setTypeface(fontUbuntuB);
        lblDirection.setTypeface(fontUbuntuB);

        lblCallUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strTelpNumber.equalsIgnoreCase("")) {
                    Toast.makeText(getBaseContext(), "No. Telp is Required!", Toast.LENGTH_SHORT).show();
                } else {
                    String uri = "tel:" + strTelpNumber.replace("-","").replace(" ","");
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }
            }
        });

        pDialog = new ProgressDialog(ServiceCentersActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);

        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
            getServiceCenters();
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            Referensi.showAlertDialog(ServiceCentersActivity.this, "No Internet Connection", "You don't have internet connection.", false);
        }
    }

    public void getServiceCenters() {
        pDialog.show();
        String url = Referensi.url+"/getServiceCenters.php?brand='"+strBrand+"'";
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length()!=0) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(0);
                        lblPlaceName.setText(jsonObject.getString("nama_servicecenter"));
                        lblAdress.setText(jsonObject.getString("alamat")+", "+jsonObject.getString("kota")+", "+jsonObject.getString("kode_pos"));

                        if (!jsonObject.isNull("telp") || jsonObject.getString("telp").equalsIgnoreCase("")) {
                            strTelpNumber = jsonObject.getString("telp");
                        } else {
                            if (!jsonObject.isNull("hp") || jsonObject.getString("hp").equalsIgnoreCase("")) {
                                strTelpNumber = jsonObject.getString("hp");
                            }
                        }

                        /*** GET LATITUDE LONGITUDE ***/
                        try {
                            if (jsonObject.getString("loglat").contains(",")) {
                                String[] splitLatLong = jsonObject.getString("loglat").split(", ");
                                if (splitLatLong.length == 2) {
                                    initializeMap(Double.parseDouble(splitLatLong[0]), Double.parseDouble(splitLatLong[1]), jsonObject.getString("nama_servicecenter"));
                                } else {
                                    Toast.makeText(ServiceCentersActivity.this, "No location of Service Centers!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ServiceCentersActivity.this, "No location of Service Centers!", Toast.LENGTH_SHORT).show();
                            }
                        } catch(Exception e) {
                            Toast.makeText(ServiceCentersActivity.this, "No location of Service Centers!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ServiceCentersActivity.this, "No data Service Centers!", Toast.LENGTH_SHORT).show();
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

    private void initializeMap(double lat1, double lng1, String strNamaServiceCenters) {
        if (mMap!=null) {
            // Move the camera instantly to hamburg with a zoom of 15.
            LatLng position = new LatLng(lat1, lng1);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat1, lng1)).title(strNamaServiceCenters).icon(BitmapDescriptorFactory.fromResource(R.drawable.apartment)));
        } else {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.container)).getMap();
            Toast.makeText(getApplicationContext(), "MAP NULL", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
