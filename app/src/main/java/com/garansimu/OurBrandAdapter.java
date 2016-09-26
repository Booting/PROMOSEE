package com.garansimu;

import android.content.Context;
import android.content.Intent;
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

public class OurBrandAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;
    private Context context;
    private Typeface fontUbuntuL, fontUbuntuB;
    private JSONArray jArrOurBrand;

    private final String PK_ID    = "pk_id";
    private final String NAMA_PT  = "nama_pt";
    private final String KATEGORI = "kategori";
    private final String BRAND    = "brand";
    private final String DESC     = "desc";
    private final String WEBSITE  = "website";
    private final String TELP     = "telp";
    private final String KOTA     = "kota";
    private final String ALAMAT   = "alamat";
    private final String KODE_POS = "kode_pos";
    private final String NAMA_PIC = "nama_pic";
    private final String HP       = "hp";
    private final String EMAIL    = "email";
    private final String ROLE     = "role";
    private final String STATUS   = "status";
    private final String LOGO     = "logo";

    private ArrayList<String> arrayPkId = new ArrayList<String>(),
            arrayNamaPt   = new ArrayList<String>(),
            arrayKategori = new ArrayList<String>(),
            arrayBrand    = new ArrayList<String>(),
            arrayDesc     = new ArrayList<String>(),
            arrayWebsite  = new ArrayList<String>(),
            arrayTelp     = new ArrayList<String>(),
            arrayKota     = new ArrayList<String>(),
            arrayAlamat   = new ArrayList<String>(),
            arrayKodePos  = new ArrayList<String>(),
            arrayNamaPic  = new ArrayList<String>(),
            arrayHp       = new ArrayList<String>(),
            arrayEmail    = new ArrayList<String>(),
            arrayRole     = new ArrayList<String>(),
            arrayStatus   = new ArrayList<String>(),
            arrayLogo     = new ArrayList<String>();

    public OurBrandAdapter(Context context, JSONArray jArrOurBrand) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context         = context;
        fontUbuntuL 		 = FontCache.get(context, "DroidSans");
		fontUbuntuB 		 = FontCache.get(context, "DroidSans-Bold");
        /*this.jArrOurBrand    = jArrOurBrand;

        for (int i = 0; i < this.jArrOurBrand.length(); i++) {
            arrayPkId.add(this.jArrOurBrand.optJSONObject(i).optString(PK_ID));
            arrayNamaPt.add(this.jArrOurBrand.optJSONObject(i).optString(NAMA_PT));
            arrayKategori.add(this.jArrOurBrand.optJSONObject(i).optString(KATEGORI));
            arrayBrand.add(this.jArrOurBrand.optJSONObject(i).optString(BRAND));
            arrayDesc.add(this.jArrOurBrand.optJSONObject(i).optString(DESC));
            arrayWebsite.add(this.jArrOurBrand.optJSONObject(i).optString(WEBSITE));
            arrayTelp.add(this.jArrOurBrand.optJSONObject(i).optString(TELP));
            arrayKota.add(this.jArrOurBrand.optJSONObject(i).optString(KOTA));
            arrayAlamat.add(this.jArrOurBrand.optJSONObject(i).optString(ALAMAT));
            arrayKodePos.add(this.jArrOurBrand.optJSONObject(i).optString(KODE_POS));
            arrayNamaPic.add(this.jArrOurBrand.optJSONObject(i).optString(NAMA_PIC));
            arrayHp.add(this.jArrOurBrand.optJSONObject(i).optString(HP));
            arrayEmail.add(this.jArrOurBrand.optJSONObject(i).optString(EMAIL));
            arrayRole.add(this.jArrOurBrand.optJSONObject(i).optString(ROLE));
            arrayStatus.add(this.jArrOurBrand.optJSONObject(i).optString(STATUS));
            arrayLogo.add(this.jArrOurBrand.optJSONObject(i).optString(LOGO));
        }*/
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 12;
        //return jArrOurBrand.length();
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
            convertView = mLayoutInflater.inflate(R.layout.item_cell_our_brand, parent, false);
            vh = new ViewHolder();

            vh.imgBrand      = (ImageView) convertView.findViewById(R.id.imgBrand);
            vh.lblTitleOne   = (TextView) convertView.findViewById(R.id.lblTitleOne);
            vh.lblTitleTwo   = (TextView) convertView.findViewById(R.id.lblTitleTwo);
            vh.lblTitleThree = (TextView) convertView.findViewById(R.id.lblTitleThree);
            vh.lblTitleFour  = (TextView) convertView.findViewById(R.id.lblTitleFour);

            vh.lblTitleOne.setTypeface(fontUbuntuB);
            vh.lblTitleTwo.setTypeface(fontUbuntuL);
            vh.lblTitleThree.setTypeface(fontUbuntuL);
            vh.lblTitleFour.setTypeface(fontUbuntuL);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if (position==0) {
            vh.imgBrand.setImageResource(R.drawable.list_product_02);
        } else if (position==1) {
            vh.imgBrand.setImageResource(R.drawable.list_product_03);
        } else if (position==2) {
            vh.imgBrand.setImageResource(R.drawable.list_product_04);
        } else if (position==3) {
            vh.imgBrand.setImageResource(R.drawable.list_product_05);
        } else if (position==4) {
            vh.imgBrand.setImageResource(R.drawable.list_product_06);
        } else if (position==5) {
            vh.imgBrand.setImageResource(R.drawable.list_product_07);
        } else if (position==6) {
            vh.imgBrand.setImageResource(R.drawable.list_product_08);
        } else if (position==7) {
            vh.imgBrand.setImageResource(R.drawable.list_product_09);
        } else if (position==8) {
            vh.imgBrand.setImageResource(R.drawable.list_product_10);
        } else if (position==9) {
            vh.imgBrand.setImageResource(R.drawable.list_product_11);
        } else if (position==10) {
            vh.imgBrand.setImageResource(R.drawable.list_product_12);
        } else if (position==11) {
            vh.imgBrand.setImageResource(R.drawable.list_product_14);
        }

        //Glide.with(context).load(Referensi.url_img+arrayLogo.get(position)).placeholder(R.drawable.img_loader).dontAnimate().into(vh.imgBrand);
        //vh.lblCaption.setText(arrayNamaPt.get(position));

        vh.imgBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position==0) {
                    Intent intent = new Intent(context, MetodePembayaranActivity.class);
                    /*intent.putExtra("nama_pt", arrayNamaPt.get(position).toString());
                    intent.putExtra("desc", arrayDesc.get(position).toString());
                    intent.putExtra("alamat", arrayAlamat.get(position).toString()+", "+arrayKota.get(position).toString()+", "+arrayKodePos.get(position).toString());
                    intent.putExtra("telp", arrayTelp.get(position).toString());
                    intent.putExtra("hp", arrayHp.get(position).toString());
                    intent.putExtra("website", arrayWebsite.get(position).toString());
                    intent.putExtra("email", arrayEmail.get(position).toString());
                    intent.putExtra("brand", arrayBrand.get(position).toString());
                    intent.putExtra("logo", arrayLogo.get(position).toString());*/
                    context.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView imgBrand;
        TextView lblTitleOne, lblTitleTwo, lblTitleThree, lblTitleFour;
    }

}