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

public class CategoryAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;
    private Context context;
    private Typeface fontUbuntuL, fontUbuntuB;
    private JSONArray jArrCategory, jArrProduk;
    private int intCountProduk=0;

    private final String ID       = "id";
    private final String CATEGORY = "category";
    private final String LOGO1    = "logo1";
    private final String KATEGORI = "kategori";

    private ArrayList<String> arrayId = new ArrayList<String>(),
            arrayCategory = new ArrayList<String>(),
            arrayLogo1    = new ArrayList<String>(),
            arrayKategori = new ArrayList<String>();

    public CategoryAdapter(Context context, JSONArray jArrCategory, JSONArray jArrProduk) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context         = context;
        fontUbuntuL 		 = FontCache.get(context, "DroidSans");
		fontUbuntuB 		 = FontCache.get(context, "DroidSans-Bold");
        this.jArrCategory    = jArrCategory;
        this.jArrProduk      = jArrProduk;

        if (jArrProduk.length()==0) {
            for (int i = 0; i < this.jArrCategory.length(); i++) {
                arrayId.add(this.jArrCategory.optJSONObject(i).optString(ID));
                arrayCategory.add(this.jArrCategory.optJSONObject(i).optString(CATEGORY));
                arrayLogo1.add(this.jArrCategory.optJSONObject(i).optString(LOGO1));
                arrayKategori.add("");
            }
        } else {
            for (int j = 0; j < this.jArrProduk.length(); j++) {
                for (int i = 0; i < this.jArrCategory.length(); i++) {
                    arrayId.add(this.jArrCategory.optJSONObject(i).optString(ID));
                    arrayCategory.add(this.jArrCategory.optJSONObject(i).optString(CATEGORY));
                    arrayLogo1.add(this.jArrCategory.optJSONObject(i).optString(LOGO1));
                    if (this.jArrProduk.optJSONObject(j).optString(KATEGORI).equalsIgnoreCase(this.jArrCategory.optJSONObject(i).optString(CATEGORY))) {
                        intCountProduk = intCountProduk + 1;
                        arrayKategori.add("" + intCountProduk);
                    } else {
                        arrayKategori.add("");
                    }
                }
            }
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return jArrCategory.length();
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
            convertView = mLayoutInflater.inflate(R.layout.item_cell, parent, false);
            vh = new ViewHolder();

            vh.relItem		   = (RelativeLayout) convertView.findViewById(R.id.relItem);
            vh.txtAddProduct   = (TextView) convertView.findViewById(R.id.txtAddProduct);
            vh.imgIcon         = (ImageView) convertView.findViewById(R.id.imgIcon);
            vh.lblCategoryName = (TextView) convertView.findViewById(R.id.lblCategoryName);
            vh.txtCount        = (TextView) convertView.findViewById(R.id.txtCount);
            vh.imgClose        = (ImageView) convertView.findViewById(R.id.imgClose);

            vh.txtAddProduct.setTypeface(fontUbuntuB);
            vh.lblCategoryName.setTypeface(fontUbuntuL);
            vh.txtCount.setTypeface(fontUbuntuL);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.lblCategoryName.setText(arrayCategory.get(position));
        if (arrayKategori.get(position).equalsIgnoreCase("")) {
            vh.txtCount.setVisibility(View.GONE);
        } else {
            vh.txtCount.setVisibility(View.VISIBLE);
            vh.txtCount.setText(arrayKategori.get(position));
        }
        Glide.with(context).load(Referensi.url_img+arrayLogo1.get(position)).placeholder(R.drawable.img_loader).dontAnimate().into(vh.imgIcon);

        vh.relItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ListProductActivity.class).putExtra("Categori", arrayCategory.get(position)));
            }
        });

        vh.txtAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, AddProductOneActivity.class).putExtra("Categori", arrayCategory.get(position)));
            }
        });

        return convertView;
    }

    static class ViewHolder {
    	RelativeLayout relItem;
        TextView txtAddProduct, lblCategoryName, txtCount;
        ImageView imgIcon, imgClose;
    }

}