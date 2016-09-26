package com.garansimu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.viewpagerindicator.UnderlinePageIndicator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ProductDetailActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager viewPager;
    private UnderlinePageIndicator underlinePageIndicator;
    private Button btnWarrantyDetails, btnSupport;
    private Typeface fontUbuntuL, fontUbuntuB;
    private ArrayList<String> arrMenu = new ArrayList<String>();
    private static ProductDetailActivity activityInstance;
    public static ProductDetailActivity getInstance() {
        return activityInstance;
    }
    private TextView txtTitle, txtBack;
    private ImageView imgSearch, imgLogo, imgLeft;
    private String strId, strNamaItem, strDistributor, strBrand, strSerialNumber, strPurchaseDate, strNamaToko, strPrice,
            strExpiredWarranty, strPhotoProduct, strPhotoWarrantyCard, strKategori, strNotes;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_product_detail);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // creating connection detector class instance
        cd               = new ConnectionDetector(getApplicationContext());
        queue    	     = Volley.newRequestQueue(this);
        garansimuPref 	 = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        activityInstance = this;
        fontUbuntuL 	 = FontCache.get(ProductDetailActivity.this, "Ubuntu-L");
        fontUbuntuB 	 = FontCache.get(ProductDetailActivity.this, "Ubuntu-B");
        txtTitle         = (TextView) findViewById(R.id.txtTitle);
        txtBack          = (TextView) findViewById(R.id.txtBack);
        imgSearch        = (ImageView) findViewById(R.id.imgSearch);
        imgLogo          = (ImageView) findViewById(R.id.imgLogo);
        imgLeft          = (ImageView) findViewById(R.id.imgLeft);

        // GET BUNDLE
        Bundle bundle        = getIntent().getExtras();
        strId                = bundle.getString("id");
        strNamaItem          = bundle.getString("nama_item");
        strDistributor       = bundle.getString("distributor");
        strBrand             = bundle.getString("brand");
        strSerialNumber      = bundle.getString("serial_number");
        strPurchaseDate      = bundle.getString("purchase_date");
        strNamaToko          = bundle.getString("nama_toko");
        strPrice             = bundle.getString("price");
        strExpiredWarranty   = bundle.getString("expired_warranty");
        strPhotoProduct      = bundle.getString("photo_product");
        strPhotoWarrantyCard = bundle.getString("photo_warranty_card");
        strKategori          = bundle.getString("kategori");
        strNotes             = bundle.getString("notes");

        imgLeft.setImageResource(R.drawable.dots_vertical);
        imgLeft.setVisibility(View.VISIBLE);
        imgLogo.setVisibility(View.GONE);
        txtBack.setVisibility(View.VISIBLE);
        txtTitle.setTypeface(fontUbuntuB);
        txtTitle.setText(strNamaItem);
        txtBack.setTypeface(fontUbuntuL);

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        arrMenu.add("{\"cat_name\":\"Warranty Details\"}");
        arrMenu.add("{\"cat_name\":\"Support\"}");

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setTag("pager");
        viewPager.setOffscreenPageLimit(6);
        viewPager.setAdapter(new EventPagerAdapter(getSupportFragmentManager()));

        btnWarrantyDetails = (Button) findViewById(R.id.btnWarrantyDetails);
        btnWarrantyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == 0) {
                    btnWarrantyDetails.setBackgroundColor(getResources().getColor(R.color.colorBgSelected));
                } else {
                    viewPager.setCurrentItem(0);
                }
            }
        });
        viewPager.setCurrentItem(0);

        btnSupport = (Button) findViewById(R.id.btnSupport);
        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        btnWarrantyDetails.setTypeface(fontUbuntuB);
        btnSupport.setTypeface(fontUbuntuB);

        underlinePageIndicator = (UnderlinePageIndicator) findViewById(R.id.underlinePageIndicator);
        underlinePageIndicator.setFades(false);
        underlinePageIndicator.setSelectedColor(getResources().getColor(R.color.colorSelected));
        underlinePageIndicator.setViewPager(viewPager);

        underlinePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                if (position==0) {
                    btnSupport.setBackgroundColor(getResources().getColor(R.color.colorBgUnselected));
                    btnSupport.setTextColor(getResources().getColor(R.color.colorUnselected));
                    btnWarrantyDetails.setBackgroundColor(getResources().getColor(R.color.colorBgSelected));
                    btnWarrantyDetails.setTextColor(getResources().getColor(R.color.colorSelected));
                } else if (position==1) {
                    btnSupport.setBackgroundColor(getResources().getColor(R.color.colorBgSelected));
                    btnSupport.setTextColor(getResources().getColor(R.color.colorSelected));
                    btnWarrantyDetails.setBackgroundColor(getResources().getColor(R.color.colorBgUnselected));
                    btnWarrantyDetails.setTextColor(getResources().getColor(R.color.colorUnselected));
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) { }
        });

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
                Intent intent = new Intent(ProductDetailActivity.this, MyProductActivity.class);
                startActivity(intent);
            }
        });

        linOurBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, OurBrandActivity.class);
                startActivity(intent);
            }
        });
        linContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, ContactUsActivity.class);
                startActivity(intent);
            }
        });
        linRegisterAsPrinciple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, RegisterAsBrandPrincipalActivity.class);
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
                Intent intent = new Intent(ProductDetailActivity.this, ProfileEditActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        linUpgradeToSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, SubscribeActivity.class);
                startActivity(intent);
            }
        });

        pDialog = new ProgressDialog(ProductDetailActivity.this);
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
            Referensi.showAlertDialog(ProductDetailActivity.this, "No Internet Connection", "You don't have internet connection.", false);
        }
    }

    public class EventPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {
        public EventPagerAdapter(FragmentManager fm) { super(fm); }
        @Override
        public Fragment getItem(int position) {
            JSONObject jObject = null;
            try {
                jObject = new JSONObject(arrMenu.get(position));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //root
            if (jObject.optString("cat_name").equalsIgnoreCase("Warranty Details")) {
                return WarrantyDetailsFragment.newInstance(strId, strNamaItem, strDistributor, strSerialNumber, strPurchaseDate,
                        strNamaToko, strPrice, strExpiredWarranty, strPhotoProduct, strPhotoWarrantyCard, strNotes);
            } else {
                return SupportFragment.newInstance(strId, strNamaItem, strKategori, strPhotoProduct, strBrand);
            }
        }
        @Override
        public int getCount() { return 2; }
        @Override
        public CharSequence getPageTitle(int position) { return ""; }
        @Override
        public void onPageScrollStateChanged(int arg0) { }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) { }
        @Override
        public void onPageSelected(int arg0) {}
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