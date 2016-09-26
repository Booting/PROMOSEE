package com.garansimu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.garansimu.Utils.FontCache;
import com.garansimu.Utils.OnSwipeTouchListener;
import com.garansimu.Utils.Referensi;

public class LandingpageActivity extends Activity {
    private Typeface fontUbuntuL, fontUbuntuB;
    private TextView txtOne, txtTwo, txtThree, txtFour, txtRegisterAsUser, txtRegisterAsPrinciple;
    private ImageView imgLandingPage, imgDoubleArrow, imgDotOne, imgDotTwo, imgDotThree, imgDotFour, imgDotFive;
    private int intPosition = 0;
    private LinearLayout linRegisterAsUser, linRegisterAsPrinciple, linParent;
    private SharedPreferences garansimuPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_landing_page);

        garansimuPref 	       = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        fontUbuntuL            = FontCache.get(this, "Ubuntu-L");
        fontUbuntuB            = FontCache.get(this, "Ubuntu-B");
        txtOne                 = (TextView) findViewById(R.id.txtOne);
        txtTwo                 = (TextView) findViewById(R.id.txtTwo);
        txtThree               = (TextView) findViewById(R.id.txtThree);
        txtFour                = (TextView) findViewById(R.id.txtFour);
        imgLandingPage         = (ImageView) findViewById(R.id.imgLandingPage);
        imgDoubleArrow         = (ImageView) findViewById(R.id.imgDoubleArrow);
        imgDotOne              = (ImageView) findViewById(R.id.imgDotOne);
        imgDotTwo              = (ImageView) findViewById(R.id.imgDotTwo);
        imgDotThree            = (ImageView) findViewById(R.id.imgDotThree);
        imgDotFour             = (ImageView) findViewById(R.id.imgDotFour);
        imgDotFive             = (ImageView) findViewById(R.id.imgDotFive);
        linRegisterAsUser      = (LinearLayout) findViewById(R.id.linRegisterAsUser);
        linRegisterAsPrinciple = (LinearLayout) findViewById(R.id.linRegisterAsPrinciple);
        txtRegisterAsUser      = (TextView) findViewById(R.id.txtRegisterAsUser);
        txtRegisterAsPrinciple = (TextView) findViewById(R.id.txtRegisterAsPrinciple);
        linParent              = (LinearLayout) findViewById(R.id.linParent);

        txtOne.setTypeface(fontUbuntuB);
        txtTwo.setTypeface(fontUbuntuL);
        txtThree.setTypeface(fontUbuntuL);
        txtFour.setTypeface(fontUbuntuL);
        txtRegisterAsUser.setTypeface(fontUbuntuL);
        txtRegisterAsPrinciple.setTypeface(fontUbuntuL);

        imgDoubleArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intPosition = intPosition + 1;
                onSwipe();
            }
        });

        txtThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingpageActivity.this, LoginRegisterActivity.class));
                finish();
            }
        });

        txtFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingpageActivity.this, LoginRegisterActivity.class));
                finish();
            }
        });

        if (garansimuPref.contains("IsFirst")) {
            if (!garansimuPref.getString("Id", "").equals("")) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            } else {
                startActivity(new Intent(getApplicationContext(), LoginRegisterActivity.class));
                finish();
            }
        }

        linParent.setOnTouchListener(new OnSwipeTouchListener(LandingpageActivity.this) {
            public void onSwipeTop() {}
            public void onSwipeRight() {
                intPosition = intPosition - 1;
                onSwipe();
            }
            public void onSwipeLeft() {
                intPosition = intPosition + 1;
                onSwipe();
            }
            public void onSwipeBottom() {}

        });
    }

    public void onSwipe() {
        if (intPosition ==0) {
            txtOne.setText("WELCOME!");
            txtTwo.setText("Garansimu will manage and store your warranties securely");
            imgLandingPage.setImageResource(R.drawable.landingpage_one);
            imgDotOne.setImageResource(R.drawable.blak_circle);
            imgDotTwo.setImageResource(R.drawable.gray_circle);
            imgDotThree.setImageResource(R.drawable.gray_circle);
            imgDotFour.setImageResource(R.drawable.gray_circle);
            imgDotFive.setImageResource(R.drawable.gray_circle);
        } else if (intPosition == 1) {
            txtOne.setText("PHOTO");
            txtTwo.setText("Take photo product and warranty card to enter your product's detail");
            imgLandingPage.setImageResource(R.drawable.landingpage_two);
            imgDotOne.setImageResource(R.drawable.gray_circle);
            imgDotTwo.setImageResource(R.drawable.blak_circle);
            imgDotThree.setImageResource(R.drawable.gray_circle);
            imgDotFour.setImageResource(R.drawable.gray_circle);
            imgDotFive.setImageResource(R.drawable.gray_circle);
        } else if (intPosition == 2) {
            txtOne.setText("VERIFY");
            txtTwo.setText("Add a proof of purchase to activate the product's");
            imgLandingPage.setImageResource(R.drawable.landingpage_three);
            imgDotOne.setImageResource(R.drawable.gray_circle);
            imgDotTwo.setImageResource(R.drawable.gray_circle);
            imgDotThree.setImageResource(R.drawable.blak_circle);
            imgDotFour.setImageResource(R.drawable.gray_circle);
            imgDotFive.setImageResource(R.drawable.gray_circle);
        } else if (intPosition == 3) {
            txtOne.setText("ACTIVATE");
            txtTwo.setText("Your eWarranty is now activated and securely stored in your account");
            imgLandingPage.setImageResource(R.drawable.landingpage_four);
            imgDotOne.setImageResource(R.drawable.gray_circle);
            imgDotTwo.setImageResource(R.drawable.gray_circle);
            imgDotThree.setImageResource(R.drawable.gray_circle);
            imgDotFour.setImageResource(R.drawable.blak_circle);
            imgDotFive.setImageResource(R.drawable.gray_circle);
        } else if (intPosition == 4) {
            txtOne.setText("MANAGE");
            txtTwo.setText("Access full support, manuals contact info and more");
            imgLandingPage.setImageResource(R.drawable.landingpage_five);
            imgDotOne.setImageResource(R.drawable.gray_circle);
            imgDotTwo.setImageResource(R.drawable.gray_circle);
            imgDotThree.setImageResource(R.drawable.gray_circle);
            imgDotFour.setImageResource(R.drawable.gray_circle);
            imgDotFive.setImageResource(R.drawable.blak_circle);
            txtThree.setVisibility(View.INVISIBLE);
            imgDoubleArrow.setVisibility(View.GONE);
            txtFour.setVisibility(View.VISIBLE);
            linRegisterAsUser.setVisibility(View.VISIBLE);
            linRegisterAsPrinciple.setVisibility(View.GONE);
        } else if (intPosition == 5) {
            startActivity(new Intent(LandingpageActivity.this, LoginRegisterActivity.class));
            finish();
        }
    }

}
