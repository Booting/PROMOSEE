package com.garansimu;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.garansimu.Utils.FontCache;
import com.garansimu.Utils.Referensi;
import org.json.JSONArray;
import java.util.ArrayList;

public class OurBrandDetailGalleryAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;
    private Context context;
    private Typeface fontUbuntuL, fontUbuntuB;
    private JSONArray jArrOurBrandItem;

    private final String ID    = "id";
    private final String NAMA  = "nama";
    private final String IMAGE = "image";

    private ArrayList<String> arrayId = new ArrayList<String>(),
            arrayNama  = new ArrayList<String>(),
            arrayImage = new ArrayList<String>();

    public OurBrandDetailGalleryAdapter(Context context, JSONArray jArrOurBrandItem) {
        this.mLayoutInflater  = LayoutInflater.from(context);
        this.context          = context;
        fontUbuntuL 		  = FontCache.get(context, "DroidSans");
		fontUbuntuB 		  = FontCache.get(context, "DroidSans-Bold");
        this.jArrOurBrandItem = jArrOurBrandItem;

        for (int i = 0; i < this.jArrOurBrandItem.length(); i++) {
            arrayId.add(this.jArrOurBrandItem.optJSONObject(i).optString(ID));
            arrayNama.add(this.jArrOurBrandItem.optJSONObject(i).optString(NAMA));
            arrayImage.add(this.jArrOurBrandItem.optJSONObject(i).optString(IMAGE));
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return jArrOurBrandItem.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.our_brand_detail_gallery_cell, parent, false);
            vh = new ViewHolder();

            vh.imgBrand   = (ImageView) convertView.findViewById(R.id.imgBrand);
            vh.lblCaption = (TextView) convertView.findViewById(R.id.lblCaption);

            vh.lblCaption.setTypeface(fontUbuntuL);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Glide.with(context).load(Referensi.url_img+arrayImage.get(position)).placeholder(R.drawable.img_loader).dontAnimate().into(vh.imgBrand);
        vh.lblCaption.setText(arrayNama.get(position));

        return convertView;
    }

    static class ViewHolder {
        ImageView imgBrand;
        TextView lblCaption;
    }

}