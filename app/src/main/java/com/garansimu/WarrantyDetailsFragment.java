package com.garansimu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.garansimu.Utils.FontCache;
import com.garansimu.Utils.ImageLoader;
import com.garansimu.Utils.PhotoViewer;
import com.garansimu.Utils.Referensi;
import com.garansimu.Utils.callURL;
import org.json.JSONArray;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WarrantyDetailsFragment extends Fragment {
	private Activity mParentActivity;
	private Typeface fontUbuntuL, fontUbuntuB;
	private TextView lblProductName, lblGaransiName, lblStatusBarang, txtStatusBarang, txtDaysLeft, txtExpressOn,
			lblProofOfPurchase, lblNotes, lblPurchaseDetails, txtPurchaseDetails, txtPurchaseDate, txtPurchasePrice,
			lblModelNumber, txtModelNumber, lblPlaceOfPurchase, txtPlaceOfPurchase, lblSerialNumber, txtSerialNumber;
	private ImageView imgProduct, imgPurchase, imgZoom, imgEditPurchase, imgEditNotes;
	private LinearLayout linNotes, linPurchaseDetails, linPDLayout;
	private RelativeLayout linNotesLayout, relSerialNumber;
	private EditText txtNotes, txtPurchaseDateEdit, txtPurchasePriceEdit, txtModelNumberEdit, txtPlaceOfPurchaseEdit,
			txtSerialNumberEdit;
	private String strId, strNamaItem, strDistributor, strSerialNumber, strPurchaseDate, strNamaToko, strPrice,
			strExpiredWarranty, strPhotoProduct, strPhotoWarrantyCard, strNotes;
	private JSONArray jsonProductImages;
	private ImageLoader mImageLoader;
	private Button btnSave;
	private ProgressDialog pDialog;

	public static WarrantyDetailsFragment newInstance(String strId, String strNamaItem, String strDistributor, String strSerialNumber,
													  String strPurchaseDate, String strNamaToko, String strPrice, String strExpiredWarranty,
													  String strPhotoProduct, String strPhotoWarrantyCard, String strNotes) {
		WarrantyDetailsFragment f = new WarrantyDetailsFragment();
		f.strId 			   = strId;
		f.strNamaItem 	       = strNamaItem;
		f.strDistributor 	   = strDistributor;
		f.strSerialNumber 	   = strSerialNumber;
		f.strPurchaseDate 	   = strPurchaseDate;
		f.strNamaToko 		   = strNamaToko;
		f.strPrice 			   = strPrice;
		f.strExpiredWarranty   = strExpiredWarranty;
		f.strPhotoProduct      = strPhotoProduct;
		f.strPhotoWarrantyCard = strPhotoWarrantyCard;
		f.strNotes			   = strNotes;

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.warranty_details_fragment, container, false);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		fontUbuntuL 	   	   = FontCache.get(mParentActivity, "Ubuntu-L");
		fontUbuntuB 	   	   = FontCache.get(mParentActivity, "Ubuntu-B");
		lblProductName 	   	   = (TextView) view.findViewById(R.id.lblProductName);
		lblGaransiName     	   = (TextView) view.findViewById(R.id.lblGaransiName);
		lblStatusBarang	   	   = (TextView) view.findViewById(R.id.lblStatusBarang);
		txtStatusBarang    	   = (TextView) view.findViewById(R.id.txtStatusBarang);
		txtDaysLeft		   	   = (TextView) view.findViewById(R.id.txtDaysLeft);
		txtExpressOn	   	   = (TextView) view.findViewById(R.id.txtExpressOn);
		imgProduct		   	   = (ImageView) view.findViewById(R.id.imgProduct);
		imgPurchase		   	   = (ImageView) view.findViewById(R.id.imgPurchase);
		lblProofOfPurchase 	   = (TextView) view.findViewById(R.id.lblProofOfPurchase);
		imgZoom			   	   = (ImageView) view.findViewById(R.id.imgZoom);
		lblNotes		   	   = (TextView) view.findViewById(R.id.lblNotes);
		lblPurchaseDetails 	   = (TextView) view.findViewById(R.id.lblPurchaseDetails);
		linNotes		   	   = (LinearLayout) view.findViewById(R.id.linNotes);
		linPurchaseDetails 	   = (LinearLayout) view.findViewById(R.id.linPurchaseDetails);
		linPDLayout		   	   = (LinearLayout) view.findViewById(R.id.linPDLayout);
		txtPurchaseDetails 	   = (TextView) view.findViewById(R.id.txtPurchaseDetails);
		imgEditPurchase	   	   = (ImageView) view.findViewById(R.id.imgEditPurchase);
		txtPurchaseDate	   	   = (TextView) view.findViewById(R.id.txtPurchaseDate);
		txtPurchaseDateEdit    = (EditText) view.findViewById(R.id.txtPurchaseDateEdit);
		txtPurchasePrice       = (TextView) view.findViewById(R.id.txtPurchasePrice);
		txtPurchasePriceEdit   = (EditText) view.findViewById(R.id.txtPurchasePriceEdit);
		lblModelNumber	       = (TextView) view.findViewById(R.id.lblModelNumber);
		txtModelNumber	       = (TextView) view.findViewById(R.id.txtModelNumber);
		txtModelNumberEdit     = (EditText) view.findViewById(R.id.txtModelNumberEdit);
		lblPlaceOfPurchase     = (TextView) view.findViewById(R.id.lblPlaceOfPurchase);
		txtPlaceOfPurchase     = (TextView) view.findViewById(R.id.txtPlaceOfPurchase);
		txtPlaceOfPurchaseEdit = (EditText) view.findViewById(R.id.txtPlaceOfPurchaseEdit);
		lblSerialNumber	       = (TextView) view.findViewById(R.id.lblSerialNumber);
		txtSerialNumber	       = (TextView) view.findViewById(R.id.txtSerialNumber);
		txtSerialNumberEdit    = (EditText) view.findViewById(R.id.txtSerialNumberEdit);
		linNotesLayout	   	   = (RelativeLayout) view.findViewById(R.id.linNotesLayout);
		imgEditNotes	   	   = (ImageView) view.findViewById(R.id.imgEditNotes);
		txtNotes		   	   = (EditText) view.findViewById(R.id.txtNotes);
		mImageLoader       	   = new ImageLoader(mParentActivity);
		btnSave				   = (Button) view.findViewById(R.id.btnSave);
		relSerialNumber		   = (RelativeLayout) view.findViewById(R.id.relSerialNumber);

		lblProductName.setTypeface(fontUbuntuB);
		lblGaransiName.setTypeface(fontUbuntuL);
		lblStatusBarang.setTypeface(fontUbuntuB);
		txtStatusBarang.setTypeface(fontUbuntuL);
		txtDaysLeft.setTypeface(fontUbuntuL);
		txtExpressOn.setTypeface(fontUbuntuL);
		lblProofOfPurchase.setTypeface(fontUbuntuB);
		lblNotes.setTypeface(fontUbuntuL);
		lblPurchaseDetails.setTypeface(fontUbuntuL);
		txtPurchaseDetails.setTypeface(fontUbuntuL);
		txtPurchaseDate.setTypeface(fontUbuntuL);
		txtPurchaseDateEdit.setTypeface(fontUbuntuL);
		txtPurchasePrice.setTypeface(fontUbuntuL);
		txtPurchasePriceEdit.setTypeface(fontUbuntuL);
		lblModelNumber.setTypeface(fontUbuntuB);
		txtModelNumber.setTypeface(fontUbuntuL);
		txtModelNumberEdit.setTypeface(fontUbuntuL);
		lblPlaceOfPurchase.setTypeface(fontUbuntuB);
		txtPlaceOfPurchase.setTypeface(fontUbuntuL);
		txtPlaceOfPurchaseEdit.setTypeface(fontUbuntuL);
		lblSerialNumber.setTypeface(fontUbuntuB);
		txtSerialNumber.setTypeface(fontUbuntuL);
		txtSerialNumberEdit.setTypeface(fontUbuntuL);
		txtNotes.setTypeface(fontUbuntuL);

		linNotes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				linNotesLayout.setVisibility(View.VISIBLE);
				linPDLayout.setVisibility(View.GONE);
				linNotes.setBackgroundColor(Color.parseColor("#27b6e0"));
				linPurchaseDetails.setBackgroundColor(Color.parseColor("#FFFFFF"));
				lblNotes.setTextColor(Color.parseColor("#FFFFFF"));
				lblPurchaseDetails.setTextColor(Color.parseColor("#464646"));
			}
		});

		linPurchaseDetails.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				linNotesLayout.setVisibility(View.GONE);
				linPDLayout.setVisibility(View.VISIBLE);
				linPurchaseDetails.setBackgroundColor(Color.parseColor("#27b6e0"));
				linNotes.setBackgroundColor(Color.parseColor("#FFFFFF"));
				lblPurchaseDetails.setTextColor(Color.parseColor("#FFFFFF"));
				lblNotes.setTextColor(Color.parseColor("#464646"));
			}
		});

		lblProductName.setText(strNamaItem);
		txtModelNumber.setText(strNamaItem);
		txtModelNumberEdit.setText(txtModelNumber.getText().toString());
		lblGaransiName.setText(strDistributor + " Distributor");
		txtSerialNumber.setText(strSerialNumber);
		txtSerialNumberEdit.setText(txtSerialNumber.getText().toString());
		txtPurchaseDate.setText(strPurchaseDate);
		txtPurchaseDateEdit.setText(txtPurchaseDate.getText().toString());
		txtPlaceOfPurchase.setText(strNamaToko);
		txtPlaceOfPurchaseEdit.setText(txtPlaceOfPurchase.getText().toString());
		txtPurchasePrice.setText("IDR "+ Referensi.currencyFormater(Double.parseDouble(strPrice)));
		txtPurchasePriceEdit.setText(txtPurchasePrice.getText().toString().replace("IDR ", ""));
		txtExpressOn.setText("Express on "+strExpiredWarranty);
		int loader = R.drawable.img_loader;
		mImageLoader.DisplayImage(Referensi.url_img2+strPhotoProduct, loader, imgProduct);
		mImageLoader.DisplayImage(Referensi.url_img2+strPhotoWarrantyCard, loader, imgPurchase);
		txtNotes.setText(strNotes);

		imgZoom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jsonProductImages = new JSONArray();
				jsonProductImages.put(strPhotoWarrantyCard);

				Intent i = new Intent(mParentActivity, PhotoViewer.class);
				i.putExtra("image_list", jsonProductImages.toString());
				i.putExtra("position", 0);
				startActivity(i);
			}
		});

		imgEditPurchase.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				imgEditPurchase.setVisibility(View.GONE);
				txtPurchaseDate.setVisibility(View.GONE);
				txtPurchasePrice.setVisibility(View.GONE);
				txtModelNumber.setVisibility(View.GONE);
				txtPlaceOfPurchase.setVisibility(View.GONE);
				txtSerialNumber.setVisibility(View.GONE);
				btnSave.setVisibility(View.VISIBLE);
				txtPurchaseDateEdit.setVisibility(View.VISIBLE);
				txtPurchasePriceEdit.setVisibility(View.VISIBLE);
				txtModelNumberEdit.setVisibility(View.VISIBLE);
				txtPlaceOfPurchaseEdit.setVisibility(View.VISIBLE);
				relSerialNumber.setVisibility(View.VISIBLE);
			}
		});

		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new updateProduct().execute();
			}
		});

		txtPurchaseDateEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DialogFragment newFragment = new SelectDateFragment();
				newFragment.show(getFragmentManager(), "DatePicker");
			}
		});

		txtPurchasePriceEdit.addTextChangedListener(new CurrencyTextWatcher());

		relSerialNumber.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onProductBarcodeClick(2);
			}
		});
		txtSerialNumberEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onProductBarcodeClick(2);
			}
		});

		pDialog = new ProgressDialog(mParentActivity);
		pDialog.setMessage("Working...");
		pDialog.setCancelable(false);

		imgEditNotes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (txtNotes.getText().length()==0) {
					Toast.makeText(mParentActivity, "Notes is Empty!", Toast.LENGTH_SHORT).show();
				} else {
					new updateNotes().execute();
				}
			}
		});

		return view;
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
					txtPurchasePriceEdit.setText(Referensi.currencyFormater(Double.parseDouble(s.toString().replace(",", ""))));
					txtPurchasePriceEdit.setSelection(txtPurchasePriceEdit.getText().length());
				}

				mEditing = false;
			}
		}
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
	}

	public void onProductBarcodeClick(final int intPosition) {
		startActivityForResult(new Intent(mParentActivity, BarcodeScannerActivity.class), intPosition);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2) {
			if (resultCode == mParentActivity.RESULT_OK) {
				txtSerialNumberEdit.setText(data.getStringExtra("Content"));
			}
		}
	}

	private class updateProduct extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
			String strNamaItem = null;
			String strNamaToko = null;
			String strPrice    = null;
			String url;
			try {
				strNamaItem = URLEncoder.encode(txtModelNumberEdit.getText().toString().replace("\"", "'"), "utf-8");
				strNamaToko = URLEncoder.encode(txtPlaceOfPurchaseEdit.getText().toString().replace("\"", "'"), "utf-8");
				strPrice    = URLEncoder.encode(txtPurchasePriceEdit.getText().toString().replace("\"", "'")
						.replace(",", ""), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			url = Referensi.url + "/service.php?ct=UPDATEPRODUCT&purchase_date=" + txtPurchaseDateEdit.getText().toString() +
					"&price=" + strPrice +
					"&nama_item=" + strNamaItem +
					"&nama_toko=" + strNamaToko +
					"&serial_number=" + txtSerialNumberEdit.getText().toString() +
					"&id=" + strId;

			String hasil = callURL.call(url);
			return hasil;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			mParentActivity.setResult(Activity.RESULT_OK);
			mParentActivity.finish();
		}
	}

	private class updateNotes extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
			String strNotes = null;
			String url;
			try {
				strNotes = URLEncoder.encode(txtNotes.getText().toString().replace("\"", "'"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			url = Referensi.url + "/service.php?ct=UPDATENOTES&notes=" + strNotes + "&id=" + strId;

			String hasil = callURL.call(url);
			return hasil;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			mParentActivity.setResult(Activity.RESULT_OK);
			mParentActivity.finish();
			Toast.makeText(mParentActivity, "Update notes success", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof ProductDetailActivity)
			mParentActivity = (ProductDetailActivity) activity;
		if (mParentActivity == null)
			mParentActivity = ProductDetailActivity.getInstance();
	}
}
