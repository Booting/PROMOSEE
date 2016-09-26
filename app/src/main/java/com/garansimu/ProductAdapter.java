package com.garansimu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.garansimu.Utils.FontCache;
import com.garansimu.Utils.Referensi;
import org.json.JSONArray;
import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;
    private Context context;
    private Typeface fontUbuntuL, fontUbuntuB;
    private JSONArray jArrSubCategory;

    private final String ID     = "id";
    private final String SUBCAT = "subcat";
    private final String ICON   = "icon";

    private ArrayList<String> arrayId = new ArrayList<String>(),
            arraySubcat = new ArrayList<String>(),
            arrayIcon   = new ArrayList<String>();

    public ProductAdapter(Context context, JSONArray jArrSubCategory) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context         = context;
        fontUbuntuL 		 = FontCache.get(context, "DroidSans");
		fontUbuntuB 		 = FontCache.get(context, "DroidSans-Bold");
        this.jArrSubCategory = jArrSubCategory;

        for (int i = 0; i < this.jArrSubCategory.length(); i++) {
            arrayId.add(this.jArrSubCategory.optJSONObject(i).optString(ID));
            arraySubcat.add(this.jArrSubCategory.optJSONObject(i).optString(SUBCAT));
            arrayIcon.add(this.jArrSubCategory.optJSONObject(i).optString(ICON));
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return jArrSubCategory.length();
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
            convertView = mLayoutInflater.inflate(R.layout.product_cell, parent, false);
            vh = new ViewHolder();

            vh.relItem		   = (RelativeLayout) convertView.findViewById(R.id.relItem);
            vh.txtAddProduct   = (TextView) convertView.findViewById(R.id.txtAddProduct);
            vh.imgIcon         = (ImageView) convertView.findViewById(R.id.imgIcon);
            vh.lblCategoryName = (TextView) convertView.findViewById(R.id.lblCategoryName);
            vh.imgClose        = (ImageView) convertView.findViewById(R.id.imgClose);

            vh.txtAddProduct.setTypeface(fontUbuntuB);
            vh.lblCategoryName.setTypeface(fontUbuntuL);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.lblCategoryName.setText(arraySubcat.get(position));
        Glide.with(context).load(Referensi.url_img+arrayIcon.get(position)).placeholder(R.drawable.img_loader).dontAnimate().into(vh.imgIcon);

        vh.relItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //context.startActivity(new Intent(context, SubscribeActivity.class));
                //context.startActivity(new Intent(context, ProductDetailActivity.class));
            }
        });

        vh.txtAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, AddProductOneActivity.class));
            }
        });

        return convertView;
    }

    static class ViewHolder {
    	RelativeLayout relItem;
        TextView txtAddProduct, lblCategoryName;
        ImageView imgIcon, imgClose;
    }

}