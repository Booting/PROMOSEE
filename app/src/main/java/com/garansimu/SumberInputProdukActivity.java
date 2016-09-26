package com.garansimu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.garansimu.Utils.FontCache;

public class SumberInputProdukActivity extends Activity {
    private Typeface fontLatoReguler, fontLatoHeavy;
    private TextView txtTitle, lblOne, txtAddDataOne, btnOne, lblTwo, txtAddDataTwo, btnTwo, lblThree, txtThree;
    private ImageView imgLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_sumber_input_produk);

        fontLatoReguler = FontCache.get(this, "Lato-Regular");
        fontLatoHeavy 	= FontCache.get(this, "Lato-Heavy");
        txtTitle        = (TextView) findViewById(R.id.txtTitle);
        imgLeft         = (ImageView) findViewById(R.id.imgLeft);
        lblOne          = (TextView) findViewById(R.id.lblOne);
        txtAddDataOne   = (TextView) findViewById(R.id.txtAddDataOne);
        btnOne          = (TextView) findViewById(R.id.btnOne);
        lblTwo          = (TextView) findViewById(R.id.lblTwo);
        txtAddDataTwo   = (TextView) findViewById(R.id.txtAddDataTwo);
        btnTwo          = (TextView) findViewById(R.id.btnTwo);
        lblThree        = (TextView) findViewById(R.id.lblThree);
        txtThree        = (TextView) findViewById(R.id.txtThree);

        txtTitle.setText("ADD DATA NOW");
        txtTitle.setTypeface(fontLatoHeavy);
        lblOne.setTypeface(fontLatoHeavy);
        txtAddDataOne.setTypeface(fontLatoHeavy);
        btnOne.setTypeface(fontLatoReguler);
        lblTwo.setTypeface(fontLatoHeavy);
        txtAddDataTwo.setTypeface(fontLatoHeavy);
        btnTwo.setTypeface(fontLatoReguler);
        lblThree.setTypeface(fontLatoReguler);
        txtThree.setTypeface(fontLatoHeavy);
        txtThree.setText(Html.fromHtml("<u>KLIK DISINI</u>"));

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void btnAddProductClick(View v) {
        startActivity(new Intent(SumberInputProdukActivity.this, AddProductOneActivity.class));
    }

    public void btnSubscribeClick(View v) {
        startActivity(new Intent(SumberInputProdukActivity.this, SubscribeActivity.class));
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
