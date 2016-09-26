package com.garansimu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.garansimu.Utils.ConnectionDetector;
import com.garansimu.Utils.FontCache;
import com.garansimu.Utils.Referensi;
import com.garansimu.Utils.callURL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AddProductFourActivity extends Activity {
    private Typeface fontUbuntuL, fontUbuntuB;
    private TextView txtTitle, lblEdit, lblSave, lblProductName, lblMerk, lblKategori, lblPeriode, lblTanggalPembelian,
            lblJumlahBarang, lblTempatPembelian, lblCurrency, lblHargaProduk, lblSyarat, lblSyaratDetail;
    private ImageView imgLogoRight, imgHeader, imgProduct;
    private DisplayMetrics displaymetrics;
    private LinearLayout linButton;
    private RelativeLayout relProductImage;
    private CheckBox chkECommerce, chkNonECommerce;
    private RequestQueue queue;
    private ProgressBar progressBar;
    private SharedPreferences garansimuPref;
    private Bundle bundle;
    private String url = "";
    private HttpEntity resEntity;
    private String imagepathOne=null, imagepathTwo=null, imagepathThree=null;
    private String pathOne="", pathTwo="", pathThree="";
    private int countImage=0;

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_add_product_four);

        // creating connection detector class instance
        cd                  = new ConnectionDetector(getApplicationContext());
        garansimuPref 	    = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        queue    	        = Volley.newRequestQueue(this);
        fontUbuntuL         = FontCache.get(this, "Ubuntu-L");
        fontUbuntuB         = FontCache.get(this, "Ubuntu-B");
        txtTitle            = (TextView) findViewById(R.id.txtTitle);
        lblEdit             = (TextView) findViewById(R.id.lblEdit);
        lblSave             = (TextView) findViewById(R.id.lblSave);
        imgLogoRight        = (ImageView) findViewById(R.id.imgLogoRight);
        imgHeader           = (ImageView) findViewById(R.id.imgHeader);
        imgProduct          = (ImageView) findViewById(R.id.imgProduct);
        linButton           = (LinearLayout) findViewById(R.id.linButton);
        relProductImage     = (RelativeLayout) findViewById(R.id.relProductImage);
        lblProductName      = (TextView) findViewById(R.id.lblProductName);
        lblMerk             = (TextView) findViewById(R.id.lblMerk);
        lblKategori         = (TextView) findViewById(R.id.lblKategori);
        lblPeriode          = (TextView) findViewById(R.id.lblPeriode);
        lblTanggalPembelian = (TextView) findViewById(R.id.lblTanggalPembelian);
        lblJumlahBarang     = (TextView) findViewById(R.id.lblJumlahBarang);
        lblTempatPembelian  = (TextView) findViewById(R.id.lblTempatPembelian);
        lblCurrency         = (TextView) findViewById(R.id.lblCurrency);
        lblHargaProduk      = (TextView) findViewById(R.id.lblHargaProduk);
        chkECommerce        = (CheckBox) findViewById(R.id.chkECommerce);
        chkNonECommerce     = (CheckBox) findViewById(R.id.chkNonECommerce);
        lblSyarat           = (TextView) findViewById(R.id.lblSyarat);
        lblSyaratDetail     = (TextView) findViewById(R.id.lblSyaratDetail);
        progressBar         = (ProgressBar) findViewById(R.id.progressBar);

        txtTitle.setText("PRODUCT VENDOR");
        txtTitle.setTypeface(fontUbuntuB);
        imgLogoRight.setVisibility(View.VISIBLE);
        txtTitle.setTypeface(fontUbuntuB);
        lblEdit.setTypeface(fontUbuntuB);
        lblSave.setTypeface(fontUbuntuB);
        lblProductName.setTypeface(fontUbuntuB);
        lblMerk.setTypeface(fontUbuntuL);
        lblKategori.setTypeface(fontUbuntuL);
        lblPeriode.setTypeface(fontUbuntuL);
        lblTanggalPembelian.setTypeface(fontUbuntuL);
        lblJumlahBarang.setTypeface(fontUbuntuL);
        lblTempatPembelian.setTypeface(fontUbuntuL);
        lblCurrency.setTypeface(fontUbuntuL);
        lblHargaProduk.setTypeface(fontUbuntuL);
        chkECommerce.setTypeface(fontUbuntuL);
        chkNonECommerce.setTypeface(fontUbuntuL);
        lblSyarat.setTypeface(fontUbuntuL);
        lblSyaratDetail.setTypeface(fontUbuntuL);

        displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        ViewGroup.LayoutParams paramsHeader  = imgHeader.getLayoutParams();
        paramsHeader.height = (int) (0.05*displaymetrics.heightPixels);

        ViewGroup.LayoutParams paramsImage  = relProductImage.getLayoutParams();
        paramsImage.height = (int) (0.4*displaymetrics.heightPixels);

        /*** SET DATA ***/
        bundle = getIntent().getExtras();
        Glide.with(AddProductFourActivity.this).load(bundle.getString("mTakeProductPath")).asBitmap().into(imgProduct);
        lblProductName.setText(bundle.getString("JenisProduk")+" "+bundle.getString("Merk"));
        lblMerk.setText(bundle.getString("Merk"));
        lblKategori.setText(bundle.getString("Kategori"));
        lblPeriode.setText(bundle.getString("PeriodeGaransi"));
        String[] strings = bundle.getString("TanggalPembelian").split("-");
        lblTanggalPembelian.setText(new StringBuilder().append(strings[2]).append(" ").append(formatMonth(Integer.parseInt(strings[1]))).append(" ").append(strings[0]));
        lblJumlahBarang.setText(bundle.getString("JumlahBarang"));
        lblTempatPembelian.setText(bundle.getString("TempatPembelian"));
        lblCurrency.setText(bundle.getString("Currency"));
        lblHargaProduk.setText(bundle.getString("HargaProduk"));
        if (bundle.getBoolean("E-commerce")) {
            chkECommerce.setChecked(true);
            chkNonECommerce.setChecked(false);
        } else {
            chkECommerce.setChecked(false);
            chkNonECommerce.setChecked(true);
        }

        if (!bundle.getString("mTakeInvoicePath").equalsIgnoreCase("")) {
            imagepathOne = bundle.getString("mTakeInvoicePath");
        } if (!bundle.getString("mTakeProductPath").equalsIgnoreCase("")) {
            imagepathTwo = bundle.getString("mTakeProductPath");
        } if (!bundle.getString("mTakeWarrantyPath").equalsIgnoreCase("")) {
            imagepathThree = bundle.getString("mTakeWarrantyPath");
        }

        lblSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagepathOne!=null) {
                    countImage = countImage+1;
                } if (imagepathTwo!=null) {
                    countImage = countImage+1;
                } if (imagepathThree!=null) {
                    countImage = countImage+1;
                }

                // get Internet status
                isInternetPresent = cd.isConnectingToInternet();

                // check for Internet status
                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests
                    if (countImage==0) {
                        new addNewProduct().execute();
                    } else {
                        new UploadImage().execute();
                    }
                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    Referensi.showAlertDialog(AddProductFourActivity.this, "No Internet Connection", "You don't have internet connection.", false);
                }
            }
        });

        lblEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private class UploadImage extends AsyncTask<HttpEntity, Void, HttpEntity> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected HttpEntity doInBackground(HttpEntity... params) {
            return doFileUpload();
        }
        @Override
        protected void onPostExecute(HttpEntity result) {
            super.onPostExecute(result);
            if (result != null) {
                countImage=0;
                new addNewProduct().execute();
            } else {
                progressBar.setVisibility(View.GONE);
                countImage=0;
            }
        }
    }

    private HttpEntity doFileUpload() {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = null;
            File file1 = null, file2, file3;
            FileBody bin1 = null, bin2, bin3;

            MultipartEntity reqEntity = new MultipartEntity();
            if (countImage==1) {
                post = new HttpPost("http://www.linkincube.com/download/UploadOne.php");
                if (imagepathOne!=null) {
                    file1     = new File(imagepathOne);
                    pathOne   = file1.getName();
                } else if (imagepathTwo!=null) {
                    file1     = new File(imagepathTwo);
                    pathTwo   = file1.getName();
                } else if (imagepathThree!=null) {
                    file1     = new File(imagepathThree);
                    pathThree = file1.getName();
                }

                bin1 = new FileBody(file1);
                reqEntity.addPart("uploadedfile1", bin1);
            } else if (countImage==2) {
                post = new HttpPost("http://www.linkincube.com/download/UploadTwo.php");

                if (imagepathOne!=null) {
                    file1     = new File(imagepathOne);
                    pathOne   = file1.getName();
                } else if (imagepathTwo!=null) {
                    file1     = new File(imagepathTwo);
                    pathTwo   = file1.getName();
                } else if (imagepathThree!=null) {
                    file1     = new File(imagepathThree);
                    pathThree = file1.getName();
                }

                if (imagepathTwo!=null) {
                    file2     = new File(imagepathTwo);
                    pathTwo   = file2.getName();
                } else if (imagepathThree!=null) {
                    file2     = new File(imagepathThree);
                    pathThree = file2.getName();
                } else {
                    file2     = new File(imagepathOne);
                    pathOne   = file2.getName();
                }

                bin1 = new FileBody(file1);
                bin2 = new FileBody(file2);
                reqEntity.addPart("uploadedfile1", bin1);
                reqEntity.addPart("uploadedfile2", bin2);
            } else if (countImage==3) {
                post = new HttpPost("http://www.linkincube.com/download/UploadThree.php");

                if (imagepathOne!=null) {
                    file1	  = new File(imagepathOne);
                    pathOne   = file1.getName();
                } else if (imagepathTwo!=null) {
                    file1 	  = new File(imagepathTwo);
                    pathTwo   = file1.getName();
                } else if (imagepathThree!=null) {
                    file1 	  = new File(imagepathThree);
                    pathThree = file1.getName();
                }

                if (imagepathTwo!=null) {
                    file2     = new File(imagepathTwo);
                    pathTwo   = file2.getName();
                } else if (imagepathThree!=null) {
                    file2     = new File(imagepathThree);
                    pathThree = file2.getName();
                } else {
                    file2     = new File(imagepathOne);
                    pathOne   = file2.getName();
                }

                if (imagepathThree!=null) {
                    file3 	  = new File(imagepathThree);
                    pathThree = file3.getName();
                } else if (imagepathOne!=null) {
                    file3 	  = new File(imagepathOne);
                    pathOne   = file3.getName();
                } else {
                    file3 	  = new File(imagepathTwo);
                    pathTwo   = file3.getName();
                }

                bin1 = new FileBody(file1);
                bin2 = new FileBody(file2);
                bin3 = new FileBody(file3);
                reqEntity.addPart("uploadedfile1", bin1);
                reqEntity.addPart("uploadedfile2", bin2);
                reqEntity.addPart("uploadedfile3", bin3);
            }

            reqEntity.addPart("user", new StringBody("User"));
            post.setEntity(reqEntity);

            HttpResponse response = client.execute(post);
            resEntity = response.getEntity();
            @SuppressWarnings("unused")
            final String response_str = EntityUtils.toString(resEntity);
        } catch (Exception ex) {
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        }
        return resEntity;
    }

    public String formatMonth(int month) {
        DateFormat formatter = new SimpleDateFormat("MMMM", Locale.US);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, month-1);
        return formatter.format(calendar.getTime());
    }

    private class addNewProduct extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... params) {
            String strNamaItem        = null;
            String strKategori        = null;
            String strBrand           = null;
            String strNamaToko        = null;
            String strExpiredWarranty = null;
            String strAnswerCustom1   = null;
            String strAnswerCustom2   = null;
            String strAnswerCustom3   = null;
            String strAnswerCustom4   = null;
            String strNamaCustomer    = null;
            try {
                strNamaItem      = URLEncoder.encode(bundle.getString("Merk").replace("\"", "'"), "utf-8");
                strKategori      = URLEncoder.encode(bundle.getString("Kategori").replace("\"", "'"), "utf-8");
                strBrand         = URLEncoder.encode(bundle.getString("JenisProduk").replace("\"", "'"), "utf-8");
                strNamaToko      = URLEncoder.encode(bundle.getString("TempatPembelian").replace("\"", "'"), "utf-8");
                strAnswerCustom1 = URLEncoder.encode(bundle.getString("answer_custom1").replace("\"", "'"), "utf-8");
                strAnswerCustom2 = URLEncoder.encode(bundle.getString("answer_custom2").replace("\"", "'"), "utf-8");
                strAnswerCustom3 = URLEncoder.encode(bundle.getString("answer_custom3").replace("\"", "'"), "utf-8");
                strAnswerCustom4 = URLEncoder.encode(bundle.getString("answer_custom4").replace("\"", "'"), "utf-8");
                strNamaCustomer  = URLEncoder.encode(garansimuPref.getString("Name", "").replace("\"", "'"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            /*** CEK PERIODE GARANSI ***/
            int intPeriodeGaransi = 0;
            if (bundle.getString("PeriodeGaransi").contains("1")) {
                intPeriodeGaransi = 1;
            } else if (bundle.getString("PeriodeGaransi").contains("2")) {
                intPeriodeGaransi = 2;
            } else if (bundle.getString("PeriodeGaransi").contains("3")) {
                intPeriodeGaransi = 3;
            }

            try {
                Calendar calendar   = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                calendar.setTime(df.parse(bundle.getString("TanggalPembelian")));
                calendar.add(Calendar.DATE, 365*intPeriodeGaransi);
                Date resultdate    = new Date(calendar.getTimeInMillis());
                strExpiredWarranty = df.format(resultdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            url = Referensi.url + "/service.php?ct=ADDPRODUCT&nama_item=" + strNamaItem +
                    "&kategori=" + strKategori +
                    "&brand=" + strBrand +
                    "&barcode=" + bundle.getString("ProductBarcode") +
                    "&serial_number=" + bundle.getString("ProductSerialNumber") +
                    "&serial_number2=" + bundle.getString("ProductSerialNumber2") +
                    "&serial_number3=" + bundle.getString("ProductSerialNumber3") +
                    "&serial_number4=" + bundle.getString("ProductSerialNumber4") +
                    "&serial_number5=" + bundle.getString("ProductSerialNumber5") +
                    "&purchase_date=" + bundle.getString("TanggalPembelian") +
                    "&nama_toko=" + strNamaToko +
                    "&price=" + bundle.getString("HargaProduk") +
                    "&photo_inv=" + pathOne +
                    "&photo_product=" + pathTwo +
                    "&photo_warranty_card=" + pathThree +
                    "&expired_warranty=" + strExpiredWarranty +
                    "&id_cust=" + garansimuPref.getString("Id", "") +
                    "&nama_cust=" + strNamaCustomer +
                    "&hp_cust=" + garansimuPref.getString("Phone", "") +
                    "&email_cust=" + garansimuPref.getString("Email", "") +
                    "&answer_custom1=" + strAnswerCustom1 +
                    "&answer_custom2=" + strAnswerCustom2 +
                    "&answer_custom3=" + strAnswerCustom3 +
                    "&answer_custom4=" + strAnswerCustom4 +
                    "&jumlah_barang=" + bundle.getString("JumlahBarang");

            System.out.println("HALO : "+url);
            String hasil = callURL.call(url);
            return hasil;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), "Add New Product succesfully!", Toast.LENGTH_LONG).show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }, 2000);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
