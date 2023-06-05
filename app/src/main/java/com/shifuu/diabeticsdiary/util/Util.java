package com.shifuu.diabeticsdiary.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.shifuu.diabeticsdiary.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

import static android.content.Context.MODE_PRIVATE;

public class Util {

    public static String DATE_FORMAT = "dd.MM.yyyy";
    public static String TIME_FORMAT = "HH:mm";
    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT);



    public static byte[] getBytesFromBitmapPNG(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        return stream.toByteArray();
    }

    public static byte[] getBytesFromBitmapJPEG(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }

    public static byte[] getDefaultUserAvatarBytes(Context context) {
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_avatar);
        return getBytesFromBitmapPNG(b);
    }


    public static Bitmap getBitmapFromUriJPEG(Context context, Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static Bitmap getBitmapFromByteArray(byte[] bytes)
    {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }



    public static void setSharedPrefUserId(Context context, long uid)
    {
        SharedPreferences sp = context.getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("uId", uid);
        editor.commit();
    }

    public static long getSharedPrefUserId(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("loginInfo", MODE_PRIVATE);
        return sp.getLong("uId", 0);
    }

    public static void setSharedPrefLoggedIn(Context context, boolean isLoggerIn)
    {
        SharedPreferences sp = context.getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLoggedIn", isLoggerIn);
        editor.commit();
    }

    public static boolean getSharedPrefLoggedIn(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("loginInfo", MODE_PRIVATE);
        return sp.getBoolean("isLoggedIn", false);
    }

    public static void runAsyncTaskThread(Runnable runnable)
    {
        new Thread(runnable).start();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
