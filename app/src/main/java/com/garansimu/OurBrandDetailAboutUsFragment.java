package com.garansimu;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.garansimu.Utils.FontCache;
import com.garansimu.Utils.Referensi;

import uk.co.deanwild.flowtextview.FlowTextView;

public class OurBrandDetailAboutUsFragment extends Fragment {
	private Activity mParentActivity;
	private Typeface fontLatoReguler, fontLatoHeavy;
	private TextView lblAlamat, lblTelp1, lblTelp2, lblWebsite, lblEmail;
	private ImageView imgLogo;
	private String strLogo, strDesc, strAlamat, strTelp, strHp, strWebsite, strEmail;

	public static OurBrandDetailAboutUsFragment newInstance(String strLogo, String strDesc, String strAlamat,
															String strTelp, String strHp, String strWebsite, String strEmail) {
		OurBrandDetailAboutUsFragment f = new OurBrandDetailAboutUsFragment();
		f.strLogo    = strLogo;
		f.strDesc    = strDesc;
		f.strAlamat  = strAlamat;
		f.strTelp    = strTelp;
		f.strHp		 = strHp;
		f.strWebsite = strWebsite;
		f.strEmail   = strEmail;

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.our_brand_detail_about_us_fragment, container, false);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		fontLatoReguler = FontCache.get(mParentActivity, "Lato-Regular");
		fontLatoHeavy 	= FontCache.get(mParentActivity, "Lato-Heavy");
		imgLogo			= (ImageView) view.findViewById(R.id.imgLogo);
		lblAlamat		= (TextView) view.findViewById(R.id.lblAlamat);
		lblTelp1		= (TextView) view.findViewById(R.id.lblTelp1);
		lblTelp2		= (TextView) view.findViewById(R.id.lblTelp2);
		lblWebsite		= (TextView) view.findViewById(R.id.lblWebsite);
		lblEmail		= (TextView) view.findViewById(R.id.lblEmail);

		lblAlamat.setTypeface(fontLatoReguler);
		lblTelp1.setTypeface(fontLatoReguler);
		lblTelp2.setTypeface(fontLatoReguler);
		lblWebsite.setTypeface(fontLatoReguler);
		lblEmail.setTypeface(fontLatoReguler);

		Glide.with(mParentActivity).load(Referensi.url_img+strLogo).placeholder(R.drawable.img_loader).dontAnimate().into(imgLogo);

		FlowTextView flowTextView = (FlowTextView) view.findViewById(R.id.ftv);
		Spanned html = Html.fromHtml("<html>"+strDesc+"</html>");
		flowTextView.setText(html);
		flowTextView.setTypeface(fontLatoReguler);
		flowTextView.setTextSize(38);
		flowTextView.invalidate();

		if (strAlamat.equalsIgnoreCase("")) {
			lblAlamat.setText("-");
		} else {
			lblAlamat.setText(strAlamat);
		}

		if (strHp.equalsIgnoreCase("")) {
			lblTelp1.setText("-");
		} else {
			lblTelp1.setText(strHp);
		}

		if (strTelp.equalsIgnoreCase("")) {
			lblTelp2.setText("-");
		} else {
			lblTelp2.setText(strTelp);
		}

		if (strWebsite.equalsIgnoreCase("")) {
			lblWebsite.setText("-");
		} else {
			lblWebsite.setText(strWebsite);
		}

		if (strEmail.equalsIgnoreCase("")) {
			lblEmail.setText("-");
		} else {
			lblEmail.setText(strEmail);
		}

		return view;
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
