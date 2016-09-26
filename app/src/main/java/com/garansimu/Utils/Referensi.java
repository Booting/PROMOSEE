package com.garansimu.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Referensi {
    public static String url       = "http://linkincube.com/garansimu";
    public static String url2      = "http://www.linkincube.com/download";
    public static String url_img   = "http://linkincube.com/garansimu/pages/";
    public static String url_img2  = "http://www.linkincube.com/download/pictures/";
    public static String PREF_NAME = "garansimu";

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String currencyFormater(Double value) {
        DecimalFormat myFormatter = new DecimalFormat("###,###,###");
        return myFormatter.format(value);
    }

    public static void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
