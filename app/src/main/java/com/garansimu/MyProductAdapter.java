package com.garansimu;

import android.app.Activity;
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
import com.garansimu.Utils.FontCache;
import com.garansimu.Utils.ImageLoader;
import com.garansimu.Utils.Referensi;
import org.json.JSONArray;
import java.util.ArrayList;

public class MyProductAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;
    private Context context;
    private Typeface fontUbuntuL, fontUbuntuB;
    private JSONArray jArrProduk;
    private ImageLoader mImageLoader;

    private final String ID                  = "id";
    private final String NAMA_ITEM           = "nama_item";
    private final String DISTRIBUTOR         = "distributor";
    private final String BRAND               = "brand";
    private final String SERIAL_NUMBER       = "serial_number";
    private final String PURCHASE_DATE       = "purchase_date";
    private final String NAMA_TOKO           = "nama_toko";
    private final String PRICE               = "price";
    private final String EXPIRED_WARRANTY    = "expired_warranty";
    private final String PHOTO_PRODUCT       = "photo_product";
    private final String PHOTO_WARRANTY_CARD = "photo_warranty_card";
    private final String ACTIVATE            = "activate";
    private final String KATEGORI            = "kategori";
    private final String NOTES               = "notes";

    private ArrayList<String> arrayId = new ArrayList<String>(),
            arrayNamaItem          = new ArrayList<String>(),
            arrayDistributor       = new ArrayList<String>(),
            arrayBrand             = new ArrayList<String>(),
            arraySerialNumber      = new ArrayList<String>(),
            arrayPurchaseDate      = new ArrayList<String>(),
            arrayNamaToko          = new ArrayList<String>(),
            arrayPrice             = new ArrayList<String>(),
            arrayExpiredWarranty   = new ArrayList<String>(),
            arrayPhotoProduct      = new ArrayList<String>(),
            arrayPhotoWarrantyCard = new ArrayList<String>(),
            arrayActivate          = new ArrayList<String>(),
            arrayKategori          = new ArrayList<String>(),
            arrayNotes             = new ArrayList<String>();

    public MyProductAdapter(Context context, JSONArray jArrProduk) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context         = context;
        fontUbuntuL 		 = FontCache.get(context, "Lato-Regular");
		fontUbuntuB 		 = FontCache.get(context, "Lato-Heavy");
        this.jArrProduk      = jArrProduk;
        this.mImageLoader    = new ImageLoader(context);

        for (int i = 0; i < this.jArrProduk.length(); i++) {
            arrayId.add(this.jArrProduk.optJSONObject(i).optString(ID));
            arrayNamaItem.add(this.jArrProduk.optJSONObject(i).optString(NAMA_ITEM));
            arrayDistributor.add(this.jArrProduk.optJSONObject(i).optString(DISTRIBUTOR));
            arrayBrand.add(this.jArrProduk.optJSONObject(i).optString(BRAND));
            arraySerialNumber.add(this.jArrProduk.optJSONObject(i).optString(SERIAL_NUMBER));
            arrayPurchaseDate.add(this.jArrProduk.optJSONObject(i).optString(PURCHASE_DATE));
            arrayNamaToko.add(this.jArrProduk.optJSONObject(i).optString(NAMA_TOKO));
            arrayPrice.add(this.jArrProduk.optJSONObject(i).optString(PRICE));
            arrayExpiredWarranty.add(this.jArrProduk.optJSONObject(i).optString(EXPIRED_WARRANTY));
            arrayPhotoProduct.add(this.jArrProduk.optJSONObject(i).optString(PHOTO_PRODUCT));
            arrayPhotoWarrantyCard.add(this.jArrProduk.optJSONObject(i).optString(PHOTO_WARRANTY_CARD));
            arrayActivate.add(this.jArrProduk.optJSONObject(i).optString(ACTIVATE));
            arrayKategori.add(this.jArrProduk.optJSONObject(i).optString(KATEGORI));
            arrayNotes.add(this.jArrProduk.optJSONObject(i).optString(NOTES));
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return jArrProduk.length();
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
            convertView = mLayoutInflater.inflate(R.layout.my_product_cell, parent, false);
            vh = new ViewHolder();

            vh.relItem		  = (RelativeLayout) convertView.findViewById(R.id.relItem);
            vh.txtNamaProduct = (TextView) convertView.findViewById(R.id.txtNamaProduct);
            vh.imgProduct     = (ImageView) convertView.findViewById(R.id.imgProduct);
            vh.imgClose       = (ImageView) convertView.findViewById(R.id.imgClose);

            vh.txtNamaProduct.setTypeface(fontUbuntuB);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.txtNamaProduct.setText(arrayNamaItem.get(position));
        int loader = R.drawable.img_loader;
        mImageLoader.DisplayImage(Referensi.url_img2+arrayPhotoProduct.get(position), loader, vh.imgProduct);

        vh.relItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).startActivityForResult(new Intent(context, ProductDetailActivity.class)
                        .putExtra("id", arrayId.get(position))
                        .putExtra("nama_item", arrayNamaItem.get(position))
                        .putExtra("distributor", arrayDistributor.get(position))
                        .putExtra("brand", arrayBrand.get(position))
                        .putExtra("serial_number", arraySerialNumber.get(position))
                        .putExtra("purchase_date", arrayPurchaseDate.get(position))
                        .putExtra("nama_toko", arrayNamaToko.get(position))
                        .putExtra("price", arrayPrice.get(position))
                        .putExtra("expired_warranty", arrayExpiredWarranty.get(position))
                        .putExtra("photo_product", arrayPhotoProduct.get(position))
                        .putExtra("photo_warranty_card", arrayPhotoWarrantyCard.get(position))
                        .putExtra("activate", arrayActivate.get(position))
                        .putExtra("kategori", arrayKategori.get(position))
                        .putExtra("notes", arrayNotes.get(position)), 1);
            }
        });

        vh.txtNamaProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).startActivityForResult(new Intent(context, ProductDetailActivity.class)
                        .putExtra("id", arrayId.get(position))
                        .putExtra("nama_item", arrayNamaItem.get(position))
                        .putExtra("distributor", arrayDistributor.get(position))
                        .putExtra("brand", arrayBrand.get(position))
                        .putExtra("serial_number", arraySerialNumber.get(position))
                        .putExtra("purchase_date", arrayPurchaseDate.get(position))
                        .putExtra("nama_toko", arrayNamaToko.get(position))
                        .putExtra("price", arrayPrice.get(position))
                        .putExtra("expired_warranty", arrayExpiredWarranty.get(position))
                        .putExtra("photo_product", arrayPhotoProduct.get(position))
                        .putExtra("photo_warranty_card", arrayPhotoWarrantyCard.get(position))
                        .putExtra("activate", arrayActivate.get(position))
                        .putExtra("kategori", arrayKategori.get(position))
                        .putExtra("notes", arrayNotes.get(position)), 1);
            }
        });

        return convertView;
    }

    static class ViewHolder {
    	RelativeLayout relItem;
        TextView txtNamaProduct;
        ImageView imgProduct, imgClose;
    }

}