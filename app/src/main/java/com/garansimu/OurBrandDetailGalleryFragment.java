package com.garansimu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.etsy.android.grid.StaggeredGridView;
import com.garansimu.Utils.FontCache;
import com.garansimu.Utils.Referensi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OurBrandDetailGalleryFragment extends Fragment {
	private Activity mParentActivity;
	private Typeface fontLatoReguler, fontLatoHeavy;
	private StaggeredGridView itemList;
	private OurBrandDetailGalleryAdapter ourBrandDetailGalleryAdapter;
	private RequestQueue queue;
	private ProgressDialog pDialog;
	private String strBrand;

	public static OurBrandDetailGalleryFragment newInstance(String strBrand) {
		OurBrandDetailGalleryFragment f = new OurBrandDetailGalleryFragment();
		f.strBrand = strBrand;

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.our_brand_detail_gallery_fragment, container, false);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		queue    	    = Volley.newRequestQueue(mParentActivity);
		fontLatoReguler = FontCache.get(mParentActivity, "Lato-Regular");
		fontLatoHeavy 	= FontCache.get(mParentActivity, "Lato-Heavy");
		itemList        = (StaggeredGridView) view.findViewById(R.id.itemList);

		pDialog = new ProgressDialog(mParentActivity);
		pDialog.setMessage("Working...");
		pDialog.setCancelable(false);

		getOurBrandItem(strBrand);

		return view;
    }

	public void getOurBrandItem(String strBrand) {
		pDialog.show();
		String url = Referensi.url + "/getOurBrandItem.php?brand='" + strBrand + "'";

		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if (response.getJSONArray("categories").length()!=0) {
						ourBrandDetailGalleryAdapter = new OurBrandDetailGalleryAdapter(mParentActivity, response.getJSONArray("categories"));
						itemList.setAdapter(ourBrandDetailGalleryAdapter);
					} else {
						JSONArray jsonArray = new JSONArray();
						ourBrandDetailGalleryAdapter = new OurBrandDetailGalleryAdapter(mParentActivity, jsonArray);
						itemList.setAdapter(ourBrandDetailGalleryAdapter);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				pDialog.dismiss();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				pDialog.dismiss();
				JSONArray jsonArray = new JSONArray();
				ourBrandDetailGalleryAdapter = new OurBrandDetailGalleryAdapter(mParentActivity, jsonArray);
				itemList.setAdapter(ourBrandDetailGalleryAdapter);
			}
		});
		queue.add(jsObjRequest);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OurBrandDetailActivity)
			mParentActivity = (OurBrandDetailActivity) activity;
		if (mParentActivity == null)
			mParentActivity = OurBrandDetailActivity.getInstance();
	}
}
