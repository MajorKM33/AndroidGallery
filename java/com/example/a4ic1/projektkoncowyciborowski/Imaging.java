package com.example.a4ic1.projektkoncowyciborowski;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by 4ic1 on 2016-10-07.
 */
public class Imaging{

    public static Bitmap convertB2Bm(int preSizeW, int preSizeH, byte[] data){
        Log.d("TARGET","conversion start");
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap smallBmp = Bitmap.createScaledBitmap(bitmap, preSizeW, preSizeH, false);
        Log.d("TARGET","conversion completed");
        Matrix matrix = new Matrix();
        // rotate Bitmap
        matrix.postRotate(90);
        //zwracam nową obróconą
        Bitmap rotatedBitmap = Bitmap.createBitmap(smallBmp, 0, 0, smallBmp.getWidth(), smallBmp.getHeight(), matrix, true);
        return rotatedBitmap;
    }

}
