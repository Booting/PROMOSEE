package com.garansimu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.garansimu.Utils.FontCache;

public class SubscribeActivity extends Activity {
    private Typeface fontLatoReguler, fontLatoHeavy;
    private TextView txtTitle, lblOne, txtOne, lblTwo, txtTwo, txtTwoDetail, btnTwo, lblThree, txtThree, txtThreeDetail,
            btnThree, lblFour, txtFour, btnFour;
    private ImageView imgLeft, imgOne, imgTwo, imgThree, imgFour;
    private RelativeLayout linSubscribeTwo, linSubscribeThree, linSubscribeFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_subscribe);

        fontLatoReguler = FontCache.get(this, "Lato-Regular");
        fontLatoHeavy 	= FontCache.get(this, "Lato-Heavy");
        txtTitle        = (TextView) findViewById(R.id.txtTitle);
        imgLeft         = (ImageView) findViewById(R.id.imgLeft);
        lblOne          = (TextView) findViewById(R.id.lblOne);
        txtOne          = (TextView) findViewById(R.id.txtOne);
        lblTwo          = (TextView) findViewById(R.id.lblTwo);
        txtTwo          = (TextView) findViewById(R.id.txtTwo);
        txtTwoDetail    = (TextView) findViewById(R.id.txtTwoDetail);
        btnTwo          = (TextView) findViewById(R.id.btnTwo);
        lblThree        = (TextView) findViewById(R.id.lblThree);
        txtThree        = (TextView) findViewById(R.id.txtThree);
        txtThreeDetail  = (TextView) findViewById(R.id.txtThreeDetail);
        btnThree        = (TextView) findViewById(R.id.btnThree);
        lblFour         = (TextView) findViewById(R.id.lblFour);
        txtFour         = (TextView) findViewById(R.id.txtFour);
        btnFour         = (TextView) findViewById(R.id.btnFour);
        imgOne          = (ImageView) findViewById(R.id.imgOne);
        imgTwo          = (ImageView) findViewById(R.id.imgTwo);
        imgThree        = (ImageView) findViewById(R.id.imgThree);
        imgFour         = (ImageView) findViewById(R.id.imgFour);
        linSubscribeTwo = (RelativeLayout) findViewById(R.id.linSubscribeTwo);
        linSubscribeThree = (RelativeLayout) findViewById(R.id.linSubscribeThree);
        linSubscribeFour  = (RelativeLayout) findViewById(R.id.linSubscribeFour);

        txtTitle.setText("SUBSCRIBE");
        txtTitle.setTypeface(fontLatoHeavy);
        lblOne.setTypeface(fontLatoHeavy);
        txtOne.setTypeface(fontLatoHeavy);
        lblTwo.setTypeface(fontLatoHeavy);
        txtTwo.setTypeface(fontLatoHeavy);
        txtTwoDetail.setTypeface(fontLatoReguler);
        btnTwo.setTypeface(fontLatoHeavy);
        lblThree.setTypeface(fontLatoHeavy);
        txtThree.setTypeface(fontLatoHeavy);
        txtThreeDetail.setTypeface(fontLatoReguler);
        btnThree.setTypeface(fontLatoHeavy);
        lblFour.setTypeface(fontLatoHeavy);
        txtFour.setTypeface(fontLatoHeavy);
        btnFour.setTypeface(fontLatoHeavy);

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linSubscribeTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgOne.setVisibility(View.GONE);
                imgTwo.setVisibility(View.VISIBLE);
                imgThree.setVisibility(View.GONE);
                imgFour.setVisibility(View.GONE);
                startActivity(new Intent(SubscribeActivity.this, SubscribeDetailActivity.class).putExtra("one", "PAKET BASIC")
                        .putExtra("two", "20 produk garansimu")
                        .putExtra("three", "Rp 39.000/tahun")
                        .putExtra("four", "2"));
            }
        });
        linSubscribeThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgOne.setVisibility(View.GONE);
                imgTwo.setVisibility(View.GONE);
                imgThree.setVisibility(View.VISIBLE);
                imgFour.setVisibility(View.GONE);
                startActivity(new Intent(SubscribeActivity.this, SubscribeDetailActivity.class).putExtra("one", "PAKET PREMIUM")
                        .putExtra("two", "50 produk garansimu")
                        .putExtra("three", "Rp 70.000/tahun")
                        .putExtra("four", "3"));
            }
        });
        linSubscribeFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgOne.setVisibility(View.GONE);
                imgTwo.setVisibility(View.GONE);
                imgThree.setVisibility(View.GONE);
                imgFour.setVisibility(View.VISIBLE);
            }
        });
    }

    public void btnSubscribeOneClick(View v) {
        imgOne.setVisibility(View.VISIBLE);
        imgTwo.setVisibility(View.GONE);
        imgThree.setVisibility(View.GONE);
        imgFour.setVisibility(View.GONE);
        startActivity(new Intent(SubscribeActivity.this, SubscribeDetailActivity.class).putExtra("one", "Free for 3 months")
            .putExtra("two", "5 produk garansimu")
            .putExtra("three", "Free")
            .putExtra("four", ""));
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
