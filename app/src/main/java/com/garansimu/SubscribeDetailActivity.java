package com.garansimu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.garansimu.Utils.FontCache;

public class SubscribeDetailActivity extends Activity {
    private Typeface fontUbuntuL, fontUbuntuB;
    private TextView lblNumber, lblDeskripsi, txtDeskripsi, lblMenyimpan, txtMenyimpan, lblBerlangganan,
            txtBerlangganan, lblPromo, btnProses;
    private ImageView imgClose, imgBarcode, imgScan;
    private RelativeLayout relProductBarcode;
    private EditText txtProductBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_subscribe_detail);

        fontUbuntuL     = FontCache.get(this, "Ubuntu-L");
        fontUbuntuB     = FontCache.get(this, "Ubuntu-B");
        imgClose        = (ImageView) findViewById(R.id.imgClose);
        lblNumber       = (TextView) findViewById(R.id.lblNumber);
        lblDeskripsi    = (TextView) findViewById(R.id.lblDeskripsi);
        txtDeskripsi    = (TextView) findViewById(R.id.txtDeskripsi);
        lblMenyimpan    = (TextView) findViewById(R.id.lblMenyimpan);
        txtMenyimpan    = (TextView) findViewById(R.id.txtMenyimpan);
        lblBerlangganan = (TextView) findViewById(R.id.lblBerlangganan);
        txtBerlangganan = (TextView) findViewById(R.id.txtBerlangganan);
        lblPromo        = (TextView) findViewById(R.id.lblPromo);
        imgBarcode      = (ImageView) findViewById(R.id.imgBarcode);
        btnProses       = (TextView) findViewById(R.id.btnProses);
        relProductBarcode = (RelativeLayout) findViewById(R.id.relProductBarcode);
        txtProductBarcode = (EditText) findViewById(R.id.txtProductBarcode);
        imgScan = (ImageView) findViewById(R.id.imgScan);

        lblNumber.setTypeface(fontUbuntuB);
        lblDeskripsi.setTypeface(fontUbuntuB);
        txtDeskripsi.setTypeface(fontUbuntuL);
        lblMenyimpan.setTypeface(fontUbuntuB);
        txtMenyimpan.setTypeface(fontUbuntuL);
        lblBerlangganan.setTypeface(fontUbuntuB);
        txtBerlangganan.setTypeface(fontUbuntuL);
        lblPromo.setTypeface(fontUbuntuB);
        btnProses.setTypeface(fontUbuntuB);
        txtProductBarcode.setTypeface(fontUbuntuL);

        txtDeskripsi.setText(getIntent().getExtras().getString("one"));
        txtMenyimpan.setText(getIntent().getExtras().getString("two"));
        txtBerlangganan.setText(getIntent().getExtras().getString("three"));
        lblNumber.setText(getIntent().getExtras().getString("four"));

        imgBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relProductBarcode.setVisibility(View.VISIBLE);
                imgBarcode.setVisibility(View.GONE);
            }
        });

        imgScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductBarcodeClick(1);
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubscribeDetailActivity.this, ConfirmationPaymentActivity.class));
            }
        });
    }

    public void onProductBarcodeClick(final int intPosition) {
        startActivityForResult(new Intent(getApplicationContext(), BarcodeScannerActivity.class), intPosition);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                txtProductBarcode.setText(data.getStringExtra("Content"));
            }
        }
    }
}
