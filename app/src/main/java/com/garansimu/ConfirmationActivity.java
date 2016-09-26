package com.garansimu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.etsy.android.grid.StaggeredGridView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConfirmationActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Typeface fontUbuntuL, fontUbuntuB;
    private TextView txtTitle, txtBack, lblTitleOne;
    private ImageView imgLogo, imgLeft;
    private RequestQueue queue;
    private ProgressDialog pDialog;
    private ArrayList<Kategori> categoriList;
    private StaggeredGridView itemList;
    private OurBrandAdapter ourBrandAdapter;
    private SharedPreferences garansimuPref;
    private LinearLayout linMyProduct, linOurBrand, linContactUs, linRegisterAsPrinciple, linLogOut, linHome, linMyProfile, linUpgradeToSubscribe;
    private ImageView imgEditLeft;
    private CircleImageView imgProfileLeft;
    private TextView txtNameLeft, txtCountProductLeft;

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
        setContentView(R.layout.activity_confirmation);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // creating connection detector class instance
        cd            = new ConnectionDetector(getApplicationContext());
        garansimuPref = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        queue    	  = Volley.newRequestQueue(this);
        fontUbuntuL   = FontCache.get(this, "Ubuntu-L");
        fontUbuntuB   = FontCache.get(this, "Ubuntu-B");
        txtTitle      = (TextView) findViewById(R.id.txtTitle);
        txtTitle      = (TextView) findViewById(R.id.txtTitle);
        txtBack       = (TextView) findViewById(R.id.txtBack);
        imgLogo       = (ImageView) findViewById(R.id.imgLogo);
        imgLeft       = (ImageView) findViewById(R.id.imgLeft);
        lblTitleOne   = (TextView) findViewById(R.id.lblTitleOne);
        itemList      = (StaggeredGridView) findViewById(R.id.itemList);

        imgLeft.setImageResource(R.drawable.dots_vertical);
        imgLeft.setVisibility(View.VISIBLE);
        imgLogo.setVisibility(View.GONE);
        txtBack.setVisibility(View.VISIBLE);
        txtTitle.setTypeface(fontUbuntuB);
        txtTitle.setText("PROMOSEE");
        txtBack.setTypeface(fontUbuntuL);

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lblTitleOne.setTypeface(fontUbuntuB);

        pDialog = new ProgressDialog(ConfirmationActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);

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

        /*txtNameLeft.setText(garansimuPref.getString("Name", ""));

        if (garansimuPref.getString("Image", "").contains("jpg") || garansimuPref.getString("Image", "").contains("png")) {
            Glide.with(this).load(Referensi.url_img2 + garansimuPref.getString("Image", "")).placeholder(R.drawable.img_loader).dontAnimate().into(imgProfileLeft);
        } else {
            Glide.with(this).load("https://graph.facebook.com/" + garansimuPref.getString("Image", "") + "/picture?type=large").placeholder(R.drawable.img_loader).dontAnimate().into(imgProfileLeft);
        }

        linMyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OurBrandActivity.this, MyProductActivity.class);
                startActivity(intent);
            }
        });

        linOurBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OurBrandActivity.this, OurBrandActivity.class);
                startActivity(intent);
            }
        });
        linContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OurBrandActivity.this, ContactUsActivity.class);
                startActivity(intent);
            }
        });
        linRegisterAsPrinciple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OurBrandActivity.this, RegisterAsBrandPrincipalActivity.class);
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
                Intent intent = new Intent(OurBrandActivity.this, ProfileEditActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        linUpgradeToSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OurBrandActivity.this, SubscribeActivity.class);
                startActivity(intent);
            }
        });

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
            getCategori();
            getUserProduk(garansimuPref.getString("Id", ""));
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            Referensi.showAlertDialog(OurBrandActivity.this, "No Internet Connection", "You don't have internet connection.", false);
        }*/
    }

    public void getOurBrand(String strCategory) {
        pDialog.show();
        String url;
        if (strCategory.equalsIgnoreCase("All")) {
            url = Referensi.url + "/getOurBrand.php?kategori=" + strCategory;
        } else {
            url = Referensi.url + "/getOurBrand.php?kategori='" + strCategory + "'";
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getJSONArray("categories").length()!=0) {
                        ourBrandAdapter = new OurBrandAdapter(ConfirmationActivity.this, response.getJSONArray("categories"));
                        itemList.setAdapter(ourBrandAdapter);
                    } else {
                        JSONArray jsonArray = new JSONArray();
                        ourBrandAdapter = new OurBrandAdapter(ConfirmationActivity.this, jsonArray);
                        itemList.setAdapter(ourBrandAdapter);
                        Toast.makeText(ConfirmationActivity.this, "No Our Brand", Toast.LENGTH_SHORT).show();
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
                JSONArray jsonArray = new JSONArray();
                ourBrandAdapter = new OurBrandAdapter(ConfirmationActivity.this, jsonArray);
                itemList.setAdapter(ourBrandAdapter);
                Toast.makeText(ConfirmationActivity.this, "No Our Brand", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsObjRequest);
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
