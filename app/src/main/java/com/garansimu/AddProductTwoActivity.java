package com.garansimu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.garansimu.Utils.FontCache;
import com.garansimu.Utils.Referensi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class AddProductTwoActivity extends Activity {
    private Typeface fontUbuntuL, fontUbuntuB;
    private TextView txtTitle, lblSave, lblGotIt;
    private EditText txtProductBarcode, txtJumlahBarang, txtProductSerialNumber, txtMerk, txtJenisProduk,
            txtTanggalPembelian, txtTempatPembelian, txtKodeTransaksi, txtHargaProduk,
            txtProductSerialNumberTwo, txtProductSerialNumberThree, txtProductSerialNumberFour, txtProductSerialNumberFive;
    private ImageView imgLogoRight, imgHeader, imgClose, imgLeft;
    private DisplayMetrics displaymetrics;
    private Spinner spinCategory, spinPeriode, spinCurrency;
    private CheckBox chkECommerce;
    private RequestQueue queue;
    private ProgressDialog pDialog;
    private ArrayList<Kategori> categoriList;
    private SharedPreferences garansimuPref;
    private RelativeLayout relProductBarcode, relProductSerialNumber, relProductSerialNumberTwo, relProductSerialNumberThree,
            relProductSerialNumberFour, relProductSerialNumberFive;
    private int year, month, day;
    private StringBuilder strPurchaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_add_product_two);

        garansimuPref 	       = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        queue    	           = Volley.newRequestQueue(this);
        fontUbuntuL            = FontCache.get(this, "Ubuntu-L");
        fontUbuntuB            = FontCache.get(this, "Ubuntu-B");
        txtTitle               = (TextView) findViewById(R.id.txtTitle);
        imgLogoRight           = (ImageView) findViewById(R.id.imgLogoRight);
        imgHeader              = (ImageView) findViewById(R.id.imgHeader);
        lblSave                = (TextView) findViewById(R.id.lblSave);
        txtProductBarcode      = (EditText) findViewById(R.id.txtProductBarcode);
        txtJumlahBarang        = (EditText) findViewById(R.id.txtJumlahBarang);
        txtProductSerialNumber = (EditText) findViewById(R.id.txtProductSerialNumber);
        txtProductSerialNumberTwo = (EditText) findViewById(R.id.txtProductSerialNumberTwo);
        txtProductSerialNumberThree = (EditText) findViewById(R.id.txtProductSerialNumberThree);
        txtProductSerialNumberFour  = (EditText) findViewById(R.id.txtProductSerialNumberFour);
        txtProductSerialNumberFive  = (EditText) findViewById(R.id.txtProductSerialNumberFive);
        txtMerk                = (EditText) findViewById(R.id.txtMerk);
        spinCategory           = (Spinner) findViewById(R.id.spinCategory);
        txtJenisProduk         = (EditText) findViewById(R.id.txtJenisProduk);
        spinPeriode            = (Spinner) findViewById(R.id.spinPeriode);
        txtTanggalPembelian    = (EditText) findViewById(R.id.txtTanggalPembelian);
        txtTempatPembelian     = (EditText) findViewById(R.id.txtTempatPembelian);
        chkECommerce           = (CheckBox) findViewById(R.id.chkECommerce);
        txtKodeTransaksi       = (EditText) findViewById(R.id.txtKodeTransaksi);
        spinCurrency           = (Spinner) findViewById(R.id.spinCurrency);
        txtHargaProduk         = (EditText) findViewById(R.id.txtHargaProduk);
        lblGotIt               = (TextView) findViewById(R.id.lblGotIt);
        relProductBarcode      = (RelativeLayout) findViewById(R.id.relProductBarcode);
        relProductSerialNumber = (RelativeLayout) findViewById(R.id.relProductSerialNumber);
        relProductSerialNumberTwo = (RelativeLayout) findViewById(R.id.relProductSerialNumberTwo);
        relProductSerialNumberThree = (RelativeLayout) findViewById(R.id.relProductSerialNumberThree);
        relProductSerialNumberFour  = (RelativeLayout) findViewById(R.id.relProductSerialNumberFour);
        relProductSerialNumberFive  = (RelativeLayout) findViewById(R.id.relProductSerialNumberFive);
        imgLeft = (ImageView) findViewById(R.id.imgLeft);

        txtTitle.setText("PRODUCT VENDOR");
        txtTitle.setTypeface(fontUbuntuB);
        imgLogoRight.setVisibility(View.VISIBLE);
        txtTitle.setTypeface(fontUbuntuB);
        lblSave.setTypeface(fontUbuntuB);
        txtProductBarcode.setTypeface(fontUbuntuL);
        txtJumlahBarang.setTypeface(fontUbuntuL);
        txtProductSerialNumber.setTypeface(fontUbuntuL);
        txtProductSerialNumberTwo.setTypeface(fontUbuntuL);
        txtProductSerialNumberThree.setTypeface(fontUbuntuL);
        txtProductSerialNumberFour.setTypeface(fontUbuntuL);
        txtProductSerialNumberFive.setTypeface(fontUbuntuL);
        txtMerk.setTypeface(fontUbuntuL);
        txtJenisProduk.setTypeface(fontUbuntuL);
        txtTanggalPembelian.setTypeface(fontUbuntuL);
        txtTempatPembelian.setTypeface(fontUbuntuL);
        chkECommerce.setTypeface(fontUbuntuL);
        txtKodeTransaksi.setTypeface(fontUbuntuL);
        txtHargaProduk.setTypeface(fontUbuntuL);

        displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        ViewGroup.LayoutParams paramsHeader  = imgHeader.getLayoutParams();
        paramsHeader.height = (int) (0.05*displaymetrics.heightPixels);

        ViewGroup.LayoutParams paramsSave  = lblSave.getLayoutParams();
        paramsSave.height = (int) (0.07*displaymetrics.heightPixels);

        /*** ADD ADAPTER PERIODE GARANSI ***/
        List<String> list = new ArrayList<>();
        list.add("1 Tahun Garansi");
        list.add("2 Tahun Garansi");
        list.add("3 Tahun Garansi");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(fontUbuntuL);
                ((TextView) v).setTextSize(14);
                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(fontUbuntuL);
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPeriode.setAdapter(dataAdapter);

        /*** ADD ADAPTER CURRENCY ***/
        List<String> listCurrency = new ArrayList<>();
        listCurrency.add("IDR");
        ArrayAdapter<String> dataAdapterCurrency = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCurrency) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(fontUbuntuL);
                ((TextView) v).setTextSize(14);
                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(fontUbuntuL);
                return v;
            }
        };
        dataAdapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCurrency.setAdapter(dataAdapterCurrency);

        pDialog = new ProgressDialog(AddProductTwoActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);

        getCategori();

        relProductBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductBarcodeClick(1);
            }
        });
        txtProductBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductBarcodeClick(1);
            }
        });
        relProductSerialNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductBarcodeClick(2);
            }
        });
        txtProductSerialNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductBarcodeClick(2);
            }
        });
        relProductSerialNumberTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductBarcodeClick(3);
            }
        });
        txtProductSerialNumberTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductBarcodeClick(3);
            }
        });
        relProductSerialNumberThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductBarcodeClick(4);
            }
        });
        txtProductSerialNumberThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductBarcodeClick(4);
            }
        });
        relProductSerialNumberFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductBarcodeClick(5);
            }
        });
        txtProductSerialNumberFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductBarcodeClick(5);
            }
        });
        relProductSerialNumberFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductBarcodeClick(6);
            }
        });
        txtProductSerialNumberFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductBarcodeClick(6);
            }
        });

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        txtTanggalPembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                showDialog(999);
            }
        });

        lblSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtProductBarcode.getText().toString().equalsIgnoreCase("")) {
                    txtProductBarcode.setError("Product Barcode is Required");
                } else if (txtJumlahBarang.getText().toString().equalsIgnoreCase("")) {
                    txtJumlahBarang.setError("Jumlah Barang / Qty is Required");
                } else if (txtProductSerialNumber.getText().toString().equalsIgnoreCase("")) {
                    txtProductSerialNumber.setError("Product Serial Number is Required!");
                } else if (txtMerk.getText().toString().equalsIgnoreCase("")) {
                    txtMerk.setError("Merk Produk is Required!");
                } else if (txtJenisProduk.getText().toString().equalsIgnoreCase("")) {
                    txtJenisProduk.setError("Jenis Produk is Required!");
                } else if (txtTanggalPembelian.getText().toString().equalsIgnoreCase("")) {
                    txtTanggalPembelian.setError("Tanggal Pembelian is Required!");
                } else if (txtTempatPembelian.getText().toString().equalsIgnoreCase("")) {
                    txtTempatPembelian.setError("Tempat Pembelian is Required!");
                } else if (txtHargaProduk.getText().toString().equalsIgnoreCase("")) {
                    txtHargaProduk.setError("Harga Produk is Required!");
                } else if (Integer.parseInt(txtJumlahBarang.getText().toString())>5) {
                    txtJumlahBarang.setError("Jumlah Barang can't more than 5!");
                } else {
                    Intent intent = new Intent(AddProductTwoActivity.this, AddProductThreeActivity.class);
                    intent.putExtra("ProductBarcode", txtProductBarcode.getText().toString());
                    intent.putExtra("JumlahBarang", txtJumlahBarang.getText().toString());
                    intent.putExtra("ProductSerialNumber", txtProductSerialNumber.getText().toString());
                    intent.putExtra("ProductSerialNumber2", txtProductSerialNumberTwo.getText().toString());
                    intent.putExtra("ProductSerialNumber3", txtProductSerialNumberThree.getText().toString());
                    intent.putExtra("ProductSerialNumber4", txtProductSerialNumberFour.getText().toString());
                    intent.putExtra("ProductSerialNumber5", txtProductSerialNumberFive.getText().toString());
                    intent.putExtra("Merk", txtMerk.getText().toString());
                    intent.putExtra("Kategori", spinCategory.getSelectedItem().toString());
                    intent.putExtra("JenisProduk", txtJenisProduk.getText().toString());
                    intent.putExtra("PeriodeGaransi", spinPeriode.getSelectedItem().toString());
                    intent.putExtra("TanggalPembelian", strPurchaseDate.toString());
                    intent.putExtra("TempatPembelian", txtTempatPembelian.getText().toString());
                    intent.putExtra("Currency", spinCurrency.getSelectedItem().toString());
                    intent.putExtra("HargaProduk", txtHargaProduk.getText().toString());
                    intent.putExtra("KodeTransaksi", txtKodeTransaksi.getText().toString());
                    intent.putExtra("E-commerce", chkECommerce.isChecked());
                    Bundle bundle = getIntent().getExtras();
                    intent.putExtra("mTakeInvoicePath", bundle.getString("mTakeInvoicePath"));
                    intent.putExtra("mTakeProductPath", bundle.getString("mTakeProductPath"));
                    intent.putExtra("mTakeWarrantyPath", bundle.getString("mTakeWarrantyPath"));
                    startActivity(intent);
                }
            }
        });

        txtHargaProduk.addTextChangedListener(new CurrencyTextWatcher());

        txtJumlahBarang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equalsIgnoreCase("1")) {
                    relProductSerialNumber.setVisibility(View.VISIBLE);
                    relProductSerialNumberTwo.setVisibility(View.GONE);
                    relProductSerialNumberThree.setVisibility(View.GONE);
                    relProductSerialNumberFour.setVisibility(View.GONE);
                    relProductSerialNumberFive.setVisibility(View.GONE);
                } else if (s.toString().equalsIgnoreCase("2")) {
                    relProductSerialNumber.setVisibility(View.VISIBLE);
                    relProductSerialNumberTwo.setVisibility(View.VISIBLE);
                    relProductSerialNumberThree.setVisibility(View.GONE);
                    relProductSerialNumberFour.setVisibility(View.GONE);
                    relProductSerialNumberFive.setVisibility(View.GONE);
                } else if (s.toString().equalsIgnoreCase("3")) {
                    relProductSerialNumber.setVisibility(View.VISIBLE);
                    relProductSerialNumberTwo.setVisibility(View.VISIBLE);
                    relProductSerialNumberThree.setVisibility(View.VISIBLE);
                    relProductSerialNumberFour.setVisibility(View.GONE);
                    relProductSerialNumberFive.setVisibility(View.GONE);
                } else if (s.toString().equalsIgnoreCase("4")) {
                    relProductSerialNumber.setVisibility(View.VISIBLE);
                    relProductSerialNumberTwo.setVisibility(View.VISIBLE);
                    relProductSerialNumberThree.setVisibility(View.VISIBLE);
                    relProductSerialNumberFour.setVisibility(View.VISIBLE);
                    relProductSerialNumberFive.setVisibility(View.GONE);
                } else if (s.toString().equalsIgnoreCase("5")) {
                    relProductSerialNumber.setVisibility(View.VISIBLE);
                    relProductSerialNumberTwo.setVisibility(View.VISIBLE);
                    relProductSerialNumberThree.setVisibility(View.VISIBLE);
                    relProductSerialNumberFour.setVisibility(View.VISIBLE);
                    relProductSerialNumberFive.setVisibility(View.VISIBLE);
                } else {
                    relProductSerialNumber.setVisibility(View.GONE);
                    relProductSerialNumberTwo.setVisibility(View.GONE);
                    relProductSerialNumberThree.setVisibility(View.GONE);
                    relProductSerialNumberFour.setVisibility(View.GONE);
                    relProductSerialNumberFive.setVisibility(View.GONE);
                }
            }
        });

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class CurrencyTextWatcher implements TextWatcher {
        boolean mEditing;
        public CurrencyTextWatcher() {
            mEditing = false;
        }
        public synchronized void afterTextChanged(Editable s) {
            if(!mEditing) {
                mEditing = true;

                if (s.length()!=0) {
                    txtHargaProduk.setText(Referensi.currencyFormater(Double.parseDouble(s.toString().replace(",", ""))));
                    txtHargaProduk.setSelection(txtHargaProduk.getText().length());
                }

                mEditing = false;
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            strPurchaseDate = new StringBuilder().append(arg1).append("-").append(arg2+1).append("-").append(arg3);
            txtTanggalPembelian.setText(new StringBuilder().append(arg3).append(" ").append(formatMonth(arg2 + 1)).append(" ").append(arg1));
        }
    };

    public String formatMonth(int month) {
        DateFormat formatter = new SimpleDateFormat("MMMM", Locale.US);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, month-1);
        return formatter.format(calendar.getTime());
    }

    public void getCategori() {
        pDialog.show();
        String url = Referensi.url+"/getCategori.php";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray categories = response.getJSONArray("categories");
                    categoriList = new ArrayList<Kategori>();
                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject catObj = (JSONObject) categories.get(i);
                        Kategori cat = new Kategori(catObj.getInt("id"), catObj.getString("category"), catObj.getString("logo1"));
                        categoriList.add(cat);
                    }
                    populateSpinnerCategori();
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void populateSpinnerCategori() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < categoriList.size(); i++) {
            list.add(categoriList.get(i).getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(fontUbuntuL);
                ((TextView) v).setTextSize(14);
                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(fontUbuntuL);
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(dataAdapter);
        if (!getIntent().getExtras().getString("Categori").equals(null)) {
            int spinnerPosition = dataAdapter.getPosition(getIntent().getExtras().getString("Categori"));
            spinCategory.setSelection(spinnerPosition);
        }
    }

    public void onProductBarcodeClick(final int intPosition) {
        if (garansimuPref.getBoolean("IsFirst", false)) {
            final Dialog dialog = new Dialog(AddProductTwoActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);

            lblGotIt = (TextView) dialog.findViewById(R.id.lblGotIt);
            imgClose = (ImageView) dialog.findViewById(R.id.imgClose);

            lblGotIt.setTypeface(fontUbuntuB);

            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            lblGotIt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    SharedPreferences.Editor editor = garansimuPref.edit();
                    editor.putBoolean("IsFirst", false);
                    editor.commit();
                    startActivityForResult(new Intent(getApplicationContext(), BarcodeScannerActivity.class), intPosition);
                }
            });

            dialog.show();
        } else {
            startActivityForResult(new Intent(getApplicationContext(), BarcodeScannerActivity.class), intPosition);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                txtProductBarcode.setText(data.getStringExtra("Content"));
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                txtProductSerialNumber.setText(data.getStringExtra("Content"));
            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                txtProductSerialNumberTwo.setText(data.getStringExtra("Content"));
            }
        } else if (requestCode == 4) {
            if (resultCode == RESULT_OK) {
                txtProductSerialNumberThree.setText(data.getStringExtra("Content"));
            }
        } else if (requestCode == 5) {
            if (resultCode == RESULT_OK) {
                txtProductSerialNumberFour.setText(data.getStringExtra("Content"));
            }
        }else if (requestCode == 6) {
            if (resultCode == RESULT_OK) {
                txtProductSerialNumberFive.setText(data.getStringExtra("Content"));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
