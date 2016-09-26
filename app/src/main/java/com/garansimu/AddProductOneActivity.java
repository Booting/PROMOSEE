package com.garansimu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.garansimu.Utils.FontCache;
import com.garansimu.Utils.ImageFilePath;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddProductOneActivity extends Activity {
    private Typeface fontUbuntuL, fontUbuntuB;
    private TextView txtTitle, lblTakeInvoicePhoto, lblTakeProductPhoto, lblTakeWarrantyCard, lblSave,
            lblRetakeOne, lblRetakeTwo, lblRetakeThree;
    private ImageView imgLeft, imgLogoRight, imgHeader, imgTakeInvoicePhoto, imgTakeProductPhoto, imgTakeWarrantyCard;
    private static final int TAKE_INVOICE_PHOTO = 1;
    private static final int TAKE_INVOICE_PHOTO_GALLERY = 11;
    private static final int TAKE_PRODUCT_PHOTO = 2;
    private static final int TAKE_PRODUCT_PHOTO_GALLERY = 22;
    private static final int TAKE_WARRANTY_CARD = 3;
    private static final int TAKE_WARRANTY_CARD_GALLERY = 33;
    private String mCurrentPhotoPath, mTakeInvoicePath="", mTakeProductPath="", mTakeWarrantyPath="";
    private RelativeLayout relTakeInvoicePhoto, relTakeProductPhoto, relTakeWarrantyCard,
            linTakeInvoicePhoto, linTakeProductPhoto, linTakeWarrantyCard;
    private DisplayMetrics displaymetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_add_product_one);

        fontUbuntuL         = FontCache.get(this, "Ubuntu-L");
        fontUbuntuB         = FontCache.get(this, "Ubuntu-B");
        txtTitle            = (TextView) findViewById(R.id.txtTitle);
        imgLogoRight        = (ImageView) findViewById(R.id.imgLogoRight);
        lblTakeInvoicePhoto = (TextView) findViewById(R.id.lblTakeInvoicePhoto);
        lblTakeProductPhoto = (TextView) findViewById(R.id.lblTakeProductPhoto);
        lblTakeWarrantyCard = (TextView) findViewById(R.id.lblTakeWarrantyCard);
        lblSave             = (TextView) findViewById(R.id.lblSave);
        imgHeader           = (ImageView) findViewById(R.id.imgHeader);
        imgTakeInvoicePhoto = (ImageView) findViewById(R.id.imgTakeInvoicePhoto);
        imgTakeProductPhoto = (ImageView) findViewById(R.id.imgTakeProductPhoto);
        imgTakeWarrantyCard = (ImageView) findViewById(R.id.imgTakeWarrantyCard);
        relTakeInvoicePhoto = (RelativeLayout) findViewById(R.id.relTakeInvoicePhoto);
        relTakeProductPhoto = (RelativeLayout) findViewById(R.id.relTakeProductPhoto);
        relTakeWarrantyCard = (RelativeLayout) findViewById(R.id.relTakeWarrantyCard);
        linTakeInvoicePhoto = (RelativeLayout) findViewById(R.id.linTakeInvoicePhoto);
        linTakeProductPhoto = (RelativeLayout) findViewById(R.id.linTakeProductPhoto);
        linTakeWarrantyCard = (RelativeLayout) findViewById(R.id.linTakeWarrantyCard);
        lblRetakeOne        = (TextView) findViewById(R.id.lblRetakeOne);
        lblRetakeTwo        = (TextView) findViewById(R.id.lblRetakeTwo);
        lblRetakeThree      = (TextView) findViewById(R.id.lblRetakeThree);
        imgLeft             = (ImageView) findViewById(R.id.imgLeft);

        txtTitle.setText("PRODUCT VENDOR");
        txtTitle.setTypeface(fontUbuntuB);
        imgLogoRight.setVisibility(View.VISIBLE);
        lblTakeInvoicePhoto.setTypeface(fontUbuntuB);
        lblTakeProductPhoto.setTypeface(fontUbuntuB);
        lblTakeWarrantyCard.setTypeface(fontUbuntuB);
        lblSave.setTypeface(fontUbuntuB);
        lblRetakeOne.setTypeface(fontUbuntuL);
        lblRetakeTwo.setTypeface(fontUbuntuL);
        lblRetakeThree.setTypeface(fontUbuntuL);

        relTakeInvoicePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(TAKE_INVOICE_PHOTO);
            }
        });

        relTakeProductPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(TAKE_PRODUCT_PHOTO);
            }
        });

        relTakeWarrantyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(TAKE_WARRANTY_CARD);
            }
        });

        lblSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddProductOneActivity.this, AddProductTwoActivity.class);
                intent.putExtra("mTakeInvoicePath", mTakeInvoicePath);
                intent.putExtra("mTakeProductPath", mTakeProductPath);
                intent.putExtra("mTakeWarrantyPath", mTakeWarrantyPath);
                intent.putExtra("Categori", getIntent().getExtras().getString("Categori"));
                startActivity(intent);
            }
        });

        displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        ViewGroup.LayoutParams paramsHeader  = imgHeader.getLayoutParams();
        paramsHeader.height = (int) (0.06*displaymetrics.heightPixels);

        ViewGroup.LayoutParams params  = relTakeInvoicePhoto.getLayoutParams();
        params.height = (int) (0.29*displaymetrics.heightPixels);

        ViewGroup.LayoutParams paramsTakeProductPhoto  = relTakeProductPhoto.getLayoutParams();
        paramsTakeProductPhoto.height = (int) (0.29*displaymetrics.heightPixels);

        ViewGroup.LayoutParams paramsTakeWarrantyCardTakeProductPhoto  = relTakeWarrantyCard.getLayoutParams();
        paramsTakeWarrantyCardTakeProductPhoto.height = (int) (0.29*displaymetrics.heightPixels);

        ViewGroup.LayoutParams paramsSave  = lblSave.getLayoutParams();
        paramsSave.height = (int) (0.07*displaymetrics.heightPixels);

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void dispatchTakePictureIntent(final int intPosition) {
        final CharSequence[] items = { "Take Photo", "Choose from Library" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductOneActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(takePictureIntent, intPosition);
                        }
                    }
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent();
                    intent.setType("*/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    if (intPosition==TAKE_INVOICE_PHOTO) {
                        startActivityForResult(Intent.createChooser(intent, "Select File"), TAKE_INVOICE_PHOTO_GALLERY);
                    } else if (intPosition==TAKE_PRODUCT_PHOTO) {
                        startActivityForResult(Intent.createChooser(intent, "Select File"), TAKE_PRODUCT_PHOTO_GALLERY);
                    } else if (intPosition==TAKE_WARRANTY_CARD) {
                        startActivityForResult(Intent.createChooser(intent, "Select File"), TAKE_WARRANTY_CARD_GALLERY);
                    }
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_INVOICE_PHOTO && resultCode == RESULT_OK) {
            mTakeInvoicePath = mCurrentPhotoPath;
            Glide.with(AddProductOneActivity.this).load(mCurrentPhotoPath).asBitmap().into(imgTakeInvoicePhoto);
            linTakeInvoicePhoto.setVisibility(View.VISIBLE);
        } else if (requestCode == TAKE_PRODUCT_PHOTO && resultCode == RESULT_OK) {
            mTakeProductPath = mCurrentPhotoPath;
            Glide.with(AddProductOneActivity.this).load(mCurrentPhotoPath).asBitmap().into(imgTakeProductPhoto);
            linTakeProductPhoto.setVisibility(View.VISIBLE);
        } else if (requestCode == TAKE_WARRANTY_CARD && resultCode == RESULT_OK) {
            mTakeWarrantyPath = mCurrentPhotoPath;
            Glide.with(AddProductOneActivity.this).load(mCurrentPhotoPath).asBitmap().into(imgTakeWarrantyCard);
            linTakeWarrantyCard.setVisibility(View.VISIBLE);
        } else if (requestCode == TAKE_INVOICE_PHOTO_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            String selectedImagePath = ImageFilePath.getPath(getApplicationContext(), selectedImageUri);
            mTakeInvoicePath = selectedImagePath;
            Glide.with(AddProductOneActivity.this).load(selectedImagePath).asBitmap().into(imgTakeInvoicePhoto);
            linTakeInvoicePhoto.setVisibility(View.VISIBLE);
        } else if (requestCode == TAKE_PRODUCT_PHOTO_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            String selectedImagePath = ImageFilePath.getPath(getApplicationContext(), selectedImageUri);
            mTakeProductPath = selectedImagePath;
            Glide.with(AddProductOneActivity.this).load(selectedImagePath).asBitmap().into(imgTakeProductPhoto);
            linTakeProductPhoto.setVisibility(View.VISIBLE);
        } else if (requestCode == TAKE_WARRANTY_CARD_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            String selectedImagePath = ImageFilePath.getPath(getApplicationContext(), selectedImageUri);
            mTakeWarrantyPath = selectedImagePath;
            Glide.with(AddProductOneActivity.this).load(selectedImagePath).asBitmap().into(imgTakeWarrantyCard);
            linTakeWarrantyCard.setVisibility(View.VISIBLE);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
