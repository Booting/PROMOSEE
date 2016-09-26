package com.garansimu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.garansimu.Utils.ConnectionDetector;
import com.garansimu.Utils.FontCache;
import com.garansimu.Utils.ImageLoader;
import com.garansimu.Utils.Referensi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SupportFragment extends Fragment {
	private Activity mParentActivity;
	private Typeface fontUbuntuL, fontUbuntuB;
	private TextView lblProductName, lblProductDetail, lblMakeACall, lblServiceCenter, lblReportAnIssue, lblUserManual,
			txtHowToUse;
	private LinearLayout linMakeACall, linServiceCenter, linReportAnIssue, linUserManual;
	private WebView displayVideo;
	private ImageView imgProduct;
	private String strId, strNamaItem, strKategori, strPhotoProduct, strBrand, strTelpNumber="";
	private RequestQueue queue;
	private ProgressDialog pDialog;
	private ImageLoader mImageLoader;

	// flag for Internet connection status
	Boolean isInternetPresent = false;
	// Connection detector class
	ConnectionDetector cd;

	public static SupportFragment newInstance(String strId, String strNamaItem, String strKategori, String strPhotoProduct, String strBrand) {
		SupportFragment f = new SupportFragment();
		f.strId 		  = strId;
		f.strNamaItem 	  = strNamaItem;
		f.strKategori     = strKategori;
		f.strPhotoProduct = strPhotoProduct;
		f.strBrand		  = strBrand;

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.support_fragment, container, false);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		// creating connection detector class instance
		cd               = new ConnectionDetector(mParentActivity);
		queue    	 	 = Volley.newRequestQueue(mParentActivity);
		fontUbuntuL 	 = FontCache.get(mParentActivity, "Ubuntu-L");
		fontUbuntuB 	 = FontCache.get(mParentActivity, "Ubuntu-B");
		lblProductName   = (TextView) view.findViewById(R.id.lblProductName);
		lblProductDetail = (TextView) view.findViewById(R.id.lblProductDetail);
		linMakeACall     = (LinearLayout) view.findViewById(R.id.linMakeACall);
		lblMakeACall     = (TextView) view.findViewById(R.id.lblMakeACall);
		linServiceCenter = (LinearLayout) view.findViewById(R.id.linServiceCenter);
		lblServiceCenter = (TextView) view.findViewById(R.id.lblServiceCenter);
		linReportAnIssue = (LinearLayout) view.findViewById(R.id.linReportAnIssue);
		lblReportAnIssue = (TextView) view.findViewById(R.id.lblReportAnIssue);
		linUserManual    = (LinearLayout) view.findViewById(R.id.linUserManual);
		lblUserManual    = (TextView) view.findViewById(R.id.lblUserManual);
		txtHowToUse	     = (TextView) view.findViewById(R.id.txtHowToUse);
		displayVideo 	 = (WebView) view.findViewById(R.id.webView);
		imgProduct		 = (ImageView) view.findViewById(R.id.imgProduct);
		mImageLoader     = new ImageLoader(mParentActivity);

		lblProductName.setTypeface(fontUbuntuB);
		lblProductDetail.setTypeface(fontUbuntuL);
		lblMakeACall.setTypeface(fontUbuntuB);
		lblServiceCenter.setTypeface(fontUbuntuL);
		lblReportAnIssue.setTypeface(fontUbuntuL);
		lblUserManual.setTypeface(fontUbuntuL);
		txtHowToUse.setTypeface(fontUbuntuL);

		lblProductName.setText(strNamaItem);
		lblProductDetail.setText(strKategori);
		int loader = R.drawable.img_loader;
		mImageLoader.DisplayImage(Referensi.url_img2+strPhotoProduct, loader, imgProduct);

		String frameVideo = "<html><body><iframe width='320' height='315' src='https://www.youtube.com/embed/lY2H2ZP56K4' frameborder='0' allowfullscreen></iframe></body></html>";
		displayVideo.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});
		WebSettings webSettings = displayVideo.getSettings();
		webSettings.setJavaScriptEnabled(true);
		displayVideo.loadData(frameVideo, "text/html", "utf-8");

		linServiceCenter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mParentActivity, ServiceCentersActivity.class);
				intent.putExtra("brand", strBrand);
				startActivity(intent);
			}
		});

		linReportAnIssue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mParentActivity, ReportAnIssueActivity.class);
				intent.putExtra("strBrand", strBrand);
				startActivity(intent);
			}
		});

		lblMakeACall.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (strTelpNumber.equalsIgnoreCase("")) {
					Toast.makeText(mParentActivity, "No. Telp is Required!", Toast.LENGTH_SHORT).show();
				} else {
					String uri = "tel:" + strTelpNumber.replace("-","").replace(" ","");
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse(uri));
					startActivity(intent);
				}
			}
		});

		pDialog = new ProgressDialog(mParentActivity);
		pDialog.setMessage("Working...");
		pDialog.setCancelable(false);

		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();

		// check for Internet status
		if (isInternetPresent) {
			// Internet Connection is Present
			// make HTTP requests
			getCallNumberOfPrincipal();
		} else {
			// Internet connection is not present
			// Ask user to connect to Internet
			Referensi.showAlertDialog(mParentActivity, "No Internet Connection", "You don't have internet connection.", false);
		}

		return view;
    }

	public void getCallNumberOfPrincipal() {
		pDialog.show();
		String url = Referensi.url+"/getCallNumberOfPrincipal.php?brand='"+strBrand+"'";
		JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				if (response.length()!=0) {
					try {
						JSONObject jsonObject = response.getJSONObject(0);
						if (!jsonObject.isNull("telp") || jsonObject.getString("telp").equalsIgnoreCase("")) {
							strTelpNumber = jsonObject.getString("telp");
						} else {
							if (!jsonObject.isNull("hp") || jsonObject.getString("hp").equalsIgnoreCase("")) {
								strTelpNumber = jsonObject.getString("hp");
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(mParentActivity, "No data Service Centers!", Toast.LENGTH_SHORT).show();
				}

				pDialog.dismiss();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				pDialog.dismiss();
				Toast.makeText(mParentActivity, error.toString(), Toast.LENGTH_SHORT).show();
			}
		});
		queue.add(jsObjRequest);
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
