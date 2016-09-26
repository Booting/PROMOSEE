package com.garansimu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
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
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.garansimu.Utils.CircleImageView;
import com.garansimu.Utils.ConnectionDetector;
import com.garansimu.Utils.FontCache;
import com.garansimu.Utils.Referensi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;

public class ContactUsActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Typeface fontLatoReguler, fontLatoHeavy;
    private TextView txtTitle, txtBack, lblAdress, lblPhoneOne, lblPhoneTwo, lblCallUs;
    private ImageView imgLogo, imgLeft;
    private EditText txtSearch;
    private GoogleMap mMap;
    private SupportMapFragment fragment;
    private LinearLayout linCall;
    private ProgressDialog pDialog;
    private SharedPreferences garansimuPref;
    private LinearLayout linMyProduct, linOurBrand, linContactUs, linRegisterAsPrinciple, linLogOut, linHome, linMyProfile, linUpgradeToSubscribe;
    private ImageView imgEditLeft;
    private CircleImageView imgProfileLeft;
    private TextView txtNameLeft, txtCountProductLeft;
    private RequestQueue queue;

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
        setContentView(R.layout.activity_contact_us);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // creating connection detector class instance
        cd              = new ConnectionDetector(getApplicationContext());
        queue    	    = Volley.newRequestQueue(this);
        garansimuPref 	= getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        fontLatoReguler = FontCache.get(this, "Lato-Regular");
        fontLatoHeavy 	= FontCache.get(this, "Lato-Heavy");
        txtTitle        = (TextView) findViewById(R.id.txtTitle);
        txtBack         = (TextView) findViewById(R.id.txtBack);
        imgLogo         = (ImageView) findViewById(R.id.imgLogo);
        imgLeft         = (ImageView) findViewById(R.id.imgLeft);
        txtSearch       = (EditText) findViewById(R.id.txtSearch);
        fragment 	    = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        mMap 	        = fragment.getMap();
        lblAdress       = (TextView) findViewById(R.id.lblAdress);
        lblPhoneOne     = (TextView) findViewById(R.id.lblPhoneOne);
        lblPhoneTwo     = (TextView) findViewById(R.id.lblPhoneTwo);
        lblCallUs       = (TextView) findViewById(R.id.lblCallUs);
        linCall         = (LinearLayout) findViewById(R.id.linCall);

        imgLeft.setImageResource(R.drawable.dots_vertical);
        imgLeft.setVisibility(View.VISIBLE);
        imgLogo.setVisibility(View.GONE);
        txtBack.setVisibility(View.VISIBLE);
        txtTitle.setTypeface(fontLatoHeavy);
        txtTitle.setText("CONTACT US");
        txtBack.setTypeface(fontLatoReguler);

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtSearch.setTypeface(fontLatoReguler);
        lblAdress.setTypeface(fontLatoReguler);
        lblPhoneOne.setTypeface(fontLatoReguler);
        lblPhoneTwo.setTypeface(fontLatoReguler);
        lblCallUs.setTypeface(fontLatoHeavy);

        linCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (lblPhoneOne.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getBaseContext(), "No. Telp is Required!", Toast.LENGTH_SHORT).show();
                } else {
                    String uri = "tel:" + lblPhoneOne.getText().toString().replace("-","").replace(" ","");
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }
            }
        });

        initializeMap(-6.219767, 106.814511);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        imgProfileLeft         = (CircleImageView) headerView.findViewById(R.id.imgProfileLeft);
        txtNameLeft            = (TextView) headerView.findViewById(R.id.txtNameLeft);
        txtCountProductLeft    = (TextView) headerView.findViewById(R.id.txtCountProductLeft);
        imgEditLeft            = (ImageView) headerView.findViewById(R.id.imgEditLeft);
        linMyProduct           = (LinearLayout) headerView.findViewById(R.id.linMyProduct);
        linOurBrand            = (LinearLayout) headerView.findViewById(R.id.linOurBrand);
        linContactUs           = (LinearLayout) headerView.findViewById(R.id.linContactUs);
        linRegisterAsPrinciple = (LinearLayout) headerView.findViewById(R.id.linRegisterAsPrinciple);
        linLogOut              = (LinearLayout) headerView.findViewById(R.id.linLogOut);
        linHome                = (LinearLayout) headerView.findViewById(R.id.linHome);
        linMyProfile           = (LinearLayout) headerView.findViewById(R.id.linMyProfile);
        linUpgradeToSubscribe  = (LinearLayout) headerView.findViewById(R.id.linUpgradeToSubscribe);

        txtNameLeft.setText(garansimuPref.getString("Name", ""));

        if (garansimuPref.getString("Image", "").contains("jpg") || garansimuPref.getString("Image", "").contains("png")) {
            Glide.with(this).load(Referensi.url_img2 + garansimuPref.getString("Image", "")).placeholder(R.drawable.img_loader).dontAnimate().into(imgProfileLeft);
        } else {
            Glide.with(this).load("https://graph.facebook.com/" + garansimuPref.getString("Image", "") + "/picture?type=large").placeholder(R.drawable.img_loader).dontAnimate().into(imgProfileLeft);
        }

        linMyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactUsActivity.this, MyProductActivity.class);
                startActivity(intent);
            }
        });

        linOurBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactUsActivity.this, OurBrandActivity.class);
                startActivity(intent);
            }
        });
        linContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactUsActivity.this, ContactUsActivity.class);
                startActivity(intent);
            }
        });
        linRegisterAsPrinciple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactUsActivity.this, RegisterAsBrandPrincipalActivity.class);
                startActivity(intent);
            }
        });
        linLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.show();

                disconnectFromFacebook();
                SharedPreferences.Editor editor = garansimuPref.edit();
                editor.putString("Id", "");
                editor.putString("Name", "");
                editor.putString("Phone", "");
                editor.putString("City", "");
                editor.putString("Address", "");
                editor.putString("Image", "");
                editor.putBoolean("IsFirst", false);
                editor.commit();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.dismiss();

                        Intent intent = new Intent(getApplicationContext(), LoginRegisterActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }, 2000);
            }
        });
        linHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        linMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactUsActivity.this, ProfileEditActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        linUpgradeToSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactUsActivity.this, SubscribeActivity.class);
                startActivity(intent);
            }
        });

        pDialog = new ProgressDialog(ContactUsActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
            getUserProduk(garansimuPref.getString("Id", ""));
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            Referensi.showAlertDialog(ContactUsActivity.this, "No Internet Connection", "You don't have internet connection.", false);
        }
    }

    private void initializeMap(double lat1, double lng1) {
        if (mMap!=null) {
            // Move the camera instantly to hamburg with a zoom of 15.
            LatLng position = new LatLng(lat1, lng1);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat1, lng1)).title("GARANSIMU").icon(BitmapDescriptorFactory.fromResource(R.drawable.apartment)));
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

    public void getUserProduk(String strIdCust) {
        pDialog.show();
        String url = Referensi.url+"/getUserProduk.php?id_cust="+strIdCust;
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                txtCountProductLeft.setText(response.length()+" Product");
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

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
            }
        }).executeAsync();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_product) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_send_us_feedback) {

        } else if (id == R.id.nav_how_does_it_work) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
