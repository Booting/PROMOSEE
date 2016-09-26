package com.garansimu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.garansimu.Utils.CircleImageView;
import com.garansimu.Utils.ConnectionDetector;
import com.garansimu.Utils.FontCache;
import com.garansimu.Utils.Referensi;
import org.json.JSONArray;

public class ConfirmationPaymentActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener {
    private Typeface fontUbuntuL, fontUbuntuB;
    private Typeface fontLatoReguler, fontLatoHeavy;
    private TextView txtTitle, txtBack, lblCustomer, txtCustomer, lblPaymentDescription, txtPaket, txtPaketDetail,
            lblPrice, txtPrice, lblAccountName, txtAccountName, lblBank, txtBank, lblAccountNumber, txtAccountNumber,
            lblStatusPayment, txtStatusPayment, txtSubmit;
    private ImageView imgLogo, imgLeft, imgEditLeft;
    private CircleImageView imgProfileLeft;
    private SharedPreferences garansimuPref;
    private RequestQueue queue;
    private ProgressDialog pDialog;
    private TextView txtNameLeft, txtCountProductLeft;
    private LinearLayout linMyProduct, linOurBrand, linContactUs, linRegisterAsPrinciple, linLogOut, linHome, linMyProfile, linUpgradeToSubscribe;

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_confirmation_payment);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fontUbuntuL           = FontCache.get(this, "Lato-Regular");
        fontUbuntuB           = FontCache.get(this, "Lato-Heavy");
        cd                    = new ConnectionDetector(getApplicationContext());
        queue    	          = Volley.newRequestQueue(this);
        garansimuPref 	      = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        fontLatoReguler       = FontCache.get(this, "Lato-Regular");
        fontLatoHeavy 	      = FontCache.get(this, "Lato-Heavy");
        txtTitle              = (TextView) findViewById(R.id.txtTitle);
        txtBack               = (TextView) findViewById(R.id.txtBack);
        imgLogo               = (ImageView) findViewById(R.id.imgLogo);
        imgLeft               = (ImageView) findViewById(R.id.imgLeft);
        lblCustomer           = (TextView) findViewById(R.id.lblCustomer);
        txtCustomer           = (TextView) findViewById(R.id.txtCustomer);
        lblPaymentDescription = (TextView) findViewById(R.id.lblPaymentDescription);
        txtPaket              = (TextView) findViewById(R.id.txtPaket);
        txtPaketDetail        = (TextView) findViewById(R.id.txtPaketDetail);
        lblPrice              = (TextView) findViewById(R.id.lblPrice);
        txtPrice              = (TextView) findViewById(R.id.txtPrice);
        lblAccountName        = (TextView) findViewById(R.id.lblAccountName);
        txtAccountName        = (TextView) findViewById(R.id.txtAccountName);
        lblBank               = (TextView) findViewById(R.id.lblBank);
        txtBank               = (TextView) findViewById(R.id.txtBank);
        lblAccountNumber      = (TextView) findViewById(R.id.lblAccountNumber);
        txtAccountNumber      = (TextView) findViewById(R.id.txtAccountNumber);
        lblStatusPayment      = (TextView) findViewById(R.id.lblStatusPayment);
        txtStatusPayment      = (TextView) findViewById(R.id.txtStatusPayment);
        txtSubmit             = (TextView) findViewById(R.id.txtSubmit);

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

        linMyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmationPaymentActivity.this, MyProductActivity.class);
                startActivity(intent);
            }
        });

        linOurBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmationPaymentActivity.this, OurBrandActivity.class);
                startActivity(intent);
            }
        });
        linContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmationPaymentActivity.this, ContactUsActivity.class);
                startActivity(intent);
            }
        });
        linRegisterAsPrinciple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmationPaymentActivity.this, RegisterAsBrandPrincipalActivity.class);
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
                Intent intent = new Intent(ConfirmationPaymentActivity.this, ProfileEditActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        linUpgradeToSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmationPaymentActivity.this, SubscribeActivity.class);
                startActivity(intent);
            }
        });

        imgLeft.setImageResource(R.drawable.dots_vertical);
        imgLeft.setVisibility(View.VISIBLE);
        imgLogo.setVisibility(View.GONE);
        txtBack.setVisibility(View.VISIBLE);
        txtTitle.setTypeface(fontLatoHeavy);
        txtTitle.setText("CONFIRMATION PAYMENT");
        txtBack.setTypeface(fontLatoReguler);
        txtNameLeft.setTypeface(fontUbuntuB);
        txtCountProductLeft.setTypeface(fontUbuntuL);

        txtNameLeft.setText(garansimuPref.getString("Name", ""));

        if (garansimuPref.getString("Image", "").contains("jpg") || garansimuPref.getString("Image", "").contains("png")) {
            Glide.with(this).load(Referensi.url_img2 + garansimuPref.getString("Image", "")).placeholder(R.drawable.img_loader).dontAnimate().into(imgProfileLeft);
        } else {
            Glide.with(this).load("https://graph.facebook.com/" + garansimuPref.getString("Image", "") + "/picture?type=large").placeholder(R.drawable.img_loader).dontAnimate().into(imgProfileLeft);
        }

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lblCustomer.setTypeface(fontLatoHeavy);
        txtCustomer.setTypeface(fontLatoReguler);
        lblPaymentDescription.setTypeface(fontLatoHeavy);
        txtPaket.setTypeface(fontLatoHeavy);
        txtPaketDetail.setTypeface(fontLatoReguler);
        lblPrice.setTypeface(fontLatoHeavy);
        txtPrice.setTypeface(fontLatoReguler);
        lblAccountName.setTypeface(fontLatoHeavy);
        txtAccountName.setTypeface(fontLatoReguler);
        lblBank.setTypeface(fontLatoHeavy);
        txtBank.setTypeface(fontLatoReguler);
        lblAccountNumber.setTypeface(fontLatoHeavy);
        txtAccountNumber.setTypeface(fontLatoReguler);
        lblStatusPayment.setTypeface(fontLatoHeavy);
        txtStatusPayment.setTypeface(fontLatoReguler);
        txtSubmit.setTypeface(fontLatoHeavy);

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pDialog = new ProgressDialog(ConfirmationPaymentActivity.this);
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
            Referensi.showAlertDialog(ConfirmationPaymentActivity.this, "No Internet Connection", "You don't have internet connection.", false);
        }
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

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
